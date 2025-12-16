package com.example.cobasupabase.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cobasupabase.data.repositories.PlaceRepository
import com.example.cobasupabase.domain.model.Place
import com.example.cobasupabase.data.dto.PlaceDto
import com.example.cobasupabase.ui.common.UiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PlaceViewModel(
    private val repo: PlaceRepository = PlaceRepository()
) : ViewModel() {

    // State untuk daftar tempat
    private val _state = MutableStateFlow<UiResult<List<Place>>>(UiResult.Idle)
    val state: StateFlow<UiResult<List<Place>>> = _state.asStateFlow()

    // Fungsi load semua tempat
    fun loadPlaces() {
        viewModelScope.launch {
            _state.value = UiResult.Loading
            try {
                val data = repo.getPlaces()
                _state.value = UiResult.Success(data)
            } catch (e: Exception) {
                _state.value = UiResult.Error(e.message ?: "Gagal memuat data tempat")
            }
        }
    }

    // Tambah tempat baru
    fun addPlace(
        name: String,
        address: String,
        rating: Double?,
        userId: String,
        imageUrl: String? = null
    ) {
        viewModelScope.launch {
            try {
                repo.createPlace(
                    name = name,
                    address = address,
                    rating = rating ?: 0.0,
                    userId = userId,
                    imageUrl = imageUrl
                )
                loadPlaces()
            } catch (e: Exception) {
                _state.value = UiResult.Error(e.message ?: "Gagal menambah tempat")
            }
        }
    }



    // Hapus tempat
    fun deletePlace(id: Int) {
        viewModelScope.launch {
            try {
                repo.deletePlace(id)
                loadPlaces()
            } catch (e: Exception) {
                _state.value = UiResult.Error(e.message ?: "Gagal menghapus tempat")
            }
        }
    }

    // Reset state daftar tempat
    fun resetState() {
        _state.value = UiResult.Idle
    }

    // State untuk detail tempat
    private val _placeDetailState = MutableStateFlow<UiResult<Place>>(UiResult.Idle)
    val placeDetailState: StateFlow<UiResult<Place>> = _placeDetailState.asStateFlow()

    // Load detail tempat berdasarkan ID (opsional, bisa dipakai untuk load terpisah)
    fun loadPlaceById(placeId: Int) {
        viewModelScope.launch {
            _placeDetailState.value = UiResult.Loading
            try {
                val place = repo.getPlaceById(placeId)
                if (place != null) {
                    _placeDetailState.value = UiResult.Success(place)
                } else {
                    _placeDetailState.value = UiResult.Error("Tempat tidak ditemukan")
                }
            } catch (e: Exception) {
                _placeDetailState.value =
                    UiResult.Error(e.message ?: "Gagal memuat detail tempat")
            }
        }
    }

    fun resetPlaceDetailState() {
        _placeDetailState.value = UiResult.Idle
    }

    // ===== Tambahan untuk Compose PlaceDetailScreen =====
    // Ambil place dari list state berdasarkan ID
    fun getPlaceById(placeId: Int): Flow<Place?> {
        return state.map { result ->
            when (result) {
                is UiResult.Success -> result.data.find { it.id == placeId }
                else -> null
            }
        }
    }
}
