package com.kotlium

import kotlin.reflect.KClass

data class StageExecuteResult(
    val isOk: Boolean,
    val executedActions: List<ActionExecuteResult>
)

data class ActionExecuteResult(
    val actionClass: KClass<out Action>,
    val isOk: Boolean,
    val message: String?
)
