package com.andrei.finalyearprojectapi.services

import com.andrei.finalyearprojectapi.entity.Car
import com.andrei.finalyearprojectapi.entity.LatLng
import com.andrei.finalyearprojectapi.entity.redis.CarKeys
import com.andrei.finalyearprojectapi.entity.redis.RedisKeys
import com.andrei.finalyearprojectapi.models.CarWithLocation
import com.andrei.finalyearprojectapi.repositories.SimpleCarRepository
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
        return runCatching {
            LatLng(
                latitude = carHash.getValue(CarKeys.LATITUDE.value).toDouble(),
                longitude = carHash.getValue(CarKeys.LONGITUDE.value).toDouble(),
            )
        }.getOrNull()
    }


}