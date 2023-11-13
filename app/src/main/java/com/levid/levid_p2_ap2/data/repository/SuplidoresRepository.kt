package com.levid.levid_p2_ap2.data.repository

import com.levid.levid_p2_ap2.data.remote.GastosApi
import com.levid.levid_p2_ap2.data.remote.dtos.SuplidorDto
import com.levid.levid_p2_ap2.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class SuplidoresRepository @Inject constructor(
    private val gastosApi: GastosApi
) {
    suspend fun getSuplidores(): Flow<Resource<List<SuplidorDto>>> = flow {
        try {
            emit(Resource.Loading())

            val suplidor = gastosApi.getSuplidores()

            emit(Resource.Success(suplidor))
        } catch (e: HttpException) {
            emit(Resource.Error(e.message ?: "Error HTTP"))
        } catch (e: IOException) {
            emit(Resource.Error(e.message ?: "No internet"))
        }
    }
}