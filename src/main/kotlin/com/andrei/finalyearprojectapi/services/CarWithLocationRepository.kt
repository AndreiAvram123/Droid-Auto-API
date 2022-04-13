package com.andrei.finalyearprojectapi.services

import com.andrei.finalyearprojectapi.entity.Car
import com.andrei.finalyearprojectapi.entity.LatLng
import com.andrei.finalyearprojectapi.entity.redis.CarKeys
import com.andrei.finalyearprojectapi.entity.redis.RedisKeys
import com.andrei.finalyearprojectapi.models.CarWithLocation
import com.andrei.finalyearprojectapi.repositories.SimpleCarRepository
import com.andrei.finalyearprojectapi.utils.unixTime
import io.lettuce.core.api.StatefulRedisConnection
import org.springframework.stereotype.Repository

interface CarWithLocationRepository{
    fun findAll():List<CarWithLocation>
}


@Repository
class CarWithLocationRepositoryImpl(
    redisConnection: StatefulRedisConnection<String, String>,
    private val simpleCarRepository: SimpleCarRepository,
):CarWithLocationRepository {

    private val invalidateLocationSeconds = 40
    private val commands = redisConnection.sync()

    override fun findAll(): List<CarWithLocation> {
        val cars = simpleCarRepository.findAll()
        return cars.mapNotNull {
            val location = getCarLocation(it)
            if(location !=null){
                CarWithLocation(
                    car = it,
                    location = getCarLocation(it) ?: throw Exception()
                )
            }else{
                null
            }
        }
    }


    private fun getCarLocation(car: Car):LatLng?{
        val keyCar = RedisKeys.car.format(car.id)
        val carHash  = commands.hgetall(
            keyCar
        )
        //location is not considered valid if it was not updated within the last 40 seconds
        val lastUpdatedTime = carHash[CarKeys.LOCATION_UPDATED_AT.value]?.toLongOrNull() ?: return null
        if(lastUpdatedTime + invalidateLocationSeconds < unixTime()){
            return null
        }

        return runCatching {
            LatLng(
                latitude = carHash.getValue(CarKeys.LATITUDE.value).toDouble(),
                longitude = carHash.getValue(CarKeys.LONGITUDE.value).toDouble(),
            )
        }.getOrNull()
    }


}