package com.kotlium.database

import java.sql.ResultSet
import java.sql.Statement

class OneRowAssertion : DatabaseAction {

    override fun execute(statement: Statement): DatabaseActionExecuteResult {
        return DatabaseActionExecuteResult(true, listOf())
    }

    fun expected(function: ResultSet.() -> Unit) {
    }

    lateinit var query: String

}
