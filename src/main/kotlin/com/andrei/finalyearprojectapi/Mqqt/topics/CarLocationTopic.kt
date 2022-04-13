package com.andrei.finalyearprojectapi.Mqqt.topics

import com.andrei.finalyearprojectapi.entity.LatLng
import com.andrei.finalyearprojectapi.entity.redis.CarKeys
import com.andrei.finalyearprojectapi.entity.redis.RedisKeys
import com.andrei.finalyearprojectapi.utils.unixTime
import com.google.gson.Gson
import com.hivemq.client.mqtt.datatypes.MqttTopic
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.api.sync.RedisCommands
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets

interface ITopic{
    val route:String
    fun matchesRoute(topic: MqttTopic):Boolean
    fun handleAction(callbackData: Mqtt5Publish)
}


@Component
class CarLocationTopic(
    redisConnection: StatefulRedisConnection<String, String>,
    private val gson: Gson
) : ITopic{

    internal data class Payload(
        val latitude:Double,
        val longitude:Double
    )

    private val pathRegex = "/cars/[0-9]+/location".toRegex()


    private val commands: RedisCommands<String, String> = redisConnection.sync()
    override val route: String = "/cars/+/location"

    override fun matchesRoute(topic: MqttTopic):Boolean  =topic.toString().matches(pathRegex)

    override fun handleAction(callbackData: Mqtt5Publish) {
        //defensive check, make sure not to trigger this method before checking with matchRoute
        if(!matchesRoute(callbackData.topic)) return

        val payload:Payload? = callbackData.parsePayload()
        if(payload != null){
            runCatching {
                updateLocation(
                    carID = callbackData.topic.carID() ?: throw Exception("car id should not be null"),
                    location = LatLng(
                        latitude = payload.latitude,
                        longitude = payload.longitude
                    )
                ) }.onFailure {
                   println(it)
            }
        }
    }

    private fun Mqtt5Publish.parsePayload():Payload?{
        return runCatching {
            val actualPayload = this.payload.orElseThrow()
            val stringPayload = StandardCharsets.UTF_8.decode(actualPayload).toString()
            return gson.fromJson(stringPayload,Payload::class.java)
        }.getOrNull()
    }

    private fun updateLocation(
        carID:Long,
        location:LatLng
    ){
        val carKey = RedisKeys.car.format(carID)
        commands.hmset(
            carKey,
            mapOf(
                CarKeys.LOCATION_UPDATED_AT.value to unixTime().toString(),
                CarKeys.LATITUDE.value to location.latitude.toString(),
                CarKeys.LONGITUDE.value to location.longitude.toString()
            )
        )
    }


    private fun MqttTopic.carID():Long?{
        val regex = "(?<=/)([0-9]+)(?=/)".toRegex()
        return regex.find(this.toString())?.value?.toLongOrNull()
    }

}