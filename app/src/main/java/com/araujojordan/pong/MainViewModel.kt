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

    val slotSize = 20f
    val gameLoopDuration = 50.milliseconds
    private val ballTrue = MutableStateFlow(Ball(true, intArrayOf(0,4)))
    private val ballFalse = MutableStateFlow(Ball(false, intArrayOf(9,4)))
    val arena = MutableStateFlow(
        Arena(arrayOf(
        arrayOf(true, true, true, true, true, true, true, true, true, true),
        arrayOf(true, true, true, true, true, true, true, true, true, true),
        arrayOf(true, true, true, true, true, true, true, true, true, true),
        arrayOf(true, true, true, true, true, true, true, true, true, true),
        arrayOf(true, true, true, true, true, true, true, true, true, true),
        arrayOf(false, false, false, false, false, false, false, false, false, false),
        arrayOf(false, false, false, false, false, false, false, false, false, false),
        arrayOf(false, false, false, false, false, false, false, false, false, false),
        arrayOf(false, false, false, false, false, false, false, false, false, false),
        arrayOf(false, false, false, false, false, false, false, false, false, false),
    ))
    )
    val ball1Position = ballTrue.map { Offset(
        x = it.position.x* slotSize + (slotSize/2),
        y = it.position.y*slotSize + (slotSize/2)
    ) }
    val ball2Position = ballFalse.map { Offset(
        x = it.position.x* slotSize + (slotSize/2),
        y = it.position.y*slotSize + (slotSize/2)
    ) }

    init { viewModelScope.launch(Dispatchers.Default) { gameLoop() } }

    suspend fun gameLoop() {
        while (true) {
            println("gameLoop()")
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