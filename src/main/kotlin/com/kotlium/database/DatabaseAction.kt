package com.kotlium.database

import java.sql.Statement

interface DatabaseAction {

    fun execute(statement: Statement): DatabaseActionExecuteResult

}