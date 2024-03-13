import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.araujojordan.pong.models.Arena
import com.araujojordan.pong.models.Ball
import com.araujojordan.pong.extensions.tryOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

internal class MainViewModel : ViewModel() {

    private val arena = MutableStateFlow(Arena())
    var arenaWidth = 96
    var arenaHeight = 60
    val slotSize = 2f
    val gameLoopDuration = 16.6.milliseconds
    private val balls = MutableStateFlow<List<Ball>>(emptyList())
    val ballsPosition = balls.map {
        it.map {
            Offset(
                x = it.position.x* slotSize + (slotSize/2),
                y = it.position.y*slotSize + (slotSize/2)
            ) to it.team
        }.toTypedArray()
    }
    var udpates = 0
    val trueSlots = arena.combine(balls) { arena, balls ->
        buildList {
            arena.slots.forEachIndexed { x, rows ->
                rows.forEachIndexed { y, tile ->
                    if (tile) add(IntOffset(x, y))
                }
            }
        }.toTypedArray()
    }.distinctUntilChanged { old, new ->
        old.forEachIndexed { index, intOffset ->
            if (tryOrNull { new[index] } == null ) return@distinctUntilChanged false
            if (intOffset.x != new[index].x || intOffset.y != new[index].y ) {
                return@distinctUntilChanged false
            }
        }
        return@distinctUntilChanged true
    }

    init {
        generateArena()
    }

    fun onClick(team: Boolean) {
        viewModelScope.launch(Dispatchers.Default) {
            var offset = IntOffset(-1,-1)
            val arena = arena.first()
            while(offset == IntOffset(-1,-1)) {
                val x = (0..arenaWidth).random()
                val y = (0..arenaHeight).random()
                if (tryOrNull { arena.slots[x][y] } != null && arena.slots[x][y] == team) {
                    offset = IntOffset(x,y)
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
                    Ball(true, IntOffset(0,arenaHeight/2 - 1)),
                    Ball(false, IntOffset(arenaWidth-1,arenaHeight/2-1))
                )
            )
            arena.emit(Arena(finalList.toTypedArray()))
        }
    }

    suspend fun tick() {
        while (viewModelScope.isActive) {
            val updatedArena = arena.first()

            balls.update { it.map { updatedArena.nextState(it) } }
            arena.update { Arena(updatedArena.slots) }

            kotlinx.coroutines.delay(gameLoopDuration)
        }
    }

}