package com.kotlium.integration

import com.kotlium.DatabaseStage
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@Suppress("SqlNoDataSourceInspection", "SqlResolve")
internal class IntegrationDatabaseTest {

    @Test
    fun integrationTest() {
        val executedResult = DatabaseStage {
            statement {
                val result = executeQuery("SELECT id FROM user WHERE id = 1")

                while (result.next()) {
                    assertThat(result.getInt("id")).isEqualTo(1)
                }
            }
        }.execute("jdbc:mysql://localhost:3306/kotlium", "kotlium", "password")

        assertThat(executedResult.isOk).isTrue()
        assertThat(executedResult.executedDatabaseActions).hasSize(1)
    }

    @Test
    fun integrationFailedTest() {
        val executedResult = DatabaseStage {
            statement {
                val result = executeQuery("SELECT id FROM user WHERE id = 2")

                while (result.next()) {
                    assertThat(result.getInt("id")).isEqualTo(1)
                }
            }
        }.execute("jdbc:mysql://localhost:3306/kotlium", "kotlium", "password")

        assertThat(executedResult.isOk).isFalse()
    }

}