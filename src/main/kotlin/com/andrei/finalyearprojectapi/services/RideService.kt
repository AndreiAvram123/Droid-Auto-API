package com.andrei.finalyearprojectapi.services

import com.andrei.finalyearprojectapi.Mqqt.commands.LockCarCommand
import com.andrei.finalyearprojectapi.Mqqt.commands.UnlockCarCommand
import com.andrei.finalyearprojectapi.configuration.Response
import com.andrei.finalyearprojectapi.entity.Car
import com.andrei.finalyearprojectapi.entity.FinishedRide
import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.entity.redis.*
import com.andrei.finalyearprojectapi.repositories.FinishedRideRepository
import com.andrei.finalyearprojectapi.repositories.SimpleCarRepository
import com.andrei.finalyearprojectapi.repositories.UserRepository
import com.andrei.finalyearprojectapi.request.auth.PaymentRequest
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
    private val simpleCarRepository: SimpleCarRepository,
    private val finishedRideRepository: FinishedRideRepository,
    private val paymentService: PaymentService,
    private val unlockCarCommand: UnlockCarCommand,
    private val lockCarCommand: LockCarCommand
) :RideService {


    private val commands = redisConnection.sync()



    override fun getOngoingRide(user: User): OngoingRide? {
        val keyUserRide = RedisKeys.userRide.format(user.id)

        val ongoingRideMap =  commands.hgetall(keyUserRide)
        if(ongoingRideMap.isEmpty()){
            return null
        }
        return ongoingRideMap.toRide()
    }


    override fun startRide(reservation: Reservation): Response<OngoingRide> {

        val currentTime = unixTime()
        deleteReservation(reservation)
        updateCarStatus(reservation.car, CarStatus.IN_USE)
        val rideMap =  mapOf(
            RideKeys.TIME_STARTED.value to currentTime.toString(),
            RideKeys.CAR_ID.value to reservation.car.id.toString(),
            RideKeys.USER_ID.value to reservation.user.id.toString(),
        )
        commands.hmset(
            RedisKeys.userRide.format(reservation.user.id),
            rideMap
        )
        val ride = rideMap.toRide()?: return Response.Error("Conversion error");
        unlockCarCommand.execute(reservation.car.id)
        return Response.Success(ride)
    }

    private fun chargeUser(finishedRide: FinishedRide){
         val paymentRequest = PaymentRequest(
             amount = finishedRide.totalCharge,
             user = finishedRide.user
         )
        paymentService.chargeFuturePayment(paymentRequest)
    }

    override fun finishRide(user: User):  RideService.FinishRideResponse {
        val ongoingRide = getOngoingRide(user) ?: return RideService.FinishRideResponse.NoRideFound

         val finishedRide = ongoingRide.toFinishedRide()

         val persistedRide = finishedRideRepository.save(finishedRide)
         user.finishedRides.add(persistedRide)
         userRepository.save(user)
        //create charge

        chargeUser(finishedRide)

        deleteRideFromRedis(ongoingRide)
        updateCarStatus(finishedRide.car, CarStatus.AVAILABLE)


        lockCarCommand.execute(finishedRide.car.id)
        return RideService.FinishRideResponse.Success(persistedRide)
    }

    private fun deleteRideFromRedis(ride: OngoingRide){
        val keyUserRide = RedisKeys.userRide.format(ride.user.id)
        commands.del(keyUserRide)
    }

    private fun updateCarStatus(car: Car, status: CarStatus) {
        val carKey = RedisKeys.car.format(car.id)
        commands.apply {
            persist(carKey)
            hset(
                carKey,
                mapOf(
                    CarKeys.STATUS.value to status.value
                )
            )
        }
    }
    private fun deleteReservation(reservation: Reservation){
        val key = RedisKeys.userCarReservation.format(reservation.user.id)
        commands.del(key)
    }


    private fun Map<String,String>.toRide():OngoingRide?{
        return runCatching {
            OngoingRide(
                startTime = getValue(RideKeys.TIME_STARTED.value).toLong(),
                car = simpleCarRepository.findByIdOrNull(getValue(RideKeys.CAR_ID.value).toLong()) ?: throw Exception(),
                user = userRepository.findByIdOrNull(getValue(RideKeys.USER_ID.value).toLong()) ?: throw Exception(),
            )
        }.getOrNull()
    }



}