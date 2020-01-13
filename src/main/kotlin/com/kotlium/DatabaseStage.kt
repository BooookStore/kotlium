package com.kotlium

class DatabaseStage {

    companion object {

        operator fun invoke(configure: DatabaseStage.() -> Unit): DatabaseStage {
            return DatabaseStage().apply(configure)
        }

    }

    fun assertTable(tableAssert: () -> Unit) {

    }

    fun execute(databaseUrl: String): Unit = Unit

}
