package com.shoppinglist.app.feature.shoppinglist.domain.model

import java.util.Date
import java.util.Locale

data class ShoppingItem(
    val id: Long = 0,
    val name: String,
    val quantity: Int,
    val price: Double,
    val isChecked: Boolean = false,
    val createdAt: Date = Date(),
    val category: String = "Other",
    val currency: String = "RUB"
) {
    val total: Double = quantity * price

    fun getLocalizedCategory(): String {
        val isRussian = Locale.getDefault().language == "ru"
        return if (isRussian) {
            when (category) {
                "Food" -> "Продукты"
                "Household" -> "Бытовая химия"
                "Clothing" -> "Одежда"
                "Electronics" -> "Электроника"
                else -> "Другое"
            }
        } else {
            category
        }
    }
}