package com.araujojordan.pong.models

import androidx.compose.ui.unit.IntOffset
import com.araujojordan.pong.extensions.tryOrNull

data class Arena(val slots: Array<Array<Boolean>> = emptyArray()) {
    val IntOffset.isOutsideArea: Boolean get() = try {
        slots[this@isOutsideArea.x][this@isOutsideArea.y]
        false
    } catch (err:Exception) {
        true
    }

    fun isEnemyHit(previousPosition: IntOffset, nextPosition: IntOffset, forTeam: Boolean): Boolean {
        val dx = nextPosition.x - previousPosition.x
        val dy = nextPosition.y - previousPosition.y

        return when {
            tryOrNull { slots[previousPosition.x][previousPosition.y+dy] != forTeam } ?: false -> true
            tryOrNull { slots[previousPosition.x+dx][previousPosition.y] != forTeam } ?: false -> true
            tryOrNull { slots[nextPosition.x][nextPosition.y] != forTeam } ?: false -> true
            else -> false
        }
    }

    fun Ball.flipDirection(previousPosition: IntOffset, nextPosition: IntOffset) {
        val dx = nextPosition.x - previousPosition.x
        val dy = nextPosition.y - previousPosition.y

        when {
            IntOffset(previousPosition.x, previousPosition.y+dy).flip(this) -> true
            IntOffset(previousPosition.x+dx, previousPosition.y).flip(this) -> true
            nextPosition.flip(this) -> true
            else -> false
        }
    }

    fun IntOffset.flip(team: Ball) : Boolean = tryOrNull {
        if(slots[x][y] != team.team) {
            slots[x][y] = team.team
            return@tryOrNull true
        }
        return@tryOrNull false
    } ?: false

    fun nextState(ball: Ball): Ball {
        var updatedBall = ball.walk()
        var hitDirection: Ball? = null
        var attempts = 0
        while (updatedBall.position.isOutsideArea || isEnemyHit(
                previousPosition = ball.position,
                nextPosition = updatedBall.position,
                forTeam = ball.team
            )) {
            hitDirection = updatedBall
            updatedBall = ball.rotate().walk()
            attempts++
            if(attempts > 5) {
                updatedBall = ball.rotate()
                if(attempts > 10) {
                    updatedBall = ball.rotate().copy(
                        position = IntOffset(
                            ball.position.x + (-attempts - 9..attempts - 9).random(),
                            ball.position.y + (-attempts - 9..attempts - 9).random(),
                        )
                    )
                    hitDirection = null
                    if (attempts > 20) {
                        println("Ball ${ball.team} at ${ball.position} got stuck!")
                        return ball
                    }
                }
            }
        }
        hitDirection?.let { ball.flipDirection(ball.position, hitDirection.position)  }
        return updatedBall
    }
}

