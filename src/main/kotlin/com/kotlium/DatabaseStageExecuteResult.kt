package com.kotlium

data class DatabaseStageExecuteResult(val executedDatabaseActions: List<DatabaseActionExecuteResult>) {

    val isOk: Boolean
        get() = executedDatabaseActions.all { it.isOk }

}