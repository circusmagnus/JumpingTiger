package pl.ravine.jumpingtiger

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import kotlin.random.Random
import kotlin.random.nextInt

private const val tigerBaseX = 400f
private const val tigerBaseY = 1800f

internal enum class HeadTo { RIGHT, LEFT }

private const val errorMargin = 30

@Composable
internal fun rememberTigerScreen() = remember {
    TigerScreenState()
}

internal class TigerScreenState {

    var tigerOffset by mutableStateOf(Offset(tigerBaseX, tigerBaseY))
        private set

    var headTo: HeadTo by mutableStateOf(HeadTo.RIGHT)
        private set

    var hamburgerOffset by mutableStateOf(calculateHamburgerOffset())
        private set

    var isCatchingHamburger by mutableStateOf(false)

    private fun calculateHamburgerOffset(): Offset {
        val randomX = Random.nextInt(-350..350)
        val randomY = Random.nextInt(-1750..-300)

        return Offset(
            x = tigerBaseX + randomX,
            y = tigerBaseY + randomY
        )
    }


    fun onTap(offset: Offset) {
        headTo = if (offset.x < tigerOffset.x) HeadTo.LEFT else HeadTo.RIGHT
        tigerOffset = offset
    }

    fun checkHamburgerCatched(currentPosition: Offset) {
        if (isCatchingHamburger) return
        val xHamburgerRange = hamburgerOffset.x - errorMargin..hamburgerOffset.x + errorMargin
        val yHamburgerRange = hamburgerOffset.y - errorMargin..hamburgerOffset.y + errorMargin
        isCatchingHamburger = currentPosition.x in xHamburgerRange && currentPosition.y in yHamburgerRange
    }

    fun hamburgerConsumed(){
        isCatchingHamburger = false
        hamburgerOffset = calculateHamburgerOffset()
    }

    fun onJumpHighpoint() {
        tigerOffset = tigerOffset.copy(y = tigerBaseY)
    }


}