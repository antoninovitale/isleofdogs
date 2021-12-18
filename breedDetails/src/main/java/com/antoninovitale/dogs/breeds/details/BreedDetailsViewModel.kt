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
        MutableStateFlow(ItemsState.ItemsReady(emptyList()))

    val itemsState: Flow<ItemsState> = itemsStateMutable

    /**
     * Load images from a repository.
     * Initial state is an empty list.
     * The state is then collected through a flow in the view.
     *
     * @param breed The breed/sub-breed to load images for
     * @param parent The breed of a sub-breed if present
     *
     * @see [BreedDetailsFragment.onViewCreated] for more.
     */
    fun loadData(breed: String, parent: String?) {
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
