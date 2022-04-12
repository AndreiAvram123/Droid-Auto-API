package com.andrei.finalyearprojectapi.configuration.beans

import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.MqttGlobalPublishFilter
import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets.UTF_8


@Component
class  MqqtCredentials(
    @Value("\${mqqt.username}")  val username:String,
    @Value("\${mqqt.password}")  val password:String,
    @Value("\${mqqt.host}")  val host:String
)
@Service
class MqqtMessageGateway(
    private val credentials: MqqtCredentials,
){
    val client: Mqtt5BlockingClient = MqttClient.builder()
        .useMqttVersion5()
        .serverHost(credentials.host)
        .serverPort(8883)
        .sslWithDefaultConfig()
        .buildBlocking()


    fun configure(){
        connect()
        subscribeToTopics()
        attachListener()
    }

    private fun attachListener() {
        client.toAsync().publishes(MqttGlobalPublishFilter.SUBSCRIBED){
            println(it.topic.toString().carID())
        }
    }

    private fun String.carID():Long?{
        val regex = "(?<=/)([0-9]+)(?=/)".toRegex()
        return regex.find(this)?.value?.toLongOrNull()
    }


    private fun subscribeToTopics(){
       subscribeToCarLocation()
    }

    private fun subscribeToCarLocation(){
        client.subscribeWith()
            .topicFilter("/cars/+/location")
            .qos(MqttQos.AT_LEAST_ONCE)
            .send()


    }

    private fun connect(){
        client.connectWith()
            .simpleAuth()
            .username(credentials.username)
            .password(UTF_8.encode(credentials.password))
            .applySimpleAuth()
            .send();
    }

    fun sendMockMessage(){
        client.publishWith()
            .topic("/pupu")
            .payload("ufff".toByteArray())
            .send();
    }
}