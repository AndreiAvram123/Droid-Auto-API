package com.andrei.finalyearprojectapi.services

import com.andrei.finalyearprojectapi.models.CarWithLocation
import com.andrei.finalyearprojectapi.repositories.SimpleCarRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

interface CarWithLocationRepository{
    fun findAll():List<CarWithLocation>
    fun findByIdOrNull(id:Long):CarWithLocation?
}


@Repository
class CarWithLocationRepositoryImpl(
    private val simpleCarRepository: SimpleCarRepository,
    private val carLocationService: CarLocationService
):CarWithLocationRepository {


    override fun findAll(): List<CarWithLocation> {
        val cars = simpleCarRepository.findAll()
        return cars.mapNotNull {
            val location = carLocationService.getCarLocation(it) ?: return@mapNotNull  null
            return@mapNotNull CarWithLocation(
                    car = it,
                    location = location
                )
            }
        }

    //todo
    //remove this
    override fun findByIdOrNull(id: Long): CarWithLocation? {
         val car = simpleCarRepository.findByIdOrNull(id)  ?: return null
         val location =  carLocationService.getCarLocation(car)  ?: return null
         return CarWithLocation(
            car = car,
            location = location
        )
    }


}