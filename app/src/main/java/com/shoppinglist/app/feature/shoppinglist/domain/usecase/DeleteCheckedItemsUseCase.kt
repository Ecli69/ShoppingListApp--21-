package com.shoppinglist.app.feature.shoppinglist.domain.usecase

import com.shoppinglist.app.feature.shoppinglist.domain.repository.ShoppingRepository

class DeleteCheckedItemsUseCase(
    private val repository: ShoppingRepository
) {
    suspend operator fun invoke() = repository.deleteCheckedItems()
}