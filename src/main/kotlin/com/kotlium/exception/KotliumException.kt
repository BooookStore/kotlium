package com.kotlium.exception

import com.kotlium.action.BrowserActionExecuteResult

open class KotliumException(message: String? = null, cause: Throwable? = null) : Exception(message, cause)

class BrowserStageException(message: String) : KotliumException(message) {

    companion object {

        fun from(cause: BrowserActionExecuteResult) = BrowserStageException("${cause.browserActionClass.simpleName} execute failed. ${cause.message[0]}")
        
    }

}

class ScenarioException(message: String, cause: BrowserStageException) : KotliumException(message, cause)