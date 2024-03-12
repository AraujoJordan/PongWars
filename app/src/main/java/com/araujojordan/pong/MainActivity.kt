package com.araujojordan.pong

import MainViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput


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
            Box(modifier = Modifier.fillMaxSize().pointerInput(Unit) {
                detectTapGestures(onTap = {
                    viewModel.onClick(it.x < displayMetrics.widthPixels /2)
                })
            }) {
                Arena(
                    slotSize = viewModel.slotSize,
                    slots = viewModel.slots.collectAsState(emptyList()),
                )
                Balls(
                    slotSize = viewModel.slotSize,
                    balls = viewModel.extraBallsPos.collectAsState(emptyList())
                )
            }
        }
    }
}

@Composable
private fun Arena(
    slotSize: Float,
    slots: State<List<Pair<Int, Int>>>,
) = Canvas(Modifier) {
    slots.value.forEach { (x, y) ->
        drawRect(
            color = Color.Black,
            topLeft = Offset(x * slotSize, y * slotSize),
            size = Size(slotSize, slotSize),
        )
    }
}

@Stable
@Composable
private fun Balls(
    slotSize: Float,
    balls: State<List<Pair<Offset, Boolean>>>
) = Canvas(modifier = Modifier.fillMaxSize()) {
    balls.value.forEach { (offset, team) ->
        drawCircle(
            color = if (team) Color.White else Color.Black,
            center = Offset(offset.x, offset.y),
            radius = slotSize,
        )
    }
}