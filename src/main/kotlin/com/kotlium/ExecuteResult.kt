package com.kotlium

import com.kotlium.action.Action
import com.kotlium.action.ActionType
import kotlin.reflect.KClass

data class StageExecuteResult(val executedActions: List<ActionExecuteResult>) {
    val isOk: Boolean
        get() = executedActions.all { it.isOk }
}

data class ActionExecuteResult(
    val actionClass: KClass<out Action>,
    val isOk: Boolean,
    val type: ActionType,
    val message: String?
)
