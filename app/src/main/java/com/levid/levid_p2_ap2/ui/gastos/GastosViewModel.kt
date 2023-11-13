package com.levid.levid_p2_ap2.ui.gastos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.levid.levid_p2_ap2.data.remote.dtos.GastoDto
import com.levid.levid_p2_ap2.data.repository.GastosRepository
import com.levid.levid_p2_ap2.data.repository.SuplidoresRepository
import com.levid.levid_p2_ap2.ui.suplidores.SuplidoresListState
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
    private val gastosRepository: GastosRepository,
    private val suplidoresRepository: SuplidoresRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(GastosListState())
    val uiState: StateFlow<GastosListState> = _uiState.asStateFlow()

    private val _uiSupState = MutableStateFlow(SuplidoresListState())
    var suplidoresState: StateFlow<SuplidoresListState> = _uiSupState.asStateFlow()

    var expandido by mutableStateOf(false)

    val id by mutableIntStateOf(0)
    var idSuplidor by mutableIntStateOf(0)
    var suplidor by mutableStateOf("")
    var fecha by mutableStateOf("")
    var concepto by mutableStateOf("")
    var ncf by mutableStateOf("")
    var itbis by mutableIntStateOf(0)
    var monto by mutableIntStateOf(0)

    //Error msg
    var msgIdSuplidor by mutableStateOf("")
    var msgFecha by mutableStateOf("")
    var msgConcepto by mutableStateOf("")
    var msgNcf by mutableStateOf("")
    var msgItbis by mutableStateOf("")
    var msgMonto by mutableStateOf("")

    private var gastoModificar: GastoDto? = null

    fun guardar(){
        if(validar()){
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
            }
            limpiar()
            cargarDatos()
        }
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
        cargarDatos()
    }
    fun limpiar(){
        fecha = ""
        idSuplidor = 0
        concepto = ""
        ncf = ""
        itbis = 0
        monto = 0
    }
    fun validar(): Boolean {
        if(idSuplidor <= 0){
            msgIdSuplidor = "Debe seleccionar un suplidor."
            return false
        }
        else if(fecha.isBlank()){
            msgFecha = "Debe colocar una fecha [yyyy-MM-dd]."
            return false
        }
        else if(concepto.isBlank()){
            msgConcepto = "Debe colocar un concepto."
            return false
        }
        else if(ncf.isBlank()){
            msgNcf = "Debe colocar un NCF"
            return false
        }
        else if(ncf.length < 11){
            msgNcf = "El máximo y mínimo de este es de 11 dígitos."
            return false
        }
        else if(itbis < 0){
            msgItbis = "El ITBIS debe ser mayor o igual a cero."
            return false
        }
        else if(monto <= 0){
            msgMonto = "El monto debe ser mayor que cero."
            return false
        }
        else
            return true
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
    fun cargarSuplidores(){
        viewModelScope.launch {
            suplidoresRepository.getSuplidores().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiSupState.update { it.copy(isLoading = true) }
                    }

                    is Resource.Success -> {
                        _uiSupState.update {
                            it.copy(
                                suplidores = result.data ?: emptyList(),
                                isLoading = false,
                                error = null
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uiSupState.update {
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
        cargarSuplidores()
    }
}