package com.kotlium.integration

import org.junit.jupiter.api.Test
import java.sql.DriverManager
import java.util.*

internal class IntegrationDatabaseTest {

    @Test
    fun integrationTest() {
        val properties = Properties().apply {
            setProperty("user", "root")
            setProperty("password", "password")
        }

        DriverManager.getConnection("jdbc:mysql://localhost:3306", properties).use {
            println("Connection Success!!!")
        }
    }

}