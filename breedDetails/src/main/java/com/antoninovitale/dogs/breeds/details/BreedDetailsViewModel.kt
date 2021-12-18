package com.antoninovitale.dogs.breeds.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antoninovitale.dogs.breeds.details.data.BreedDetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreedDetailsViewModel @Inject constructor(
    private val repository: BreedDetailsRepository
) : ViewModel() {

    private val itemsStateMutable: MutableStateFlow<ItemsState> =
        MutableStateFlow(ItemsState.Loading)

    internal val itemsState: Flow<ItemsState> = itemsStateMutable

    internal fun loadData(breed: String, parent: String?) {
        viewModelScope.launch {
            itemsStateMutable.value = ItemsState.Loading
            try {
                val models = if (parent != null) {
                    repository.getSubBreedImages(parent, breed)
                } else {
                    repository.getBreedImages(breed)
                }
                itemsStateMutable.value = ItemsState.ItemsReady(models)
            } catch (ex: Exception) {
                itemsStateMutable.value = ItemsState.ItemsFailure(ex)
            }
        }
    }

    sealed class ItemsState {
        object Loading : ItemsState()
        data class ItemsReady(val items: List<String>) : ItemsState()
        data class ItemsFailure(val throwable: Throwable) : ItemsState()
    }
}
