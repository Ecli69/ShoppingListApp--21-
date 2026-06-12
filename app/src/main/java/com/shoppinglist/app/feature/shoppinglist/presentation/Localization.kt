package com.shoppinglist.app.feature.shoppinglist.presentation

import androidx.compose.runtime.Composable
import java.util.Locale

data class AppStrings(
    val appName: String,
    val shoppingList: String,
    val settings: String,
    val theme: String,
    val language: String,
    val light: String,
    val dark: String,
    val system: String,
    val english: String,
    val russian: String,
    val addItem: String,
    val name: String,
    val quantity: String,
    val price: String,
    val category: String,
    val add: String,
    val cancel: String,
    val delete: String,
    val deleteCheckedItems: String,
    val emptyList: String,
    val tapToAdd: String,
    val startShopping: String,
    val shoppingMode: String,
    val currentItem: String,
    val checkOff: String,
    val pcs: String,
    val categories: List<String>,
    val settingsRestartMessage: String,
    val total: String,
    val currencyRub: String,
    val currencyUsd: String,
    val selectCurrency: String
)

private val ruStrings = AppStrings(
    appName = "Список покупок",
    shoppingList = "Список покупок",
    settings = "Настройки",
    theme = "Тема",
    language = "Язык",
    light = "Светлая",
    dark = "Тёмная",
    system = "Системная",
    english = "Английский",
    russian = "Русский",
    addItem = "Добавить товар",
    name = "Название",
    quantity = "Количество",
    price = "Цена",
    category = "Категория",
    add = "Добавить",
    cancel = "Отмена",
    delete = "Удалить",
    deleteCheckedItems = "Удалить отмеченные",
    emptyList = "Ваш список покупок пуст",
    tapToAdd = "Нажмите + чтобы добавить товары",
    startShopping = "За покупками",
    shoppingMode = "Режим покупок",
    currentItem = "Текущий товар",
    checkOff = "Вычеркнуть",
    pcs = "шт",
    categories = listOf("Продукты", "Бытовая химия", "Одежда", "Электроника", "Другое"),
    settingsRestartMessage = "Приложение перезапустится для применения языка",
    total = "Итого",
    currencyRub = "Рубль (₽)",
    currencyUsd = "Доллар ($)",
    selectCurrency = "Валюта"
)

private val enStrings = AppStrings(
    appName = "Shopping List",
    shoppingList = "Shopping List",
    settings = "Settings",
    theme = "Theme",
    language = "Language",
    light = "Light",
    dark = "Dark",
    system = "System",
    english = "English",
    russian = "Russian",
    addItem = "Add Item",
    name = "Name",
    quantity = "Quantity",
    price = "Price",
    category = "Category",
    add = "Add",
    cancel = "Cancel",
    delete = "Delete",
    deleteCheckedItems = "Delete checked items",
    emptyList = "Your shopping list is empty",
    tapToAdd = "Tap + to add items",
    startShopping = "Start shopping",
    shoppingMode = "Shopping Mode",
    currentItem = "Current item",
    checkOff = "Check off",
    pcs = "pcs",
    categories = listOf("Food", "Household", "Clothing", "Electronics", "Other"),
    settingsRestartMessage = "App will restart to apply language",
    total = "Total",
    currencyRub = "Ruble (₽)",
    currencyUsd = "Dollar ($)",
    selectCurrency = "Currency"
)

@Composable
fun getAppStrings(): AppStrings {
    val isRussian = Locale.getDefault().language == "ru"
    return if (isRussian) ruStrings else enStrings
}

fun translateCategory(category: String): String {
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