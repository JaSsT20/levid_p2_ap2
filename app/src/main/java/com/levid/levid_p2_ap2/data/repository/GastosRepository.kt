package com.levid.levid_p2_ap2.data.repository

import com.levid.levid_p2_ap2.data.remote.GastosApi
import com.levid.levid_p2_ap2.data.remote.dtos.GastoDto
import com.levid.levid_p2_ap2.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class GastosRepository @Inject constructor(
    private val gastosApi: GastosApi
) {
    suspend fun getGastos(): Flow<Resource<List<GastoDto>>> = flow {
        try {
            emit(Resource.Loading())

            val gastos = gastosApi.getGastos()

            emit(Resource.Success(gastos))
        } catch (e: HttpException) {
            emit(Resource.Error(e.message ?: "Error HTTP"))
        } catch (e: IOException) {
            emit(Resource.Error(e.message ?: "No internet"))
        }
    }

    suspend fun getGasto(id: Int): Flow<Resource<GastoDto>> = flow {
        try {
            emit(Resource.Loading())

            val gasto = gastosApi.getGasto(id)

            emit(Resource.Success(gasto))
        } catch (e: HttpException) {
            emit(Resource.Error(e.message ?: "Error HTTP"))
        } catch (e: IOException) {
            emit(Resource.Error(e.message ?: "No internet"))
        }
    }

    suspend fun postGasto(gasto: GastoDto) = gastosApi.postGasto(gasto)
    suspend fun deleteGasto(id: Int) = gastosApi.deleteGasto(id)

//    suspend fun postGasto(gasto: GastoDto): Flow<Resource<GastoDto>> = flow {
//        try {
//            emit(Resource.Loading())
//
//            val gasto = gastosApi.postGasto(gasto)
//
//            if (gasto.isSuccessful) {
//                emit(Resource.Success(gasto.body()!!))
//            } else {
//                emit(Resource.Error(gasto.message()))
//            }
//        } catch (e: HttpException) {
//            emit(Resource.Error(e.message ?: "Error HTTP"))
//        } catch (e: IOException) {
//            emit(Resource.Error(e.message ?: "No internet"))
//        }
//    }

    //TODO PutGasto
}