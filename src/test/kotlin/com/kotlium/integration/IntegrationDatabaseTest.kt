package com.kotlium.integration

import com.kotlium.DatabaseStage
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class IntegrationDatabaseTest {

    @Test
    fun integrationTest() {
        var sumOfId = 0;

        val isSuccess = DatabaseStage {
            statement {
                val result = executeQuery("SELECT id FROM user")

                while (result.next()) {
                    sumOfId += result.getInt("id")
                }
            }
        }.execute("jdbc:mysql://localhost:3306/kotlium", "kotlium", "password")

        assertThat(isSuccess).isTrue()
        assertThat(sumOfId).isEqualTo(6)
    }

}