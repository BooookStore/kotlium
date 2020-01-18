package com.kotlium.database

import com.kotlium.StatementBlock
import java.sql.Statement

data class StatementAction(private val statementBlock: StatementBlock) : DatabaseAction {

    override fun execute(statement: Statement): DatabaseActionExecuteResult {
        return invokeStatementBlock(statementBlock, statement)
    }

    private fun invokeStatementBlock(
        statementBlock: StatementBlock,
        statement: Statement
    ): DatabaseActionExecuteResult {
        return runCatching {
            statementBlock.invoke(statement)
        }.fold(
            onSuccess = { DatabaseActionExecuteResult(true, listOf()) },
            onFailure = { DatabaseActionExecuteResult(false, listOf()) }
        )
    }

}