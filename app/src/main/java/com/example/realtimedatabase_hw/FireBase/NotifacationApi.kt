package com.example.realtimedatabase_hw

import com.example.realtimedatabase_hw.Constants.CONTENT_TYPE
import com.example.realtimedatabase_hw.Constants.SERVER_KEY
import com.example.realtimedatabase_hw.FireBase.PushNotification
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotifiacationAPI {
    @Headers("Authorization: key=$SERVER_KEY", "Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotifiacation(
        @Body notification: PushNotification
    ): Response<ResponseBody>
}