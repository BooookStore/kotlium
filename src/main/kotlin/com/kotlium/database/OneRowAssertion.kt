package com.kotlium.database

import java.sql.ResultSet

class OneRowAssertion {
    fun expected(function: ResultSet.() -> Unit) {
    }

    lateinit var query: String
}
