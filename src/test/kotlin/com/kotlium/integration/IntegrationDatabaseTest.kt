package com.kotlium.integration

import com.kotlium.DatabaseStage
import org.junit.jupiter.api.Test

internal class IntegrationDatabaseTest {

    @Test
    fun integrationTest() {
        // setup
        val jdbcDriver = "..."

        // execute
        val executeResult = DatabaseStage {
            assertTable {
            }
        }.execute(jdbcDriver)
    }

}