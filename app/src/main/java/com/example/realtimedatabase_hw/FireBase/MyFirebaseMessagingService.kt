package com.example.realtimedatabase_hw.FireBase

import android.content.Intent
import android.util.Log
import com.example.realtimedatabase_hw.ui.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService(){

    val TAG = "Test"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
           super.onMessageReceived(remoteMessage)

        val i= Intent(applicationContext, MainActivity::class.java)
        val manager= MyNotificationManager(applicationContext)
        manager.showNotification(1,remoteMessage.data["title"].toString(),remoteMessage.data["massage"].toString(),i)



    }

    /**
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
    }







}