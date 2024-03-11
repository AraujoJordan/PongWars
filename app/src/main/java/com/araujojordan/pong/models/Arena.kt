package com.araujojordan.pong.models

import com.araujojordan.pong.Ball
import com.araujojordan.pong.extensions.tryOrNull
import com.araujojordan.pong.extensions.x
import com.araujojordan.pong.extensions.y

data class Arena(
    val slots: Array<Array<Boolean>> = emptyArray(),
) {
    val IntArray.isOutsideArea: Boolean get() = try {
        slots[this@isOutsideArea.x][this@isOutsideArea.y]
        false
    } catch (err:Exception) {
        true
    }

    fun isEnemyHit(previousPosition: IntArray, nextPosition: IntArray, forTeam: Boolean): Boolean {
        val dx = nextPosition.x - previousPosition.x
        val dy = nextPosition.y - previousPosition.y

        return when {
            tryOrNull { slots[previousPosition.x][previousPosition.y+dy] != forTeam } ?: false -> true
            tryOrNull { slots[previousPosition.x+dx][previousPosition.y] != forTeam } ?: false -> true
            tryOrNull { slots[nextPosition.x][nextPosition.y] != forTeam } ?: false -> true
            else -> false
        }
    }

    fun Ball.flipDirection(previousPosition: IntArray, nextPosition: IntArray) {
        val dx = nextPosition.x - previousPosition.x
        val dy = nextPosition.y - previousPosition.y

        when {
            intArrayOf(previousPosition.x, previousPosition.y+dy).flip(this) -> true
            intArrayOf(previousPosition.x+dx, previousPosition.y).flip(this) -> true
            nextPosition.flip(this) -> true
            else -> false
        }
    }

    fun IntArray.flip(team: Ball) : Boolean = tryOrNull {
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
            if(attempts > 5) return nextState(ball.rotate())
        }
        hitDirection?.let { ball.flipDirection(ball.position, hitDirection.position)  }
        return updatedBall
    }

}

