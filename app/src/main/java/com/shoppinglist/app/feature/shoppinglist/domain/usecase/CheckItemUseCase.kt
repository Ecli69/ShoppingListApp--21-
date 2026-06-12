package com.shoppinglist.app.feature.shoppinglist.domain.usecase

import com.shoppinglist.app.feature.shoppinglist.domain.repository.ShoppingRepository

class CheckItemUseCase(
    private val repository: ShoppingRepository
) {
    suspend operator fun invoke(itemId: Long, isChecked: Boolean) =
        repository.checkItem(itemId, isChecked)
}