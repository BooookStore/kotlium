package com.kotlium

open class StageExecuteResult(val isOk: Boolean) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is StageExecuteResult) return false
        if (isOk != other.isOk) return false
        return true
    }

    override fun hashCode(): Int {
        return isOk.hashCode()
    }

}