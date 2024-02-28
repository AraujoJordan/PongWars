package com.araujojordan.pong

import MainViewModel
import android.graphics.drawable.shapes.Shape
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.araujojordan.pong.ui.theme.TestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel = MainViewModel()
        setContent {
            Arena(
                trueBall = viewModel.ballTrue.collectAsState(Ball(true)).value,
                falseBall = viewModel.ballFalse.collectAsState(Ball(false)).value,
                arena = viewModel.arena.collectAsState(Arena()).value,
            )
        }
    }
}

val size = 15.dp

@Composable
private fun Arena(
    trueBall: Ball,
    falseBall: Ball,
    arena: Arena
) = Box(
    modifier = Modifier.fillMaxSize().background(Color.LightGray),
    contentAlignment = Alignment.Center,
) {
    Row {
        arena.slots.forEachIndexed { x, columns ->
            Column {
                columns.forEachIndexed { y, tile ->
                    Box(
                        Modifier
                            .requiredSize(size)
                            .background(if (tile) Color.White else Color.Black)
                    ) {
                        if (trueBall.position.contentEquals(intArrayOf(x, y))) {
                            Ball(trueBall)
                        }
                        if (falseBall.position.contentEquals(intArrayOf(x, y))) {
                            Ball(falseBall)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Ball(ball: Ball) = Box(
    modifier = Modifier.clip(CircleShape)
        .requiredSize(size)
        .background(if (ball.team) Color.Red else Color.Blue)

)

@Composable
@Preview
private fun Preview() {
    Arena()
}