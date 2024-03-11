import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.araujojordan.pong.models.Arena
import com.araujojordan.pong.Ball
import com.araujojordan.pong.extensions.x
import com.araujojordan.pong.extensions.y
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

internal class MainViewModel : ViewModel() {

    val arena = MutableStateFlow(Arena())

    var arenaWidth = 96
    var arenaHeight = 60
    val slotSize = 20f
    val gameLoopDuration = 25.milliseconds
    private val ballTrue = MutableStateFlow(Ball(true, intArrayOf(0,arenaHeight/2 - 1)))
    private val ballFalse = MutableStateFlow(Ball(false, intArrayOf(arenaWidth-1,arenaHeight/2-1)))
    val ball1Position = ballTrue.map { Offset(
        x = it.position.x* slotSize + (slotSize/2),
        y = it.position.y*slotSize + (slotSize/2)
    ) }
    val ball2Position = ballFalse.map { Offset(
        x = it.position.x* slotSize + (slotSize/2),
        y = it.position.y*slotSize + (slotSize/2)
    ) }

    init {
        generateArena()
        viewModelScope.launch(Dispatchers.Default) { gameLoop() }
    }

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
        viewModelScope.launch(Dispatchers.Default) { arena.emit(Arena(finalList.toTypedArray())) }
    }


    suspend fun gameLoop() {
        while (true) {
            var updatedBallTrue = ballTrue.first()
            var updatedBallFalse = ballFalse.first()
            val updatedArena = arena.first()

            updatedBallTrue = updatedArena.nextState(updatedBallTrue)
            updatedBallFalse = updatedArena.nextState(updatedBallFalse)

            ballTrue.update { updatedBallTrue }
            ballFalse.update { updatedBallFalse }
            arena.update { updatedArena }

            kotlinx.coroutines.delay(gameLoopDuration)
        }
    }

}