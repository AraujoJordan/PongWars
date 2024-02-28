import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.araujojordan.pong.Arena
import com.araujojordan.pong.Ball

internal class MainViewModel : ViewModel() {

    val ballTrue = Ball(true)
    val ballFalse = Ball(false)
    val arena = Arena(arrayOf(
        arrayOf(true, true, true, true, true, true, true, true, true, true),
        arrayOf(true, true, true, true, true, true, true, true, true, true),
        arrayOf(true, true, true, true, true, true, true, true, true, true),
        arrayOf(true, true, true, true, true, true, true, true, true, true),
        arrayOf(true, true, true, true, true, true, true, true, true, true),
        arrayOf(true, true, true, true, true, true, true, true, true, true),
        arrayOf(true, true, true, true, true, true, true, true, true, true),
        arrayOf(true, true, true, true, true, true, true, true, true, true),
        arrayOf(true, true, true, true, true, true, true, true, true, true),
        arrayOf(true, true, true, true, true, true, true, true, true, true),
    ))


}