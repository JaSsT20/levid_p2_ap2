package com.levid.levid_p2_ap2.ui.suplidores

import com.levid.levid_p2_ap2.data.remote.dtos.SuplidorDto

data class SuplidoresListState(
    val isLoading: Boolean = false,
    val suplidores: List<SuplidorDto> = emptyList(),
    val error: String? = null
)
