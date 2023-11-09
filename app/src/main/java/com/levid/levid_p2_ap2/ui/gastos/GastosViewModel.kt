package com.levid.levid_p2_ap2.ui.gastos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.levid.levid_p2_ap2.data.remote.dtos.GastoDto
import com.levid.levid_p2_ap2.data.repository.GastosRepository
import com.levid.levid_p2_ap2.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GastosViewModel @Inject constructor(
    private val gastosRepository: GastosRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(GastosListState())
    val uiState: StateFlow<GastosListState> = _uiState.asStateFlow()

    var suplidor by mutableStateOf("")
    var fecha by mutableStateOf("")
    var concepto by mutableStateOf("")
    var ncf by mutableStateOf("")
    var itbis by mutableStateOf(0)
    var monto by mutableStateOf(0)

    fun guardar(){
        viewModelScope.launch {
            gastosRepository.postGasto(
                GastoDto(
                    id = 0,
                    fecha = fecha,
                    suplidor = suplidor,
                    concepto = concepto,
                    ncf = ncf,
                    itbis = itbis,
                    monto = monto
                )
            )
        }
    }
    init{
        viewModelScope.launch {
            gastosRepository.getGastos().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                gastos = result.data ?: emptyList(),
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                error = result.message ?: "Error",
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }
}