package com.levid.levid_p2_ap2.data.remote.dtos

import com.squareup.moshi.Json

data class SuplidorDto(
    @Json(name = "idSuplidor")
    val id: Int,
    val nombres: String
)