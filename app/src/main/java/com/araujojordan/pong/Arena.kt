package com.araujojordan.pong

import com.araujojordan.pong.extensions.x
import com.araujojordan.pong.extensions.y

data class Arena(
    val slots: Array<Array<Boolean>>,
) {
    val IntArray.isOutsideArea: Boolean
        get() = try {
            slots[this@isOutsideArea.x][this@isOutsideArea.y]
            true
        } catch (err:Exception) {
            false
        }

    fun isEnemyHit(ball: Ball, position: IntArray): Boolean = try {
        slots[ball.position.x][ball.position.y] != ball.team
    } catch (err:Exception) {
        false
    }

    fun flip(position: IntArray) = copy(
        slots = slots.apply {
            slots[position.x][position.y] = !slots[position.x][position.y]
        }
    )
}

