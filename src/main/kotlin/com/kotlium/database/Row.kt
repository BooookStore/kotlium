package com.kotlium.database

class Row(private vararg val column: Column) {

    operator fun get(i: Int): Column = column[i]

}
