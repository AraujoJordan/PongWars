import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.araujojordan.pong.models.Arena
import com.araujojordan.pong.Ball
import com.araujojordan.pong.extensions.tryOrNull
import com.araujojordan.pong.extensions.x
import com.araujojordan.pong.extensions.y
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

internal class MainViewModel : ViewModel() {

    private val arena = MutableStateFlow(Arena())
    val slots: MutableStateFlow<List<Pair<Int, Int>>> = MutableStateFlow(emptyList())

    var arenaWidth = 96
    var arenaHeight = 60
    val slotSize = 22f
    val gameLoopDuration = 16.6.milliseconds
    val balls = MutableStateFlow<List<Ball>>(emptyList())
    val extraBallsPos = balls.map {
        it.map {
            Offset(
                x = it.position.x* slotSize + (slotSize/2),
                y = it.position.y*slotSize + (slotSize/2)
            ) to it.team
        }
    }

    init {
        generateArena()
    }

    fun onClick(team: Boolean) {
        viewModelScope.launch(Dispatchers.Default) {
            var offset = intArrayOf(-1,-1)
            val arena = arena.first()
            while(offset.contentEquals(intArrayOf(-1,-1))) {
                val x = (0..arenaWidth).random()
                val y = (0..arenaHeight).random()
                if (tryOrNull { arena.slots[x][y] } != null && arena.slots[x][y] == team) {
                    offset = intArrayOf(x,y)
                }
            }
            balls.update {
                it + Ball(
                    team = team,
                    position = offset
                )
            }
        }
    }

    fun startGameLoop() = viewModelScope.launch(Dispatchers.Default) { tick() }

    fun generateArena() {
        val trueRows = mutableListOf<Boolean>()
        val falseRows = mutableListOf<Boolean>()
        repeat(arenaHeight) {
            trueRows.add(true)
            falseRows.add(false)
        }
        val finalList = mutableListOf<Array<Boolean>>()
        repeat(arenaWidth/2) {
            finalList.add(trueRows.toTypedArray())
        }
        repeat(arenaWidth/2) {
            finalList.add(falseRows.toTypedArray())
        }
        viewModelScope.launch(Dispatchers.Default) {
            balls.emit(
                listOf(
                    Ball(true, intArrayOf(0,arenaHeight/2 - 1)),
                    Ball(false, intArrayOf(arenaWidth-1,arenaHeight/2-1))
                )
            )
            arena.emit(Arena(finalList.toTypedArray()))
        }
    }

    suspend fun tick() {
        while (viewModelScope.isActive) {
            val updatedArena = arena.first()

            balls.update {
                it.map {
                    updatedArena.nextState(it)
                }
            }

            arena.update {
                updatedArena
            }
            slots.update {
                mutableListOf<Pair<Int, Int>>().apply {
                    updatedArena.slots.forEachIndexed { x, rows ->
                        rows.forEachIndexed { y, tile ->
                            if (tile)
                                add(x to y)
                        }
                    }
                }
            }
            kotlinx.coroutines.delay(gameLoopDuration)
        }
    }

}