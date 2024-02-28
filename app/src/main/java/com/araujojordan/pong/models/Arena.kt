package com.araujojordan.pong

import android.icu.text.Transliterator.Position
import com.araujojordan.pong.extensions.tryOrNull
import com.araujojordan.pong.extensions.x
import com.araujojordan.pong.extensions.y

data class Arena(
    val slots: Array<Array<Boolean>> = emptyArray(),
) {
    val IntArray.isOutsideArea: Boolean
        get() = try {
            slots[this@isOutsideArea.x][this@isOutsideArea.y]
            println("isOutsideArea()")
            false
        } catch (err:Exception) {
            true
        }

    fun slotPos(x: Int, y: Int) = tryOrNull { slots[x][y] }

    fun isEnemyHit(ball: Ball): Boolean = try {
        val x = ball.position.x
        val y = ball.position.y
        println("isEnemyHit()")
        flip(position = intArrayOf(ball.position.x, ball.position.y), ball = ball)
        when (ball.direction) {
            45 -> {
                if(slots[x+1][y] != ball.team) {
                    flip(position = intArrayOf(ball.position.x+1, ball.position.y), ball = ball)
                    true
                } else if (slots[x][y-1] != ball.team) {
                    flip(position = intArrayOf(ball.position.x, ball.position.y-1), ball = ball)
                    true
                } else {
                    false
                }
            }
            135 -> {
                if(slots[x+1][y] != ball.team) {
                    flip(position = intArrayOf(ball.position.x+1, ball.position.y), ball = ball)
                    true
                } else if (slots[x][y+1] != ball.team) {
                    flip(position = intArrayOf(ball.position.x, ball.position.y+1), ball = ball)
                    true
                } else {
                    false
                }
            }
            225 -> {
                if(slots[x-1][y] != ball.team) {
                    flip(position = intArrayOf(ball.position.x-1, ball.position.y), ball = ball)
                    true
                } else if (slots[x][y+1] != ball.team) {
                    flip(position = intArrayOf(ball.position.x, ball.position.y+1), ball = ball)
                    true
                } else {
                    false
                }
            }
            else -> {
                if(slots[x-1][y] != ball.team) {
                    flip(position = intArrayOf(ball.position.x-1, ball.position.y), ball = ball)
                    true
                } else if (slots[x][y-1] != ball.team) {
                    flip(position = intArrayOf(ball.position.x, ball.position.y-1), ball = ball)
                    true
                } else {
                    false
                }
            }
        }
    } catch (err:Exception) {
        false
    }

    fun flip(ball: Ball, position: IntArray) {
        slots[position.x][position.y] = ball.team
    }

    fun nextPosition(ball: Ball): Ball {
        val newPosition = ball.nextPosition()
        return when {
            newPosition.isOutsideArea -> {
                nextPosition(ball.onCollision())
            }
            isEnemyHit(ball) -> {
                nextPosition(ball.onCollision())
            }
            else -> ball.copy(position = newPosition)
        }
    }
}

