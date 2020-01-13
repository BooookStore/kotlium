package com.kotlium.integration

import com.kotlium.DatabaseStage
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class IntegrationDatabaseTest {

    @Test
    fun integrationTest() {
        val isSuccess = DatabaseStage {

        }.execute("jdbc:mysql://localhost:3306")

        assertThat(isSuccess).isTrue()
    }

}