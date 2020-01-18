package com.kotlium.database

import org.assertj.core.api.Assertions.assertThat
import java.sql.Statement

class OneRowAssertion : DatabaseAction {

    lateinit var query: String

    lateinit var expected: Row

    override fun execute(statement: Statement): DatabaseActionExecuteResult {
        val resultSet = statement.executeQuery(query)

        if (!resultSet.next()) {
            return DatabaseActionExecuteResult(false, listOf("not one row. actual row count is ${resultSet.fetchSize}"))
        }

        return runCatching {
            expected.columns().forEach {
                assertThat(resultSet.getObject(it.name)).isEqualTo(it.value)
            }
        }.fold(
            onSuccess = { DatabaseActionExecuteResult(true, listOf()) },
            onFailure = { DatabaseActionExecuteResult(false, listOf()) }
        )
    }

}
