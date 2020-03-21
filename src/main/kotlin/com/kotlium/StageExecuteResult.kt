package com.kotlium

abstract class StageExecuteResult() {

    abstract fun throwIfFailed()

    abstract fun isOk(): Boolean

}