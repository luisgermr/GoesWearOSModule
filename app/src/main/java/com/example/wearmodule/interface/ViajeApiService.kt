package com.example.wearmodule.`interface`

import com.example.wearmodule.models.Viaje
import retrofit2.Call
import retrofit2.http.*

interface ViajeApiService {

    @GET("api/auth/skip/viajes/{viajeId}/pasajeros")
    fun getPasajeroInfo(@Path("viajeId") viajeId: Long): Call<Viaje>
}