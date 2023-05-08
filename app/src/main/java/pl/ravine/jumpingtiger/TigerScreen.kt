@file:OptIn(ExperimentalMaterial3Api::class)

package pl.ravine.jumpingtiger

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
internal fun JumingTigerNavigation() {
    val state = rememberTigerScreen()

    val currentTigerX: Float by animateFloatAsState(
        targetValue = state.tigerOffset.x,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessVeryLow
        )
    )
    val currentTigerY: Float by animateFloatAsState(
        targetValue = state.tigerOffset.y,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessLow
        ),
        finishedListener = { state.onJumpHighpoint() }
    )

    val currentPosition by remember {
        derivedStateOf {
            Offset(x = currentTigerX, y = currentTigerY)
        }
    }

    state.checkHamburgerCatched(currentPosition)

    if (state.isCatchingHamburger) {
        TaskScreen(state = state)
    } else {
        JumpingTigerScreen(state = state, tigerOffset = currentPosition)
    }
}

@Composable
internal fun JumpingTigerScreen(state: TigerScreenState, tigerOffset: Offset) {

    Scaffold(
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
                .fillMaxHeight()
                .pointerInput(true) {
                    detectTapGestures(
                        onTap = { offset -> state.onTap(offset) }
                    )
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.plansza),
                contentDescription = stringResource(id = R.string.jungle),
                contentScale = ContentScale.FillBounds
            )
            Box(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()){
                Hamburger(offset = state.hamburgerOffset)
                Tiger(headTo = state.headTo, offset = tigerOffset)
            }
        }
    }
}

@Composable
private fun Tiger(headTo: HeadTo, offset: Offset) {
    Surface(
        color = Color.Transparent,
        modifier = Modifier
            .size(width = 80.dp, height = 50.dp)
            .graphicsLayer {
                this.rotationY = if (headTo == HeadTo.RIGHT) 0f else 180f
                translationX = offset.x
                translationY = offset.y
            },
    ) {
        Image(
            painter = painterResource(id = R.drawable.tygrys),
            contentDescription = stringResource(id = R.string.tiger),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun Hamburger(offset: Offset) {
    Surface(
        color = Color.Transparent,
        modifier = Modifier
            .size(width = 30.dp, height = 30.dp)
            .graphicsLayer {
                translationX = offset.x
                translationY = offset.y
            },
    ) {
        Image(
            painter = painterResource(id = R.drawable.hamburger),
            contentDescription = stringResource(id = R.string.hamburger),
            contentScale = ContentScale.Fit
        )
    }
}