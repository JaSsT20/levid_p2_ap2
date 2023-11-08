package com.levid.levid_p2_ap2.data.remote.dtos

import com.squareup.moshi.Json

data class GastoDto(
    @Json(name = "idGasto")
    val id: Int,
    val fecha: String,
    val suplidor: String,
    val concepto: String,
    val ncf: String,
    val itbis: Int,
    val monto: Int
)
