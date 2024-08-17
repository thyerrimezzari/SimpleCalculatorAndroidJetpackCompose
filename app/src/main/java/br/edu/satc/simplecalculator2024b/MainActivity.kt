package br.edu.satc.simplecalculator2024b

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.edu.satc.simplecalculator2024b.ui.theme.SimpleCalculator2024BTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleCalculator2024BTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Greeting("Android")
                    CalculatorApp()
                }
            }
        }
    }
}

@Composable
fun CalculatorButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier
            .padding(PaddingValues(4.dp))
            .fillMaxSize()
    ) {
        Text(text = text, fontSize = 48.sp)
    }
}

@Composable
fun CalculatorApp() {
    val display = remember { mutableStateOf(TextFieldValue("0")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicTextField(
            value = display.value,
            onValueChange = { display.value = it },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.Black)
                .padding(16.dp),
            textStyle = androidx.compose.ui.text.TextStyle(
                color = Color.White,
                fontSize = 64.sp,
                textAlign = TextAlign.Right
            )
        )

        Column(
            modifier = Modifier.weight(3f)
        ) {
            val buttons = listOf(
                listOf("7", "8", "9", "/"),
                listOf("4", "5", "6", "*"),
                listOf("1", "2", "3", "-"),
                listOf("0", "C", "=", "+")
            )
            buttons.forEach { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    row.forEach { button ->
                        CalculatorButton(
                            text = button,
                            onClick = { onButtonClick(button, display) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

fun onButtonClick(button: String, display: MutableState<TextFieldValue>) {

    when (button) {
        "=" -> {
            val result = evaluateExpression(display.value.text)
            display.value = TextFieldValue(result.toString())
        }
        "C" -> {
            display.value = TextFieldValue("0")
        }

        else -> {
            if (display.value.text == "0") {
                display.value = TextFieldValue(button)
            } else {
                display.value = TextFieldValue(display.value.text + button)
            }
        }
    }
}

fun evaluateExpression(expression: String): Double {
    val tokens = expression.split("").filter { it.isNotEmpty() }
    val numbers = mutableListOf<Double>()
    val operators = mutableListOf<String>()

    for (token in tokens) {
        when (token) {
            "+", "-", "*", "/" -> operators.add(token)
            else -> numbers.add(token.toDouble())
        }
    }

    // Realiza as operações na ordem de prioridade: multiplicação e divisão primeiro, depois adição e subtração
    var result = numbers[0]
    var operatorIndex = 0

    while (operatorIndex < operators.size) {
        val operator = operators[operatorIndex]
        val nextNumber = numbers[operatorIndex + 1]

        result = when (operator) {
            "*" -> result * nextNumber
            "/" -> result / nextNumber
            "+" -> result + nextNumber
            "-" -> result - nextNumber
            else -> result
        }

        operatorIndex++
    }

    return result
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    SimpleCalculator2024BTheme {
        // Greeting("Android")
        CalculatorApp()
    }
}