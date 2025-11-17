package com.example.gaminghub.data.network.retrofit

import com.example.gaminghub.data.network.api.SolicitudModeradorApi
import com.example.gaminghub.data.network.api.UsuarioApi
import com.example.gaminghub.utils.Constantes
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitSolicitudModerador {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constantes.BASE_URL_SOLICITUD)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val solicitudModeradorApi: SolicitudModeradorApi by lazy {
        retrofit.create(SolicitudModeradorApi::class.java)
    }
}