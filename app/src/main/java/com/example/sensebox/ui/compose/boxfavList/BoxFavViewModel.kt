package com.example.sensebox.ui.compose.boxfavList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sensebox.data.database.FavBoxRepository
import com.example.sensebox.data.model.Box
import com.example.sensebox.data.model.BoxDetail
import com.example.sensebox.data.model.FavBox
import com.example.sensebox.data.model.Sensor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BoxFavViewModel @Inject constructor(
    private val favBoxRepository: FavBoxRepository
) : ViewModel() {
    val favBoxUiState : StateFlow<FavBoxUiState> =
        favBoxRepository.getAllFavBoxesStream()
            .map { favBoxes ->
                FavBoxUiState(
                    favBoxes.map { favBox ->
                        Box(
                            id = favBox.id,
                            name = favBox.name,
                            description = favBox.description,
                            sensors = List(favBox.sensorsCount) { Sensor() }
                        )
                    }.toList() )
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000L),
                FavBoxUiState()
            )

    fun deleteFavBox(id: String) {
        viewModelScope.launch {
            favBoxRepository.deleteBox(id)
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            favBoxRepository.deleteAll()
        }
    }
}

data class FavBoxUiState (val favBoxes: List<Box> = emptyList())



