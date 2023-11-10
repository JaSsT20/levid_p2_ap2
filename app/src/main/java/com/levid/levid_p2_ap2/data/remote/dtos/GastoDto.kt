package com.levid.levid_p2_ap2.data.remote.dtos

import com.squareup.moshi.Json

data class GastoDto(
    @Json(name = "idGasto")
    val id: Int = 0,
    val fecha: String,
    val idSuplidor: Int,
    val suplidor: String? = null,
    val concepto: String,
    val descuento: Int? = 0,
    val ncf: String?,
    val itbis: Int,
    val monto: Int
)
