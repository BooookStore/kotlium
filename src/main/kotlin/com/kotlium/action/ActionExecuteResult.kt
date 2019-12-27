package com.kotlium.action

import kotlin.reflect.KClass

data class ActionExecuteResult(
    val actionClass: KClass<out Action>,
    val isOk: Boolean,
    val type: ActionType,
    val message: List<String> = listOf()
)