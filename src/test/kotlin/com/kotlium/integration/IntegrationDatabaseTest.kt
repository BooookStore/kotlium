package com.kotlium.integration

import com.kotlium.DatabaseStage
import com.kotlium.database.Column
import com.kotlium.database.DatabaseActionExecuteResult
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@Suppress("SqlNoDataSourceInspection", "SqlResolve")
internal class IntegrationDatabaseTest {

    private val url = "jdbc:mysql://localhost:3306/kotlium"

    private val user = "kotlium"

    private val password = "password"

    @Test
    fun columnTest() {
        Column("id", 1).let { column ->
            assertThat(column)
                .hasFieldOrPropertyWithValue("name", "id")
                .hasFieldOrPropertyWithValue("value", 1)
        }
        Column("name", "bookstore").let { column ->
            assertThat(column)
                .hasFieldOrPropertyWithValue("name", "name")
                .hasFieldOrPropertyWithValue("value", "bookstore")
        }
    }

    @Test
    fun integrationTest() {
        val executedResult = DatabaseStage {
            statement {
                val result = executeQuery("SELECT id FROM user WHERE id = 1")

                while (result.next()) {
                    val column = Column("id", 1)
                    assertThat(result.getObject(column.name)).isEqualTo(column.value)
                }
            }
            statement {
                val result = executeQuery("SELECT id FROM user WHERE id = 2")

                while (result.next()) {
                    val column = Column("id", 3)
                    assertThat(result.getObject(column.name)).isEqualTo(column.value)
                }
            }
            statement {
                val result = executeQuery("SELECT id FROM user WHERE id = 3")

                while (result.next()) {
                    val column = Column("id", 3)
                    assertThat(result.getObject(column.name)).isEqualTo(column.value)
                }
            }
        }.execute(url, user, password)

        assertThat(executedResult.isOk).isFalse()
        assertThat(executedResult.executedDatabaseActions).hasSize(2).containsExactly(
            DatabaseActionExecuteResult(isOk = true, message = listOf()),
            DatabaseActionExecuteResult(isOk = false, message = listOf())
        )
    }

    @Test
    fun assertOneRowTest() {
        val executedResult = DatabaseStage {
            assertOneRow {
                query = "SELECT id FROM user WHERE id = 1"
                expected {
                    val column = Column("id", 1)
                    assertThat(getObject(column.name)).isEqualTo(column.value)
                }
            }
        }.execute(url, user, password)

        assertThat(executedResult.isOk).isTrue()
        assertThat(executedResult.executedDatabaseActions).hasSize(1).containsExactly(
            DatabaseActionExecuteResult(true, listOf())
        )
    }

}