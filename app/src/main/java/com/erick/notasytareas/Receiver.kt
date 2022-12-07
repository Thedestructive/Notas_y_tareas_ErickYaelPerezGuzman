package com.erick.notasytareas

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

var notificationID = 1
val tituloExtra2 ="Tarea Pendiente"
const val mensajeExtra2 = "messageExtra"

class Receiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationUtils = Notificacion(context)
        val notification = intent.getStringExtra(tituloExtra2)
            ?.let { notificationUtils.getNotificationBuilder(it,
                intent.getStringExtra(mensajeExtra2)!!
            ).build() }
        notificationUtils.getManager().notify(notificationID++, notification)
    }
}