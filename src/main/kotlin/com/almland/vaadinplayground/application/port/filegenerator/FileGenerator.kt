package com.almland.vaadinplayground.application.port.filegenerator

internal interface FileGenerator<T, R> {
    fun createFile(t: T): R
}
