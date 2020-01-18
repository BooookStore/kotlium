package com.kotlium.database

class Row(val column1: Column, val column2: Column) {

    operator fun get(i: Int): Column {
        if (i == 1) {
            return column1
        } else {
            return column2
        }
    }

}
