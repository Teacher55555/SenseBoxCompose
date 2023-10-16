package com.example.sensebox.ui.compose.boxlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sensebox.data.model.Box
import com.example.sensebox.data.network.BoxesRepository
import com.example.sensebox.ui.navigation.NavigationDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class BoxListViewModel @Inject constructor(
    private val boxesRepository: BoxesRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

   private var distance: Int = savedStateHandle[NavigationDestination.BoxList.savedStataHandel] ?: 10
       get() = field * 1000

   var uiState : BoxUiState by mutableStateOf(BoxUiState.Loading)
       private set

   private fun getBoxes(maxDistance : Int = distance) {
       viewModelScope.launch {
           uiState = try {
               val boxes = boxesRepository.getBoxes(maxDistance)
               BoxUiState.Success(boxes)
           } catch (e: IOException) {
               BoxUiState.Failure
           }
       }
   }
    init {
        getBoxes()
    }
}

sealed interface BoxUiState {
    data class Success(val boxes: List<Box>) : BoxUiState
    object Failure : BoxUiState
    object Loading : BoxUiState
}