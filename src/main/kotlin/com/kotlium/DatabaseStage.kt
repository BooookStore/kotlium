package com.kotlium

import java.sql.DriverManager
import java.util.*

class DatabaseStage {

    companion object {

        operator fun invoke(configure: DatabaseStage.() -> Unit): DatabaseStage {
            return DatabaseStage().apply(configure)
        }

    }

    fun execute(url: String): Boolean {
        val properties = Properties().apply {
            setProperty("user", "root")
            setProperty("password", "password")
        }

        val result = runCatching {
            DriverManager.getConnection(url, properties).use {
                println("Connection Success!!!")
            }
        }

        return result.isSuccess
    }

}
