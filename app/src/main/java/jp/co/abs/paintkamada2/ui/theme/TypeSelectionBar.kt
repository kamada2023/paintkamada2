package jp.co.abs.paintkamada2.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import jp.co.abs.paintkamada2.DrawingPathRoute

@Composable
fun TypeSelectionBar(
    modifier: Modifier = Modifier,
    penSize: Float,
    shapeType: MutableState<ShapeType>,
    tempStorageSize: MutableState<List<DrawingPathRoute>?>,
    resize: (Float) -> Unit
) {
    Row(modifier = modifier.drawBehind {
            drawRect(
                color = Color.LightGray,
                size = size.copy(width = size.width, height = size.height)
            )
        }) {
        Button(
            onClick = { resize(10.0f) },
            modifier = Modifier.weight(1f),
            border = if (penSize.equals(10.0f)) BorderStroke(3.dp, Color.Black)
            else BorderStroke(0.dp, Color.Black),
            shape = MaterialTheme.shapes.small
        ) {
            Text(text = AnnotatedString("太"))
        }
        Button(
            onClick = { resize(4.0f) },
            modifier = Modifier.weight(1f),
            border = if (penSize.equals(10.0f)) BorderStroke(0.dp, Color.Black)
            else BorderStroke(3.dp, Color.Black),
            shape = MaterialTheme.shapes.small
        ) {
            Text(text = AnnotatedString("細"))
        }
        Column(modifier = Modifier
            .semantics { contentDescription = "Oval" }
            .clickable { shapeType.value = ShapeType.ARC }
            .weight(1f)
            .drawBehind {
                if (shapeType.value == ShapeType.ARC) {
                    drawOval(
                        color = Color.Black,
                        topLeft = Offset(20f, 10f),
                        size = size.copy(80f, 80f),
                        style = Stroke(width = 15f)
                    )
                }
                drawOval(
                    color = Color.Blue, topLeft = Offset(20f, 10f), size = size.copy(80f, 80f)
                )
            }) {}
        Box(modifier = Modifier
            .semantics { contentDescription = "Triangle" }
            .clickable { shapeType.value = ShapeType.TRIANGLE }
            .weight(1f)
            .drawBehind {
                val path = Path()
                path.moveTo(55f, 10f)
                path.lineTo(10f, 90f)
                path.lineTo(100f, 90f)
                if (shapeType.value == ShapeType.TRIANGLE) {
                    path.lineTo(55f, 10f)
                    drawPath(
                        path = path, color = Color.Black, style = Stroke(
                            width = 15f, cap = StrokeCap.Round
                        )
                    )
                }
                drawPath(
                    path = path, color = Color.Blue
                )
            }) {}
        Box(modifier = Modifier
            .semantics { contentDescription = "Rect" }
            .clickable { shapeType.value = ShapeType.RECT }
            .weight(1f)
            .drawBehind {
                if (shapeType.value == ShapeType.RECT) {
                    drawRect(
                        color = Color.Black,
                        topLeft = Offset(10f, 10f),
                        size = size.copy(80f, 80f),
                        style = Stroke(width = 15f)
                    )
                }
                drawRect(
                    color = Color.Blue, topLeft = Offset(10f, 10f), size = size.copy(80f, 80f)
                )
            }) {}
        Box(modifier = Modifier
            .semantics { contentDescription = "Pen" }
            .clickable {
                shapeType.value = ShapeType.BRUSH
                tempStorageSize.value = null
            }
            .weight(1f)
            .drawBehind {
                val path = Path()
                path.moveTo(10f, 60f)
                path.lineTo(10f, 90f)
                path.lineTo(40f, 90f)
                path.lineTo(100f, 40f)
                path.lineTo(70f, 10f)
                if (shapeType.value == ShapeType.BRUSH) {
                    path.lineTo(10f, 65f)
                    drawPath(
                        path = path, color = Color.Black, style = Stroke(width = 15f)
                    )
                }
                drawPath(
                    path = path, color = Color.Blue
                )
            }) {}
    }
}