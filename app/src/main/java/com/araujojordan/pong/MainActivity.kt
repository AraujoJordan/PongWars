package com.araujojordan.pong

import MainViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import com.araujojordan.pong.models.Arena

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel = MainViewModel()
        setContent {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Arena(
                    slotSize = viewModel.slotSize,
                    trueBall = animateOffsetAsState(
                        viewModel.ball1Position.collectAsState(initial = Offset(0f,0f)).value,
                        animationSpec = tween(
                            durationMillis = viewModel.gameLoopDuration.inWholeMilliseconds.toInt(),
                            easing = LinearEasing,
                        ),
                    ).value,
                    falseBall =  animateOffsetAsState(
                        viewModel.ball2Position.collectAsState(initial = Offset(0f,0f)).value,
                        animationSpec = tween(
                            durationMillis = viewModel.gameLoopDuration.inWholeMilliseconds.toInt(),
                            easing = LinearEasing,
                        ),
                    ).value,
                    arena = viewModel.arena.collectAsState(Arena()).value,
                )
            }
        }
    }
}

@Composable
private fun Arena(
    slotSize: Float,
    trueBall: Offset,
    falseBall: Offset,
    arena: Arena,
) = Canvas(modifier = Modifier) {
    arena.slots.forEachIndexed { x, rows ->
        rows.forEachIndexed { y, tile ->
            drawRect(
                color = if (tile) Color.Black else Color.White,
                topLeft = Offset(x * slotSize, y * slotSize),
                size = Size(slotSize, slotSize),
            )
        }
    }
    drawCircle(
        color = Color.White,
        center = trueBall,
        radius = slotSize,
    )
    drawCircle(
        color = Color.Black,
        center = falseBall,
        radius = slotSize,
    )
}