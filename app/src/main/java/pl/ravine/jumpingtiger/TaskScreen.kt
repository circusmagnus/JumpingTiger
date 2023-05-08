package pl.ravine.jumpingtiger

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.ravine.jumpingtiger.ui.theme.JumpingTigerTheme
import pl.ravine.jumpingtiger.ui.theme.Orange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TaskScreen(state: TigerScreenState) {

    val question by remember {
        mutableStateOf("Question")
    }

    var answer by remember {
        mutableStateOf("")
    }

    Scaffold(modifier = Modifier.padding(8.dp)) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            Text(text = question)
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = answer,
                onValueChange = { typed -> answer = typed }
            )
            TextButton(
                modifier = Modifier.fillMaxWidth().background(Orange),
                onClick = { state.hamburgerConsumed() }
            ) {
                Text(textAlign = TextAlign.Center, text = stringResource(id = R.string.send))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskScreenPreview() {
    JumpingTigerTheme {
        TaskScreen(TigerScreenState())
    }
}