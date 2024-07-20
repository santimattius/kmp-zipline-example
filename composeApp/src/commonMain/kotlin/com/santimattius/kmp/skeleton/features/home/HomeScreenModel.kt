package com.santimattius.kmp.skeleton.features.home

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.santimattius.kmp.core.ServerManager
import com.santimattius.kmp.skeleton.core.data.PictureRepository
import com.santimattius.kmp.skeleton.core.domain.Picture
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class HomeUiState(
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
    val data: Picture? = null,
)

class HomeScreenModel(
    private val repository: PictureRepository,
    private val serverManager: ServerManager
) : StateScreenModel<HomeUiState>(HomeUiState()) {

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        mutableState.update { it.copy(isLoading = false, hasError = true) }
    }

    val uiText = serverManager.data.stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    init {
        randomImage()
        serverManager.start()
    }

    fun randomImage() {
        mutableState.update { it.copy(isLoading = true, hasError = false) }
        screenModelScope.launch(exceptionHandler) {
            repository.random().onSuccess { picture ->
                mutableState.update { it.copy(isLoading = false, data = picture) }
            }.onFailure {
                mutableState.update { it.copy(isLoading = false, hasError = true) }
            }
        }
    }

    override fun onDispose() {
        serverManager.stop()
        super.onDispose()
    }
}