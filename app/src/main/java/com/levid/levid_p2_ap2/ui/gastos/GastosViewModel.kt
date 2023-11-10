package com.levid.levid_p2_ap2.ui.gastos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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

    val id by mutableIntStateOf(0)
    var idSuplidor by mutableIntStateOf(0)
    var fecha by mutableStateOf("")
    var concepto by mutableStateOf("")
    var ncf by mutableStateOf("")
    var itbis by mutableIntStateOf(0)
    var monto by mutableIntStateOf(0)

    private var gastoModificar: GastoDto? = null

    fun guardar(){
        viewModelScope.launch {
            try{
                if(gastoModificar != null){
                    modificar()
                }
                else{
                    insertar()
                }

            }catch (ex: Exception){
                _uiState.update { it.copy(error = ex.message ?: "Error") }
            }
            insertar()
        }
        limpiar()
        cargarDatos()
    }
    fun modificar(){
        viewModelScope.launch {
            gastosRepository.putGasto(gastoModificar!!.id, gastoModificar!!)
        }
    }
    fun insertar(){
        viewModelScope.launch {
            gastosRepository.postGasto(
                GastoDto(
                    fecha = fecha,
                    idSuplidor = idSuplidor,
                    concepto = concepto,
                    ncf = ncf,
                    itbis = itbis,
                    monto = monto
                )
            )
        }
    }
    fun modificarGasto(gasto: GastoDto) {
        gastoModificar = gasto
        // Rellena los inputs con los datos del gasto
        fecha = gasto.fecha
        idSuplidor = gasto.idSuplidor
        concepto = gasto.concepto
        ncf = gasto.ncf ?: ""
        itbis = gasto.itbis
        monto = gasto.monto
    }
    fun delete(id: Int) {
        viewModelScope.launch {
            gastosRepository.deleteGasto(id)
        }
    }
    fun limpiar(){
        fecha = ""
        idSuplidor = 0
        concepto = ""
        ncf = ""
        itbis = 0
        monto = 0
    }
    fun cargarDatos(){
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
    init{
        cargarDatos()
    }
}