package com.example.realtimedatabase_hw.FireBase

import com.example.realtimedatabase_hw.Constants.BASE_URL
import com.example.realtimedatabase_hw.NotifiacationAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitIInstance {

    companion object{
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        val api by lazy {
            retrofit.create(NotifiacationAPI::class.java )
        }
    }
}