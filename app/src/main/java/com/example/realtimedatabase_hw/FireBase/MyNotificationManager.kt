package com.example.realtimedatabase_hw.FireBase

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.realtimedatabase_hw.R

class MyNotificationManager(var context: Context) {
    private val id_Notification=200;
    private val id_Channl="id_Channl";

    fun showNotification(
        id:Int,
        title:String,
        massge:String,
        intent: Intent

    ){
        var pendingIntent: PendingIntent? = null
        pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(context, id_Notification, intent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getActivity(context, id_Notification, intent, PendingIntent.FLAG_ONE_SHOT)
        }
        val builder= NotificationCompat.Builder(context,id_Channl)
        val notification: Notification
        notification=builder.setSmallIcon(R.drawable.ic_baseline_library_books_24).setContentIntent(pendingIntent).setContentTitle(title).setContentText(massge).setAutoCancel(true).build()
        val notificationManeger=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val channel= NotificationChannel(id_Channl,"Channel Noti", NotificationManager.IMPORTANCE_HIGH)
            channel.setShowBadge(true)
            channel.description=massge
            channel.enableLights(true)
            channel.enableVibration(true)
            notificationManeger.createNotificationChannel(channel)
        }
        notificationManeger.notify(id,notification)
    }
}