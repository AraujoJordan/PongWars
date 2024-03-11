package com.araujojordan.pong

import com.araujojordan.pong.extensions.x
import com.araujojordan.pong.extensions.y
import kotlin.random.Random


data class Ball(
    val team: Boolean,
    val position: IntArray = intArrayOf(5,5),
    val direction: Int = if (team) 45 else 135,
) {
    fun rotate(): Ball {
        val newDirection = if (Random.nextBoolean()) {
            direction + 90
        } else {
            direction - 90
        }
        return copy(
            direction = if (newDirection > 360) {
                45
            } else if (newDirection < 0) {
                315
            } else {
                newDirection
            }
        )
    }

    fun walk() = copy(position = when (direction) {
            45 -> intArrayOf(position.x + 1,position.y - 1)
            135 -> intArrayOf(position.x + 1,position.y + 1)
            225 -> intArrayOf(position.x - 1,position.y + 1)
            else -> intArrayOf(position.x - 1,position.y - 1)
        }
    )
}