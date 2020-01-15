package com.kotlium

import java.sql.DriverManager
import java.util.*

class DatabaseStage {

    companion object {

        operator fun invoke(configure: DatabaseStage.() -> Unit): DatabaseStage {
            return DatabaseStage().apply(configure)
        }

    }

    fun execute(url: String, user: String, password: String): Boolean {
        val properties = Properties().apply {
            setProperty("user", user)
            setProperty("password", password)
        }

        val result = runCatching {
            DriverManager.getConnection(url, properties).use {
                println("Connection Success!!!")
            }
        }

        return result.isSuccess
    }

}
