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
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.IntOffset
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.measureTime
import kotlin.time.toDuration


class MainActivity : ComponentActivity() {

    private val viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val displayMetrics = resources.displayMetrics
        viewModel.arenaWidth = (displayMetrics.widthPixels / viewModel.slotSize).toInt()
        viewModel.arenaHeight = (displayMetrics.heightPixels  / viewModel.slotSize).toInt()
        viewModel.generateArena()
        viewModel.gameLoopDuration = (1000f / (display?.refreshRate ?: 60f)).toInt().toDuration(DurationUnit.MILLISECONDS)

        viewModel.startGameLoop()

        setContent {
            Box(modifier = Modifier.fillMaxSize().pointerInput(Unit) {
                detectTapGestures(onTap = {
                    viewModel.onClick(it.x < displayMetrics.widthPixels / 2)
                })
            }) {
                Arena()
                Balls()
            }
        }
    }

    @Stable
    @Composable
    private fun Balls(
        slotSize: Float = viewModel.slotSize,
        balls: Array<Pair<Offset, Boolean>> =  viewModel.ballsPosition.collectAsState(emptyArray()).value
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            balls.forEach { (offset, team) ->
                drawCircle(
                    color = if (team) Color.White else Color.Black,
                    center = Offset(offset.x, offset.y),
                    radius = slotSize,
                )
            }
        }
    }

    @Stable
    @Composable
    private fun Arena(
        slotSize: Float= viewModel.slotSize,
        slots: Array<IntOffset> = viewModel.trueSlots.collectAsState(emptyArray()).value,
    ) = Canvas(Modifier.fillMaxSize()) {
        slots.forEach { (x, y) ->
            drawRect(
                color = Color.Black,
                topLeft = Offset(x * slotSize, y * slotSize),
                size = Size(slotSize, slotSize),
            )
        }
    }
}



