package com.levid.levid_p2_ap2.ui.suplidores

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.levid.levid_p2_ap2.data.repository.SuplidoresRepository
import com.levid.levid_p2_ap2.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SuplidoresViewModel @Inject constructor(
    private val suplidorRepository: SuplidoresRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(SuplidoresListState())
    val uiState: StateFlow<SuplidoresListState> = _uiState.asStateFlow()
    init {
        viewModelScope.launch {
            suplidorRepository.getSuplidores().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                suplidores = result.data ?: emptyList(),
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