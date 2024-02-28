package com.araujojordan.pong.extensions

fun tryOrNull(block: () -> Unit) {
    try {
        block()
    } catch (err: Exception) {
        null
    }
}