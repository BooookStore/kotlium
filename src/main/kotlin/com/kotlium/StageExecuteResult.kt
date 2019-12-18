package com.kotlium

import kotlin.reflect.KClass

data class StageExecuteResult(val executedActions: List<ActionExecuteResult>) {
    val isOk: Boolean
        get() = executedActions.all { it.isOk }
}

data class ActionExecuteResult(
    val actionClass: KClass<out Action>,
    val isOk: Boolean,
    val message: String?
)
