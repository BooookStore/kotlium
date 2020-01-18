package com.kotlium

import com.kotlium.database.DatabaseActionExecuteResult

data class DatabaseStageExecuteResult(val executedDatabaseActions: List<DatabaseActionExecuteResult>) {

    val isOk: Boolean
        get() = executedDatabaseActions.all { it.isOk }

}