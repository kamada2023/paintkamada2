package jp.co.abs.paintkamada2.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

@Composable
fun ColorSelection(penColor: Color, color: (Color) -> Unit) {
    Row(
        modifier = Modifier
            .drawBehind {
                drawRect(
                    color = Color.LightGray,
                    size = size.copy(width = size.width, height = size.height)
                )
            }
    ) {
        var black = BorderStroke(width = 0.dp, color = Color.Black)
        var red = BorderStroke(width = 0.dp, color = Color.White)
        var blue = BorderStroke(width = 0.dp, color = Color.White)
        var yellow = BorderStroke(width = 0.dp, color = Color.White)
        var green = BorderStroke(width = 0.dp, color = Color.White)
        var orange = BorderStroke(width = 0.dp, color = Color.White)
        var purple = BorderStroke(width = 0.dp, color = Color.White)
        var brown = BorderStroke(width = 0.dp, color = Color.White)
        var lightBlue = BorderStroke(width = 0.dp, color = Color.White)
        var gray = BorderStroke(width = 0.dp, color = Color.White)

        when (penColor) {
            Color(0xFF000000) -> {
                black = BorderStroke(5.dp, color = Color.White)
            }

            Color(0xFFFF0000) -> {
                red = BorderStroke(5.dp, color = Color.Black)
            }

            Color(0xFF0000FF) -> {
                blue = BorderStroke(5.dp, color = Color.Black)
            }

            Color(0xFFFFFB00) -> {
                yellow = BorderStroke(5.dp, color = Color.Black)
            }

            Color(0xFF00FF00) -> {
                green = BorderStroke(5.dp, color = Color.Black)
            }

            Color(0xFFFF5722) -> {
                orange = BorderStroke(5.dp, color = Color.Black)
            }

            Color(0xFFA600FF) -> {
                purple = BorderStroke(5.dp, color = Color.Black)
            }

            Color(0xFFCF490A) -> {
                brown = BorderStroke(5.dp, color = Color.Black)
            }

            Color(0xFF00BCD4) -> {
                lightBlue = BorderStroke(5.dp, color = Color.Black)
            }

            Color(0xFF646464) -> {
                gray = BorderStroke(5.dp, color = Color.Black)
            }
        }
        Button(
            onClick = { color(Color(0xFF000000)) },
            colors = ButtonDefaults.buttonColors(Color(0xFF000000)),
            border = black,
            modifier = Modifier
                .weight(weight = 1f, fill = true)
                .semantics { contentDescription = "Black" }
        ) {}
        Button(
            onClick = { color(Color(0xFFFF0000)) },
            colors = ButtonDefaults.buttonColors(Color(0xFFFF0000)),
            border = red,
            modifier = Modifier
                .weight(weight = 1f, fill = true)
                .semantics { contentDescription = "Red" }
        ) {}
        Button(
            onClick = { color(Color(0xFF0000FF)) },
            colors = ButtonDefaults.buttonColors(Color(0xFF0000FF)),
            border = blue,
            modifier = Modifier
                .weight(weight = 1f, fill = true)
                .semantics { contentDescription = "Blue" }
        ) {}
        Button(
            onClick = { color(Color(0xFFFFFB00)) },
            colors = ButtonDefaults.buttonColors(Color(0xFFFFFB00)),
            border = yellow,
            modifier = Modifier
                .weight(weight = 1f, fill = true)
                .semantics { contentDescription = "Yellow" }
        ) {}

        Button(
            onClick = { color(Color(0xFF00FF00)) },
            colors = ButtonDefaults.buttonColors(Color(0xFF00FF00)),
            border = green,
            modifier = Modifier
                .weight(weight = 1f, fill = true)
                .semantics { contentDescription = "Green" }
        ) {}
        Button(
            onClick = { color(Color(0xFFFF5722)) },
            colors = ButtonDefaults.buttonColors(Color(0xFFFF5722)),
            border = orange,
            modifier = Modifier
                .weight(weight = 1f, fill = true)
                .semantics { contentDescription = "Orange" }
        ) {}
        Button(
            onClick = { color(Color(0xFFA600FF)) },
            colors = ButtonDefaults.buttonColors(Color(0xFFA600FF)),
            border = purple,
            modifier = Modifier
                .weight(weight = 1f, fill = true)
                .semantics { contentDescription = "Purple" }
        ) {}
        Button(
            onClick = { color(Color(0xFFCF490A)) },
            colors = ButtonDefaults.buttonColors(Color(0xFFCF490A)),
            border = brown,
            modifier = Modifier
                .weight(weight = 1f, fill = true)
                .semantics { contentDescription = "Brown" }
        ) {}
        Button(
            onClick = { color(Color(0xFF00BCD4)) },
            colors = ButtonDefaults.buttonColors(Color(0xFF00BCD4)),
            border = lightBlue,
            modifier = Modifier
                .weight(weight = 1f, fill = true)
                .semantics { contentDescription = "LightBlue" }
        ) {}
        Button(
            onClick = { color(Color(0xFF646464)) },
            colors = ButtonDefaults.buttonColors(Color(0xFF646464)),
            border = gray,
            modifier = Modifier
                .weight(weight = 1f, fill = true)
                .semantics { contentDescription = "Gray" }
        ) {}
    }
}
