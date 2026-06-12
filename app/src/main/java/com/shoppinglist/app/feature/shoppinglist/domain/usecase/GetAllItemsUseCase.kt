package com.shoppinglist.app.feature.shoppinglist.domain.usecase

import com.shoppinglist.app.feature.shoppinglist.domain.model.ShoppingItem
import com.shoppinglist.app.feature.shoppinglist.domain.repository.ShoppingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllItemsUseCase @Inject constructor(
    private val repository: ShoppingRepository
) {
    operator fun invoke(): Flow<List<ShoppingItem>> = repository.getAllItems()
}