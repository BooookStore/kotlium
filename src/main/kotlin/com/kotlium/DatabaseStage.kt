package com.kotlium

class DatabaseStage {

    companion object {

        operator fun invoke(configure: DatabaseStage.() -> Unit): DatabaseStage {
            return DatabaseStage().apply(configure)
        }

    }

    fun execute(url: String) {

    }

}
