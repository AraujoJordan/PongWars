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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import com.araujojordan.pong.models.Arena


class MainActivity : ComponentActivity() {

    private val viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val displayMetrics = resources.displayMetrics
        viewModel.arenaWidth = (displayMetrics.widthPixels / viewModel.slotSize).toInt()
        viewModel.arenaHeight = (displayMetrics.heightPixels  / viewModel.slotSize).toInt()
        viewModel.generateArena()

        viewModel.startGameLoop()

        setContent {
            Box(modifier = Modifier.fillMaxSize()) {
                Arena(
                    slotSize = viewModel.slotSize,
                    arena = viewModel.arena.collectAsState(Arena()),
                )
                Balls(
                    slotSize = viewModel.slotSize,
                    trueBall = viewModel.ball1Position.collectAsState(initial = Offset(0f,0f)),
                    falseBall =  viewModel.ball2Position.collectAsState(initial = Offset(0f,0f)),
                )
            }
        }
    }
}

@Stable
@Composable
private fun Arena(
    slotSize: Float,
    arena: State<Arena>,
) = Canvas(Modifier) {
    arena.value.slots.forEachIndexed { x, rows ->
        rows.forEachIndexed { y, tile ->
            if (tile) {
                drawRect(
                    color = Color.Black,
                    topLeft = Offset(x * slotSize, y * slotSize),
                    size = Size(slotSize, slotSize),
                )
            }
        }
    }
}

@Stable
@Composable
private fun Balls(
    slotSize: Float,
    trueBall: State<Offset>,
    falseBall: State<Offset>,
) = Canvas(modifier = Modifier.fillMaxSize()) {
    drawCircle(
        color = Color.White,
        center = trueBall.value,
        radius = slotSize,
    )
    drawCircle(
        color = Color.Black,
        center = falseBall.value,
        radius = slotSize,
    )
}