package com.kotlium.database

import java.sql.ResultSet
import java.sql.Statement

class OneRowAssertion : DatabaseAction {

    lateinit var query: String

    lateinit var assertionBlock: ResultSet.() -> Unit

    override fun execute(statement: Statement): DatabaseActionExecuteResult {
        val resultSet = statement.executeQuery(query)

        if (!resultSet.next()) {
            return DatabaseActionExecuteResult(false, listOf("not one row. actual row count is ${resultSet.fetchSize}"))
        }

        return runCatching {
            assertionBlock.invoke(resultSet)
        }.fold(
            onSuccess = { DatabaseActionExecuteResult(true, listOf()) },
            onFailure = { DatabaseActionExecuteResult(false, listOf()) }
        )
    }

    fun expected(block: ResultSet.() -> Unit) {
        assertionBlock = block
    }

}
