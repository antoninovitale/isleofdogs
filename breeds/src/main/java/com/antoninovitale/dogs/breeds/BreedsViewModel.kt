package com.antoninovitale.dogs.breeds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antoninovitale.dogs.breeds.data.BreedsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreedsViewModel @Inject constructor(
    private val repository: BreedsRepository,
    private val breedsItemModelsMapper: BreedsItemModelsMapper
) : ViewModel() {

    private val itemsStateMutable: MutableStateFlow<ItemsState> =
        MutableStateFlow(ItemsState.ItemsReady(emptyList()))
    private val itemClickChannel: Channel<BreedsItemModel> = Channel()

    val itemsState: Flow<ItemsState> = itemsStateMutable
    val itemClickResult: Flow<BreedsItemModel> = itemClickChannel.receiveAsFlow()

    init {
        loadData()
    }

    fun onItemClicked(item: BreedsItemModel) {
        viewModelScope.launch {
            itemClickChannel.send(item)
        }
    }

    fun loadData() {
        viewModelScope.launch {
            itemsStateMutable.value = ItemsState.Loading
            try {
                val models = breedsItemModelsMapper.map(repository.getBreeds())
                itemsStateMutable.value = ItemsState.ItemsReady(models)
            } catch (ex: Exception) {
                itemsStateMutable.value = ItemsState.ItemsFailure(ex)
            }
        }
    }

    sealed class ItemsState {
        object Loading : ItemsState()
        data class ItemsReady(val items: List<BreedsItemModel>) : ItemsState()
        data class ItemsFailure(val throwable: Throwable) : ItemsState()
    }
}
