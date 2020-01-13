package com.kotlium

import java.sql.DriverManager
import java.util.*

class DatabaseStage {

    companion object {

        operator fun invoke(configure: DatabaseStage.() -> Unit): DatabaseStage {
            return DatabaseStage().apply(configure)
        }

    }

    fun execute(url: String) {
        val properties = Properties().apply {
            setProperty("user", "root")
            setProperty("password", "password")
        }

        DriverManager.getConnection("jdbc:mysql://localhost:3306", properties).use {
            println("Connection Success!!!")
        }
    }

}
