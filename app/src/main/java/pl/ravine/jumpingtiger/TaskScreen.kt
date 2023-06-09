package pl.ravine.jumpingtiger

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import pl.ravine.jumpingtiger.ui.theme.JumpingTigerTheme
import pl.ravine.jumpingtiger.ui.theme.Orange

@Serializable
internal data class Question(
    val task: String,
    val result: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TaskScreen(state: TigerScreenState) {

    val question by produceState(initialValue = Question(task = "Pytanie...", result = "hack")) {
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
        }
        val question: Question =
            client.get("https://calculate-graal-mvn-nhgp6wxvtq-lm.a.run.app/").body()
        value = question
        awaitDispose {
            client.close()
        }
    }

//    remember {
//        mutableStateOf("Question")
//    }

    var answer by remember {
        mutableStateOf("")
    }

    var isCorrect by remember {
        mutableStateOf(true)
    }

    Scaffold(modifier = Modifier.padding(8.dp)) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            Text(text = question.task, fontSize = 30.sp)
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = answer,
                textStyle = LocalTextStyle.current.copy(color = if (isCorrect) Color.Black else Color.Red),
                onValueChange = { typed ->
                    answer = typed
                    isCorrect = true
                }
            )
            TextButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Orange),
                onClick = {
                    isCorrect = answer.equals(question.result, ignoreCase = true)
                    if (isCorrect) state.hamburgerConsumed()

                }
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