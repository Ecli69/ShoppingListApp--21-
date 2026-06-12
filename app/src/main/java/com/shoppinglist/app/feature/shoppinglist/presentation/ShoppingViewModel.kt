package com.shoppinglist.app.feature.shoppinglist.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shoppinglist.app.core.notification.NotificationCallback
import com.shoppinglist.app.feature.shoppinglist.domain.model.ShoppingItem
import com.shoppinglist.app.feature.shoppinglist.domain.repository.ShoppingRepository
import com.shoppinglist.app.feature.shoppinglist.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ShoppingViewModel"

@HiltViewModel
class ShoppingViewModel @Inject constructor(
    private val repository: ShoppingRepository
) : ViewModel() {

    private val getAllItemsUseCase = GetAllItemsUseCase(repository)
    private val addItemUseCase = AddItemUseCase(repository)
    private val checkItemUseCase = CheckItemUseCase(repository)
    private val deleteItemUseCase = DeleteItemUseCase(repository)
    private val deleteCheckedItemsUseCase = DeleteCheckedItemsUseCase(repository)

    private val _state = MutableStateFlow(ShoppingState())
    val state: StateFlow<ShoppingState> = _state.asStateFlow()

    private val _items = MutableStateFlow<List<ShoppingItem>>(emptyList())
    val items: StateFlow<List<ShoppingItem>> = _items.asStateFlow()

    init {
        loadItems()
        // Устанавливаем callback прямо в ViewModel (живёт дольше, чем экран)
        NotificationCallback.onItemChecked = { itemName ->
            Log.d(TAG, "Callback received for item: $itemName")
            toggleItemCheckByName(itemName)
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Очищаем callback при уничтожении ViewModel
        if (NotificationCallback.onItemChecked == this::toggleItemCheckByName) {
            NotificationCallback.onItemChecked = null
        }
    }

    private fun toggleItemCheckByName(itemName: String) {
        viewModelScope.launch {
            Log.d(TAG, "toggleItemCheckByName: looking for item: $itemName")
            val currentItems = _items.value
            val item = currentItems.find { it.name == itemName }
            if (item != null) {
                Log.d(TAG, "Found item: ${item.name}, current isChecked=${item.isChecked}")
                toggleItemCheck(item)
            } else {
                Log.e(TAG, "Item not found: $itemName")
            }
        }
    }

    private fun loadItems() {
        viewModelScope.launch {
            Log.d(TAG, "loadItems: loading all items")
            getAllItemsUseCase()
                .onStart { _state.update { it.copy(isLoading = true) } }
                .catch { e ->
                    Log.e(TAG, "loadItems: error", e)
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = e.message
                        )
                    }
                }
                .collect { newItems ->
                    Log.d(TAG, "loadItems: received ${newItems.size} items")
                    newItems.forEach { item ->
                        Log.d(TAG, "loadItems: item=${item.name}, isChecked=${item.isChecked}")
                    }
                    _items.value = newItems
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    fun addItem(name: String, quantity: Int, price: Double, category: String, currency: String) {
        viewModelScope.launch {
            if (name.isNotBlank() && quantity > 0 && price >= 0) {
                val newItem = ShoppingItem(
                    name = name,
                    quantity = quantity,
                    price = price,
                    category = category,
                    currency = currency
                )
                Log.d(TAG, "addItem: adding ${newItem.name}")
                addItemUseCase(newItem)
                _state.update { it.copy(showAddDialog = false) }
                loadItems()
            }
        }
    }

    fun toggleItemCheck(item: ShoppingItem) {
        viewModelScope.launch {
            Log.d(TAG, "toggleItemCheck: item=${item.name}, current isChecked=${item.isChecked}, new=${!item.isChecked}")
            checkItemUseCase(item.id, !item.isChecked)
            delay(200)
            loadItems()
        }
    }

    fun deleteItem(item: ShoppingItem) {
        viewModelScope.launch {
            Log.d(TAG, "deleteItem: item=${item.name}")
            deleteItemUseCase(item)
            loadItems()
        }
    }

    fun deleteCheckedItems() {
        viewModelScope.launch {
            Log.d(TAG, "deleteCheckedItems")
            deleteCheckedItemsUseCase()
            loadItems()
        }
    }

    fun showAddDialog(show: Boolean) {
        _state.update { it.copy(showAddDialog = show) }
    }

    fun setCurrentItemForService(item: ShoppingItem) {
        _state.update { it.copy(currentServiceItem = item) }
    }

    fun refreshItems() {
        loadItems()
    }

    data class ShoppingState(
        val isLoading: Boolean = false,
        val error: String? = null,
        val showAddDialog: Boolean = false,
        val currentServiceItem: ShoppingItem? = null
    )
}