package com.kotlium.database

data class DatabaseActionExecuteResult(
    val isOk: Boolean,
    val message: List<String>
)