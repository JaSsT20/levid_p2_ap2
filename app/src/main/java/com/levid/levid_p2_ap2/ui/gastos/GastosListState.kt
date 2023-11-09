package com.levid.levid_p2_ap2.ui.gastos

import com.levid.levid_p2_ap2.data.remote.dtos.GastoDto

data class GastosListState(
    val isLoading: Boolean = false,
    val gastos: List<GastoDto> = emptyList(),
    val error: String? = null
)