package com.example.sensebox.ui.compose.boxdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sensebox.data.database.FavBoxRepository
import com.example.sensebox.data.model.Box
import com.example.sensebox.data.model.BoxDetail
import com.example.sensebox.data.model.FavBox
import com.example.sensebox.data.network.BoxesRepository
import com.example.sensebox.ui.compose.boxfavList.BoxFavViewModel
import com.example.sensebox.ui.compose.boxlist.BoxUiState
import com.example.sensebox.ui.navigation.NavigationDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

private const val JPG_MUNSTER = ".jpg?"
private const val IMAGE_PATH = "https://opensensemap.org/userimages/"

@HiltViewModel
class BoxDetailViewModel @Inject constructor (
    private val boxesRepository: BoxesRepository,
    private val favBoxRepository: FavBoxRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private var boxId: String = savedStateHandle[NavigationDestination.BoxDetail.savedStataHandel] ?: ""

    var uiState : BoxDetailUiState by mutableStateOf(BoxDetailUiState.Loading)
        private set

    var favoriteBoxUiState : StateFlow<FavoriteUiState> = initFavoriteUiState()
        private set

    private fun initFavoriteUiState ()  =
        favBoxRepository.getFavBoxStream(boxId)
            .map { FavoriteUiState(boxId == it?.id) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), FavoriteUiState(false)
    )

    private fun getBox(id : String) {
        viewModelScope.launch {
            uiState = try {
                val box = boxesRepository.getBoxDetail(id)
                val correctBox = box.copy(
                    image = IMAGE_PATH + box.image,
                )
                BoxDetailUiState.Success(correctBox)
            } catch (e: IOException) {
                BoxDetailUiState.Failure
            }
        }
    }

    init {
        getBox(boxId)
    }

    suspend fun addFavBox(box: BoxDetail) {
        val favBox = FavBox(
            id = box.id,
            name = box.name,
            description = box.description,
            sensorsCount = box.sensors.size
        )
        favBoxRepository.addItem(favBox)
        favoriteBoxUiState = initFavoriteUiState()

    }

    suspend fun deleteFavBox(box: BoxDetail) {
        favBoxRepository.deleteBox(box.id)
        favoriteBoxUiState = initFavoriteUiState()
    }
}

data class FavoriteUiState(val isFavorite: Boolean)

sealed interface BoxDetailUiState {
    data class Success(val detailBox: BoxDetail) : BoxDetailUiState
    object Failure : BoxDetailUiState
    object Loading : BoxDetailUiState
}