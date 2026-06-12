package com.shoppinglist.app.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "shopping_items")
data class ShoppingItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val quantity: Int,
    val price: Double,
    val isChecked: Boolean = false,
    val createdAt: Date = Date(),
    val category: String = "Other",
    val currency: String = "RUB"
)