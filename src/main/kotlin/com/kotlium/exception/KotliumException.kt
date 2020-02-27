package com.kotlium.exception

import com.kotlium.action.BrowserActionExecuteResult

open class KotliumException(message: String? = null) : Exception(message)

class BrowserStageException(message: String) : KotliumException(message) {

    companion object {

        fun from(cause: BrowserActionExecuteResult) = BrowserStageException("${cause.browserActionClass.simpleName} execute failed. ${cause.message[0]}")
        
    }

}
