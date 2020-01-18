package com.kotlium

import org.slf4j.LoggerFactory
import java.sql.DriverManager
import java.sql.Statement
import java.util.*

typealias StatementBlock = Statement.() -> Unit

class DatabaseStage {

    private val logger = LoggerFactory.getLogger(DatabaseStage::class.java)

    private val statementActions = mutableListOf<StatementAction>()

    companion object {

        operator fun invoke(configure: DatabaseStage.() -> Unit): DatabaseStage {
            return DatabaseStage().apply(configure)
        }

    }

    fun statement(block: Statement.() -> Unit) {
        statementActions += StatementAction(block)
    }

    fun execute(url: String, user: String, password: String): DatabaseStageExecuteResult {
        val properties = Properties().apply {
            setProperty("user", user)
            setProperty("password", password)
        }

        val databaseActionExecuteResults = invokeStatementBlocks(url, properties)

        return DatabaseStageExecuteResult(databaseActionExecuteResults)
    }

    private fun invokeStatementBlocks(url: String, properties: Properties): MutableList<DatabaseActionExecuteResult> {
        val databaseActionExecuteResults = mutableListOf<DatabaseActionExecuteResult>()
        runCatching {
            DriverManager.getConnection(url, properties).use { connection ->
                connection.createStatement().use { statement ->
                    statementActions.map { statementAction ->
                        val executeResult = statementAction.execute(statement)
                        databaseActionExecuteResults.add(executeResult)
                    }
                }
            }
        }
        return databaseActionExecuteResults
    }

}

data class StatementAction(private val statementBlock: StatementBlock) {

    fun execute(statement: Statement): DatabaseActionExecuteResult {
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
