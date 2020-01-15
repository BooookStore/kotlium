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

    fun execute(url: String, user: String, password: String): Boolean {
        val properties = Properties().apply {
            setProperty("user", user)
            setProperty("password", password)
        }

        val result = runCatching {
            DriverManager.getConnection(url, properties).use { connection ->
                val statement = connection.createStatement()
                statementBlocks.forEach { it.invoke(statement) }
                statement.close()
            }
        }

        result.exceptionOrNull()?.let {
            logger.error("failed DatabaseStage", it)
        }

        return result.isSuccess
    }

}
