package com.kotlium

data class DatabaseStageExecuteResult(
    val isOk: Boolean,
    val executedDatabaseActions: List<Any>
)