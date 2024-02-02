package jp.co.abs.paintkamada2.ui.theme

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import jp.co.abs.paintkamada2.CustomDrawingPath
import jp.co.abs.paintkamada2.DrawingPathRoute

@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawingScreen(modifier: Modifier = Modifier) {
    // 描画の履歴の記録のため
    var penColor by remember { mutableStateOf(Color(0xFF000000)) }
    var penSize by remember { mutableStateOf(4.0f) }
    val shapeType = remember { mutableStateOf(ShapeType.BRUSH) }
    val tracks = rememberSaveable { mutableStateOf<List<CustomDrawingPath>?>(null) }
    val tempStorageSize = remember { mutableStateOf<List<DrawingPathRoute>?>(null) }
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    var saveJudge by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = { Text("ペイントアプリVer.2") }
                )
                SettingsTopAppBar(
                    tracks = tracks, bitmap = bitmap,
                    tempStorageSize = tempStorageSize, saveJudge = saveJudge
                )
            }
        },
        bottomBar = {
            Column {
                ColorSelection(penColor = penColor, color = { color -> penColor = color })
                TypeSelectionBar(
                    penSize = penSize,
                    shapeType = shapeType,
                    tempStorageSize = tempStorageSize,
                    resize = { resize -> penSize = resize })
            }
        }
    ) {
        /**Canvasでの描画部分*/
        DrawingCanvas(
            tracks = tracks,
            tempStorageSize = tempStorageSize,
            penSize = penSize,
            penColor = penColor,
            shapeType = shapeType,
            modifier = Modifier.padding(it),
            bitmap = bitmap
        )
        Canvas(modifier = Modifier.padding(it)) {
            if (bitmap.value != null) drawImage(bitmap.value!!.asImageBitmap())
            saveJudge = tempStorageSize.value == null
        }
    }
}
enum class ShapeType {
    BRUSH,
    ARC,
    TRIANGLE,
    RECT,
}