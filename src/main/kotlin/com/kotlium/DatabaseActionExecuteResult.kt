package com.kotlium

data class DatabaseActionExecuteResult(
    val isOk: Boolean,
    val message: List<String>
)