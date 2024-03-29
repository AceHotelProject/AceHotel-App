package com.project.acehotel.core.data.source.remote

import android.content.Context
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import timber.log.Timber

class MQTTService(context: Context?) {

    private var mqttClient = MqttAndroidClient(context, server, clientId)

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

    companion object {
        private const val server = "tcp://91.108.104.92:1883"
        private const val clientId = "test-app"
    }
}