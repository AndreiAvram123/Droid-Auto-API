package com.andrei.finalyearprojectapi.services

import com.andrei.finalyearprojectapi.models.DirectionStep
import com.andrei.finalyearprojectapi.request.auth.DirectionsRequest
import com.andrei.finalyearprojectapi.response.DirectionsResponse
import com.google.gson.Gson
import com.google.gson.JsonParser
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import java.lang.reflect.Type
import kotlin.reflect.jvm.javaType
import kotlin.reflect.typeOf


interface MapsService {
    fun getWalkingDirections(directionsRequest: DirectionsRequest):DirectionsResponse
}

@Service
class MapsServiceImpl (
    private val gson: Gson,
    @Value("\${directionsApiKey}")
    private val directionsApiKey:String,
    ):MapsService{
    private val requestExecutor = RestTemplate()
    private val directionsURL:String = "https://maps.googleapis.com/maps/api/directions/json"

    override fun getWalkingDirections(directionsRequest: DirectionsRequest) :DirectionsResponse{
        val requestURL = "$directionsURL?key={key}&origin={origin}&destination={destination}&mode={mode}"
        val uriVariables = mapOf<String,Any>(
            "key" to directionsApiKey,
            "origin" to "${directionsRequest.startLatitude},${directionsRequest.startLongitude}",
            "destination" to "${directionsRequest.endLatitude},${directionsRequest.endLongitude}",
             "mode" to "walking"
        )

        val rawResponse = requestExecutor.getForObject<String>(requestURL, uriVariables )

        val steps = JsonParser.parseString(rawResponse)
            .asJsonObject.getAsJsonArray("routes").get(0)
            .asJsonObject.getAsJsonArray("legs").get(0)
            .asJsonObject.getAsJsonArray("steps")

        val listType: Type = typeOf<List<DirectionStep>>().javaType

       val convertedSteps  = gson.fromJson<List<DirectionStep>>(steps,listType)
       return DirectionsResponse(
          convertedSteps
      )
    }

}