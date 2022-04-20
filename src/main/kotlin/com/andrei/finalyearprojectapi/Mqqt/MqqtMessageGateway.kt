package com.andrei.finalyearprojectapi.Mqqt

import com.andrei.finalyearprojectapi.Mqqt.topics.CarLocationTopic
import com.hivemq.client.mqtt.MqttGlobalPublishFilter
import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets

@Service
class MqqtMessageGateway(
    private val client: Mqtt5BlockingClient,
    private val carLocationTopic: CarLocationTopic,
    private val credentials: MqqtCredentials,
){

    fun configure(){
        connect()
        subscribeToTopics()
        attachListener()
    }

    private fun attachListener() {
        client.toAsync().publishes(MqttGlobalPublishFilter.SUBSCRIBED){
            when{
                carLocationTopic.matchesRoute(it.topic) -> {
                    carLocationTopic.handleAction(it)
                }
            }
        }
    }



    private fun subscribeToTopics(){
       subscribeToCarLocation()
    }

    private fun subscribeToCarLocation(){
        client.subscribeWith()
            .topicFilter(carLocationTopic.route)
            .qos(MqttQos.AT_LEAST_ONCE)
            .send()


    }

    private fun connect(){
        client.connectWith()
            .simpleAuth()
            .username(credentials.username)
            .password(StandardCharsets.UTF_8.encode(credentials.password))
            .applySimpleAuth()
            .send();
    }

}