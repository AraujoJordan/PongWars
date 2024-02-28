package com.araujojordan.pong


data class Ball(
    val team: Boolean,
    val position: IntArray = intArrayOf(5,5),
    val direction: Int = 45,
) {
    fun onCollision()= copy(direction = this@Ball.direction + 45)
}