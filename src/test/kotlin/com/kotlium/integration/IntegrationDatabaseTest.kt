package com.kotlium.integration

import com.kotlium.DatabaseStage
import org.junit.jupiter.api.Test

internal class IntegrationDatabaseTest {

    @Test
    fun integrationTest() {
        val executeResult = DatabaseStage {

        }.execute("jdbc:mysql://localhost:3306")
    }

}