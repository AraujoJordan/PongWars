package com.araujojordan.pong

import com.araujojordan.pong.extensions.x
import com.araujojordan.pong.extensions.y
import kotlin.random.Random.Default.nextInt
import kotlin.random.nextInt


data class Ball(
    val team: Boolean,
    val position: IntArray = intArrayOf(5,5),
    val direction: Int = 45,
) {
    fun onCollision() = copy(
        direction = if (direction + 90 > 360)
            45
        else
            direction + 90
    )

    fun nextPosition() = when (direction) {
        45 -> intArrayOf(position.x + 1,position.y - 1)
        135 -> intArrayOf(position.x + 1,position.y + 1)
        225 -> intArrayOf(position.x - 1,position.y + 1)
        else -> intArrayOf(position.x - 1,position.y - 1)
    }
}