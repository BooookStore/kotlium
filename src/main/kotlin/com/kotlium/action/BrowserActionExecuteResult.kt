package com.kotlium.action

import kotlin.reflect.KClass

data class BrowserActionExecuteResult(
    val browserActionClass: KClass<out BrowserAction>,
    val type: BrowserActionType,
    val isOk: Boolean,
    val message: List<String> = listOf()
)

enum class BrowserActionType { OPERATOR, ASSERT }
