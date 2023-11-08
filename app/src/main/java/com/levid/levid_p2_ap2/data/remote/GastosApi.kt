package com.levid.levid_p2_ap2.data.remote

import com.levid.levid_p2_ap2.data.remote.dtos.GastoDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface GastosApi {
    @GET("api/Gastos")
    suspend fun getGastos(): List<GastoDto>
    @POST("api/Gastos")
    suspend fun postGasto(@Body gasto: GastoDto) : Response<GastoDto>

    @GET("api/Gastos/{id}")
    suspend fun getGasto(id: Int): GastoDto

    @PUT("api/Gastos/{id}")
    suspend fun putGasto(id: Int, gasto: GastoDto)
}