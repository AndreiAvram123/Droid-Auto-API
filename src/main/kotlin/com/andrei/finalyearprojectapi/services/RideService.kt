package com.andrei.finalyearprojectapi.services

import com.andrei.finalyearprojectapi.configuration.Response
import com.andrei.finalyearprojectapi.entity.Car
import com.andrei.finalyearprojectapi.entity.FinishedRide
import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.entity.redis.*
import com.andrei.finalyearprojectapi.repositories.CarRepository
import com.andrei.finalyearprojectapi.repositories.FinishedRideRepository
import com.andrei.finalyearprojectapi.repositories.UserRepository
import com.andrei.finalyearprojectapi.utils.unixTime
import io.lettuce.core.api.StatefulRedisConnection
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

interface RideService{
    fun getOngoingRide(user:User): OngoingRide?
    fun startRide(reservation: Reservation):Response<OngoingRide>
    fun finishRide(user:User):FinishRideResponse

    sealed class FinishRideResponse{
        object NoRideFound:FinishRideResponse()
        object UserInCar:FinishRideResponse()
        data class Success(val finishedRide: FinishedRide):FinishRideResponse()
    }
}

@Service
class RideServiceImpl(
    redisConnection: StatefulRedisConnection<String, String>,
    private val userRepository: UserRepository,
    private val carRepository: CarRepository,
    private val finishedRideRepository: FinishedRideRepository
) :RideService {


    private val commands = redisConnection.sync()



    override fun getOngoingRide(user: User): OngoingRide? {
        val keyUserRide = FormatKeys.userRide.format(user.id)

        val ongoingRideMap =  commands.hgetall(keyUserRide)
        if(ongoingRideMap.isEmpty()){
            return null
        }
        return ongoingRideMap.toRide()
    }


    override fun startRide(reservation: Reservation): Response<OngoingRide> {

        val currentTime = unixTime()
        deleteReservation(reservation)
        updateCarStatus(reservation.car)
        val rideMap =  mapOf(
            RideKeys.TIME_STARTED.value to currentTime.toString(),
            RideKeys.CAR_ID.value to reservation.car.id.toString(),
            RideKeys.USER_ID.value to reservation.user.id.toString(),
        )
        commands.hmset(
            FormatKeys.userRide.format(reservation.user.id),
            rideMap
        )
        val ride = rideMap.toRide()?: return Response.Error("Conversion error");
        return Response.Success(ride)
    }

    override fun finishRide(user: User):  RideService.FinishRideResponse {
        val ongoingRide = getOngoingRide(user) ?: return RideService.FinishRideResponse.NoRideFound

        val finishedRide = ongoingRide.toFinishedRide()
        //create charge

        finishedRideRepository.save(finishedRide)

        clearRideDataRedis(ongoingRide)
        return RideService.FinishRideResponse.Success(finishedRide)

        //todo

        //communicate with arduino
    }

    private fun clearRideDataRedis(ride: OngoingRide){
        val keyUserRide = FormatKeys.userRide.format(ride.user.id)
        val carKey = FormatKeys.car.format(ride.car.id)
        commands.del(carKey)
        commands.del(keyUserRide)
    }

    private fun updateCarStatus(car: Car) {
        val carKey = FormatKeys.car.format(car.id)
        commands.apply {
            persist(carKey)
            hset(
                carKey,
                mapOf(
                    CarKeys.STATUS.value to CarStatus.IN_USE.value
                )
            )
        }
    }
    private fun deleteReservation(reservation: Reservation){
        val key = FormatKeys.userReservation.format(reservation.user.id)
        commands.del(key)
    }


    private fun Map<String,String>.toRide():OngoingRide?{
        return runCatching {
            OngoingRide(
                startedTime = getValue(RideKeys.TIME_STARTED.value).toLong(),
                car = carRepository.findByIdOrNull(getValue(RideKeys.CAR_ID.value).toLong()) ?: throw Exception(),
                user = userRepository.findByIdOrNull(getValue(RideKeys.USER_ID.value).toLong()) ?: throw Exception(),
            )
        }.getOrNull()
    }



}