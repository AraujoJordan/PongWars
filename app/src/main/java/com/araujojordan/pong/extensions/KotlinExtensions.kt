package com.araujojordan.pong.extensions

fun <T> tryOrNull(block: () -> T): T? = try {
    block()
} catch (err: Exception) {
    null
}