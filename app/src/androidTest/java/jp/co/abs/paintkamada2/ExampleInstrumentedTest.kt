package jp.co.abs.paintkamada2

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.click
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import jp.co.abs.paintkamada2.ui.theme.ColorSelection
import jp.co.abs.paintkamada2.ui.theme.DrawingScreen
import jp.co.abs.paintkamada2.ui.theme.SettingsTopAppBar
import jp.co.abs.paintkamada2.ui.theme.ShapeType
import jp.co.abs.paintkamada2.ui.theme.TypeSelectionBar
import jp.co.abs.paintkamada2.ui.theme.paintkamada2Theme

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule
import kotlin.math.abs

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun drawingScreenTest() {
        composeTestRule.setContent {
            paintkamada2Theme { DrawingScreen() }
        }
        composeTestRule.onRoot().printToLog("オーバルみる")
        composeTestRule.onNodeWithText("ペイントアプリVer.2").assertIsDisplayed()
    }

    @Test
    fun typeSelectionBarTest() {
        composeTestRule.setContent {
            var penSize by remember { mutableStateOf(4.0f) }
            val shapeType = remember { mutableStateOf(ShapeType.BRUSH) }
            val tempStorageSize = remember { mutableStateOf<List<DrawingPathRoute>?>(null) }
            TypeSelectionBar(penSize = penSize,
                shapeType = shapeType,
                tempStorageSize = tempStorageSize,
                resize = { resize -> penSize = resize })
        }
        composeTestRule.onNodeWithText("太").assertIsDisplayed().assertHasClickAction()
            .performClick()
        composeTestRule.onNodeWithText("細").assertIsDisplayed().assertHasClickAction()
            .performClick()
        composeTestRule.onNodeWithContentDescription("Oval").assertHasClickAction().performClick()
            .performTouchInput { click(Offset(20f, 10f)) }
        composeTestRule.onNodeWithContentDescription("Triangle").assertHasClickAction()
            .performTouchInput { click(Offset(55f, 10f)) }
        composeTestRule.onNodeWithContentDescription("Rect").assertHasClickAction()
            .performTouchInput { click(Offset(20f, 10f)) }
        composeTestRule.onNodeWithContentDescription("Pen").assertHasClickAction()
            .performTouchInput { click(Offset(10f, 60f)) }
    }

    @Test
    fun settingsTopAppBarFalseTest() {
        composeTestRule.setContent {
            val tracks = remember { mutableStateOf<List<CustomDrawingPath>?>(null) }
            val bitmap = remember { mutableStateOf<Bitmap?>(null) }
            val tempStorageSize = remember { mutableStateOf<List<DrawingPathRoute>?>(null) }
            val saveJudge = true
            SettingsTopAppBar(
                tracks = tracks,
                bitmap = bitmap,
                tempStorageSize = tempStorageSize,
                saveJudge = saveJudge
            )
        }
        composeTestRule.onNodeWithText("CLEAR").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("SAVE").assertIsDisplayed().performClick()
        Thread.sleep(1000)
    }

    @Test
    fun settingsTopAppBarTrueTest() {
        val shapeLineTest = ArrayList<CustomDrawingPath>().apply {
            add(
                CustomDrawingPath(
                    path = DrawingPathRoute.MoveTo(100F, 100F),
                    color = Color.Green,
                    size = 4f,
                    type = ShapeType.RECT
                )
            )
            add(
                CustomDrawingPath(
                    path = DrawingPathRoute.MoveTo(300F, 300F),
                    color = Color.Green,
                    size = 4f,
                    type = ShapeType.RECT
                )
            )
        }
        val dottedLineTest = ArrayList<DrawingPathRoute>().apply {
            add(DrawingPathRoute.TopLeft(100F, 100F))
            add(DrawingPathRoute.SizeTo(300F, 300F))
        }
        composeTestRule.setContent {
            val density = LocalDensity.current
            val layoutDirection = LocalLayoutDirection.current
            val tracks =
                remember { mutableStateOf<List<CustomDrawingPath>?>(shapeLineTest.toList()) }
            val bitmap = remember { mutableStateOf<Bitmap?>(null) }
            val tempStorageSize =
                remember { mutableStateOf<List<DrawingPathRoute>?>(dottedLineTest.toList()) }
            var saveJudge by remember { mutableStateOf(false) }
            Canvas(modifier = Modifier.fillMaxSize()) {
                val imageBitmap =
                    ImageBitmap(width = size.width.toInt(), height = size.height.toInt())
                CanvasDrawScope().draw(
                    density, layoutDirection, androidx.compose.ui.graphics.Canvas(imageBitmap), size
                ) {
                    drawRect(
                        color = Color.White,
                        size = size.copy(width = size.width, height = size.height)
                    )
                    if (tracks.value != null) {
                        drawRect(
                            color = (tracks.value as ArrayList<CustomDrawingPath>)[0].color,
                            topLeft = Offset(
                                (tracks.value as ArrayList<CustomDrawingPath>)[0].path.x,
                                (tracks.value as ArrayList<CustomDrawingPath>)[0].path.x
                            ),
                            size = Size(
                                abs((tracks.value as ArrayList<CustomDrawingPath>)[0].path.x - (tracks.value as ArrayList<CustomDrawingPath>)[1].path.x),
                                abs((tracks.value as ArrayList<CustomDrawingPath>)[0].path.y - (tracks.value as ArrayList<CustomDrawingPath>)[1].path.y)
                            ),
                            style = Stroke(width = (tracks.value as ArrayList<CustomDrawingPath>)[0].size),
                            blendMode = BlendMode.SrcOver
                        )
                    }
                    if (tempStorageSize.value != null) {
                        drawRect(
                            color = Color.Black, topLeft = Offset(
                                tempStorageSize.value!![0].x, tempStorageSize.value!![0].y
                            ), size = Size(
                                tempStorageSize.value!![1].x - tempStorageSize.value!![0].x,
                                tempStorageSize.value!![1].y - tempStorageSize.value!![0].y
                            ), style = Stroke(
                                width = 3.0f, pathEffect = PathEffect.dashPathEffect(
                                    intervals = floatArrayOf(
                                        20f, 10f
                                    ), phase = 30f
                                )
                            ), blendMode = BlendMode.SrcOver
                        )
                    }
                }
                bitmap.value = imageBitmap.asAndroidBitmap()
            }
            Canvas(modifier = Modifier.fillMaxSize()) {
                if (bitmap.value != null) drawImage(bitmap.value!!.asImageBitmap())
                saveJudge = tempStorageSize.value == null
            }
            SettingsTopAppBar(
                tracks = tracks,
                bitmap = bitmap,
                tempStorageSize = tempStorageSize,
                saveJudge = saveJudge
            )
        }
        composeTestRule.onNodeWithText("SAVE").assertIsDisplayed().performClick()
        Thread.sleep(1000)
        composeTestRule.onNodeWithText("SAVE").assertIsDisplayed().performClick()
        Thread.sleep(1000)
        composeTestRule.onNodeWithText("CLEAR").assertIsDisplayed().performClick()
        Thread.sleep(1000)
    }

    @Test
    fun colorSelectionTest() {
        composeTestRule.setContent {
            var penColor by remember { mutableStateOf(Color.Black) }
            ColorSelection(penColor = penColor, color = { color -> penColor = color })
        }
        composeTestRule.onNodeWithContentDescription("Black").assertIsDisplayed()
            .assertHasClickAction().performClick()
        composeTestRule.onNodeWithContentDescription("Red").assertIsDisplayed()
            .assertHasClickAction().performClick()
        composeTestRule.onNodeWithContentDescription("Blue").assertIsDisplayed()
            .assertHasClickAction().performClick()
        composeTestRule.onNodeWithContentDescription("Yellow").assertIsDisplayed()
            .assertHasClickAction().performClick()
        composeTestRule.onNodeWithContentDescription("Green").assertIsDisplayed()
            .assertHasClickAction().performClick()
        composeTestRule.onNodeWithContentDescription("Orange").assertIsDisplayed()
            .assertHasClickAction().performClick()
        composeTestRule.onNodeWithContentDescription("Purple").assertIsDisplayed()
            .assertHasClickAction().performClick()
        composeTestRule.onNodeWithContentDescription("Brown").assertIsDisplayed()
            .assertHasClickAction().performClick()
        composeTestRule.onNodeWithContentDescription("LightBlue").assertIsDisplayed()
            .assertHasClickAction().performClick()
        composeTestRule.onNodeWithContentDescription("Gray").assertIsDisplayed()
            .assertHasClickAction().performClick()
        Thread.sleep(2000)
    }
}