import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.araujojordan.pong.Arena
import com.araujojordan.pong.Ball
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

internal class MainViewModel : ViewModel() {

    val gameLoopDuration = 250.milliseconds
    val ballTrue = MutableStateFlow(Ball(true, intArrayOf(0,4)))
    val ballFalse = MutableStateFlow(Ball(false, intArrayOf(9,4)))
    val arena = MutableStateFlow(Arena(arrayOf(
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
    )))

    init { viewModelScope.launch(Dispatchers.Default) { gameLoop() } }

    suspend fun gameLoop() {
        while (true) {
            println("gameLoop()")
            var updatedBallTrue = ballTrue.first()
            var updatedBallFalse = ballFalse.first()
            val updatedArena = arena.first()

            updatedBallTrue = updatedArena.nextPosition(updatedBallTrue)
            updatedBallFalse = updatedArena.nextPosition(updatedBallFalse)

            ballTrue.update { updatedBallTrue }
            ballFalse.update { updatedBallFalse }
            arena.update { updatedArena }

            kotlinx.coroutines.delay(gameLoopDuration)
        }
    }

}