package com.kotlium.integration

import com.kotlium.DatabaseStage
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@Suppress("SqlNoDataSourceInspection", "SqlResolve")
internal class IntegrationDatabaseTest {

    @Test
    fun integrationTest() {
        val isSuccess = DatabaseStage {
            statement {
                val result = executeQuery("SELECT id FROM user WHERE id = 1")

                while (result.next()) {
                    assertThat(result.getInt("id")).isEqualTo(1)
                }
            }
        }.execute("jdbc:mysql://localhost:3306/kotlium", "kotlium", "password")

        assertThat(isSuccess).isTrue()
    }

}