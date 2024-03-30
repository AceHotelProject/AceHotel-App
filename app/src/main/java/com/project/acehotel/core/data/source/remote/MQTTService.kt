package com.project.acehotel.core.data.source.remote

import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import timber.log.Timber
import javax.inject.Inject

class MQTTService @Inject constructor(private val mqttClient: MqttAndroidClient) {

    fun connect(
        cbConnect: IMqttActionListener,
        cbClient: MqttCallback
    ) {
        mqttClient.setCallback(cbClient)

        try {
            mqttClient.connect(MqttConnectOptions(), null, cbConnect)
        } catch (e: Exception) {
            Timber.tag("MQTT").e(e.toString())
        }
    }

    fun subscribe(
        topic: String,
        qos: Int = 1,
        cbSubscribe: IMqttActionListener
    ) {
        try {
            mqttClient.subscribe(topic, qos, null, cbSubscribe)
        } catch (e: MqttException) {
            Timber.tag("MQTT").e(e.toString())
        }
    }

    fun unsubscribe(
        topic: String,
        cbUnsubscribe: IMqttActionListener
    ) {
        try {
            mqttClient.unsubscribe(topic, null, cbUnsubscribe)
        } catch (e: MqttException) {
            Timber.tag("MQTT").e(e.toString())
        }
    }

    fun publish(
        topic: String,
        msg: String,
        qos: Int = 1,
        retained: Boolean = false,
        cbPublish: IMqttActionListener
    ) {
        try {
            val message = MqttMessage()
            message.payload = msg.toByteArray()
            message.qos = qos
            message.isRetained = retained
            mqttClient.publish(topic, message, null, cbPublish)
        } catch (e: Exception) {
            Timber.tag("MQTT").e(e.toString())
        }
    }

    fun disconnect(cbDisconnect: IMqttActionListener) {
        try {
            mqttClient.disconnect(null, cbDisconnect)
        } catch (e: MqttException) {
            Timber.tag("MQTT").e(e.toString())
        }
    }

    fun isConnected(): Boolean {
        return mqttClient.isConnected
    }
}