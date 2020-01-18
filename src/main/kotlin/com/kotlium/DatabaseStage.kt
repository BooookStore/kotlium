package com.kotlium

import org.slf4j.LoggerFactory
import java.sql.DriverManager
import java.sql.Statement
import java.util.*

typealias StatementBlock = Statement.() -> Unit

class DatabaseStage {

    private val logger = LoggerFactory.getLogger(DatabaseStage::class.java)

    private val statementBlocks = mutableListOf<StatementBlock>()

    companion object {

        operator fun invoke(configure: DatabaseStage.() -> Unit): DatabaseStage {
            return DatabaseStage().apply(configure)
        }

    }

    fun statement(block: Statement.() -> Unit) {
        statementBlocks += block
    }

    fun execute(url: String, user: String, password: String): DatabaseStageExecuteResult {
        val properties = Properties().apply {
            setProperty("user", user)
            setProperty("password", password)
        }

        val databaseActionExecuteResults = mutableListOf<DatabaseActionExecuteResult>()
        val result = runCatching {
            DriverManager.getConnection(url, properties).use { connection ->
                val statement = connection.createStatement()
                statementBlocks.map { statementBlock ->
                    val executeResult = runCatching {
                        statementBlock.invoke(statement)
                    }.fold(
                        onSuccess = { DatabaseActionExecuteResult(true, listOf()) },
                        onFailure = { DatabaseActionExecuteResult(false, listOf()) }
                    )
                    databaseActionExecuteResults.add(executeResult)
                }
                statement.close()
            }
        }

        return DatabaseStageExecuteResult(databaseActionExecuteResults)
    }

}
