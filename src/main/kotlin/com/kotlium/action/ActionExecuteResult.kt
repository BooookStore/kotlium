package com.kotlium.action

import kotlin.reflect.KClass

data class ActionExecuteResult(
    val actionClass: KClass<out Action>,
    val type: ActionType,
    val isOk: Boolean,
    val message: List<String> = listOf()
)

enum class ActionType { OPERATOR, ASSERT }
