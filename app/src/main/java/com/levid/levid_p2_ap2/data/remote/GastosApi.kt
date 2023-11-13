package com.levid.levid_p2_ap2.data.remote

import com.levid.levid_p2_ap2.data.remote.dtos.GastoDto
import com.levid.levid_p2_ap2.data.remote.dtos.SuplidorDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface GastosApi {
    @GET("api/Gastos")
    suspend fun getGastos(): List<GastoDto>
    @POST("api/Gastos")
    suspend fun postGasto(@Body gasto: GastoDto) : Response<GastoDto>

    @GET("api/Gastos/{id}")
    suspend fun getGasto(id: Int): GastoDto

    @PUT("api/Gastos/{id}")
    suspend fun putGasto(@Path("id") id: Int,@Body gasto: GastoDto): Response<GastoDto>

    @DELETE("api/Gastos/{id}")
    suspend fun deleteGasto(@Path("id") id: Int): Response<GastoDto>

    @GET("api/SuplidoresGastos")
    suspend fun getSuplidores(): List<SuplidorDto>
}