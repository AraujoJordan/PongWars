package com.araujojordan.pong.models

import androidx.compose.ui.unit.IntOffset
import kotlin.random.Random


data class Ball(
    val team: Boolean,
    val position: IntOffset = IntOffset(5,5),
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
            45 -> IntOffset(position.x + 1,position.y - 1)
            135 -> IntOffset(position.x + 1,position.y + 1)
            225 -> IntOffset(position.x - 1,position.y + 1)
            else -> IntOffset(position.x - 1,position.y - 1)
        }
    )
}