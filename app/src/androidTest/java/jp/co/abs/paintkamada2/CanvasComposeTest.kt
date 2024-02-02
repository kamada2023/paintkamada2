package jp.co.abs.paintkamada2

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.dragAndDrop
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performMouseInput
import jp.co.abs.paintkamada2.ui.theme.DrawingCanvas
import jp.co.abs.paintkamada2.ui.theme.ShapeType
import org.junit.Rule
import org.junit.Test

class CanvasComposeTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun drawingCanvasPenTest() {
        composeTestRule.setContent {
            val penColor = Color(0xFF000000)
            val penSize = 4.0f
            val shapeType = remember { mutableStateOf(ShapeType.BRUSH) }
            val tracks = rememberSaveable { mutableStateOf<List<CustomDrawingPath>?>(null) }
            val tempStorageSize = remember { mutableStateOf<List<DrawingPathRoute>?>(null) }
            val bitmap = remember { mutableStateOf<Bitmap?>(null) }
            var saveJudge by remember { mutableStateOf(true) }
            DrawingCanvas(
                tracks = tracks,
                tempStorageSize = tempStorageSize,
                penSize = penSize,
                penColor = penColor,
                shapeType = shapeType,
                modifier = Modifier.fillMaxSize(),
                bitmap = bitmap
            )
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                val bmp = bitmap.value
                if (bmp != null) drawImage(bmp.asImageBitmap())
                saveJudge = tempStorageSize.value == null
            }
        }
        composeTestRule.onNodeWithContentDescription("tapJudgment")
            .assertIsDisplayed()
            .performMouseInput {
                dragAndDrop(
                    start = Offset(100f, 100f),
                    end = Offset(300f, 300f)
                )
            }
        composeTestRule.onNodeWithContentDescription("tapJudgment")
            .assertIsDisplayed()
            .performMouseInput {
                dragAndDrop(
                    start = Offset(1000f, 100f),
                    end = Offset(800f, 300f),
                )
            }
        composeTestRule.onNodeWithContentDescription("tapJudgment")
            .assertIsDisplayed()
            .performMouseInput {
                dragAndDrop(
                    start = Offset(100f, 1200f),
                    end = Offset(300f, 1000f)
                )
            }
        composeTestRule.onNodeWithContentDescription("tapJudgment")
            .assertIsDisplayed()
            .performMouseInput {
                dragAndDrop(
                    start = Offset(1000f, 1200f),
                    end = Offset(800f, 1000f),
                )
            }
        Thread.sleep(2000)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun drawingCanvasARCTest() {
        composeTestRule.setContent {
            val penColor = Color(0xFF000000)
            val penSize = 4.0f
            val shapeType = remember { mutableStateOf(ShapeType.ARC) }
            val tracks = rememberSaveable { mutableStateOf<List<CustomDrawingPath>?>(null) }
            val tempStorageSize = remember { mutableStateOf<List<DrawingPathRoute>?>(null) }
            val bitmap = remember { mutableStateOf<Bitmap?>(null) }
            var saveJudge by remember { mutableStateOf(false) }
            DrawingCanvas(
                tracks = tracks,
                tempStorageSize = tempStorageSize,
                penSize = penSize,
                penColor = penColor,
                shapeType = shapeType,
                modifier = Modifier.fillMaxSize(),
                bitmap = bitmap
            )
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                val bmp = bitmap.value
                if (bmp != null) drawImage(bmp.asImageBitmap())
                saveJudge = tempStorageSize.value == null
            }
        }
        composeTestRule.onNodeWithContentDescription("tapJudgment")
            .assertIsDisplayed()
            .performMouseInput {
                dragAndDrop(
                    start = Offset(100f, 100f),
                    end = Offset(300f, 300f)
                )
                dragAndDrop(
                    start = Offset(1000f, 100f),
                    end = Offset(800f, 300f),
                )
                dragAndDrop(
                    start = Offset(100f, 1200f),
                    end = Offset(300f, 1000f)
                )
                dragAndDrop(
                    start = Offset(1000f, 1200f),
                    end = Offset(800f, 1000f),
                )
            }
        Thread.sleep(500)
//topLeft_small->big->small->big
        composeTestRule.onNodeWithContentDescription("tapJudgment")
            .assertIsDisplayed()
            .performMouseInput {
                Thread.sleep(500)
                dragAndDrop(
                    start = Offset(809.9f, 1009.9f),
                    end = Offset(980.1f, 1180.1f)
                )
            }
            .performMouseInput {
                Thread.sleep(500)
                dragAndDrop(
                    start = Offset(989.9f, 1189.9f),
                    end = Offset(700f, 700f)
                )
            }
            .performMouseInput {
                Thread.sleep(500)
                dragAndDrop(
                    start = Offset(690.1f, 690.1f),
                    end = Offset(1001f, 1201f)
                )
            }
            .performMouseInput {
                Thread.sleep(500)
                dragAndDrop(
                    start = Offset(970.1f, 1170.1f),
                    end = Offset(800f, 1000f)
                )
            }
//top_small->big->small->big
        composeTestRule.onNodeWithContentDescription("tapJudgment")
            .assertIsDisplayed()
            .performMouseInput {
                Thread.sleep(500)
                dragAndDrop(
                    start = Offset(810f, 1009.9f),
                    end = Offset(1000f, 1200f),
                )
            }
            .performMouseInput {
                Thread.sleep(500)
                dragAndDrop(
                    start = Offset(810f, 1189.9f),
                    end = Offset(700f, 800f),
                )
            }
            .performMouseInput {
                Thread.sleep(500)
                dragAndDrop(
                    start = Offset(990f, 790.1f),
                    end = Offset(1001f, 1201f),
                )
            }
            .performMouseInput {
                Thread.sleep(500)
                dragAndDrop(
                    start = Offset(990f, 1170.1f),
                    end = Offset(800f, 1000f),
                )
            }
        //topRight_small->big->small->big
        composeTestRule.onNodeWithContentDescription("tapJudgment")
            .assertIsDisplayed()
            .performMouseInput {
                Thread.sleep(500)
                dragAndDrop(
                    start = Offset(1009.9f, 1009.9f),
                    end = Offset(780f, 1180f)
                )
            }
            .performMouseInput {
                Thread.sleep(500)
                dragAndDrop(
                    start = Offset(829.9f, 1170.1f),
                    end = Offset(1080f, 1080f)
                )
            }
            .performMouseInput {
                Thread.sleep(500)
                dragAndDrop(
                    start = Offset(1070.1f, 1070.1f),
                    end = Offset(801f, 1201f)
                )
            }
            .performMouseInput {
                Thread.sleep(500)
                dragAndDrop(
                    start = Offset(810.1f,1189.9f),
                    end = Offset(1000f, 1000f)
                )
            }
        //right
        composeTestRule.onNodeWithContentDescription("tapJudgment")
            .assertIsDisplayed()
            .performMouseInput {
                Thread.sleep(500)
                dragAndDrop(
                    start = Offset(1009.9f, 1010f),
                    end = Offset(819.9f, 800f),
                )
            }
            .performMouseInput {
                Thread.sleep(500)
                dragAndDrop(
                    start = Offset(829.9f, 1190f),
                    end = Offset(1080f, 1300f),
                )
            }
            .performMouseInput {
                Thread.sleep(500)
                dragAndDrop(
                    start = Offset(1070.1f, 1010f),
                    end = Offset(780f, 780f),
                )
            }
            .performMouseInput {
                Thread.sleep(500)
                dragAndDrop(
                    start = Offset(810.1f, 1190f),
                    end = Offset(1000f, 1000f),
                )
            }
        //rightBottom
        composeTestRule.onNodeWithContentDescription("tapJudgment")
            .assertIsDisplayed()
            .performMouseInput {
                Thread.sleep(500)
                dragAndDrop(
                    start = Offset(1009.9f, 1209.9f),
                    end = Offset(800f, 1000f),
                )
            }
            .performMouseInput {
                Thread.sleep(500)
                dragAndDrop(
                    start = Offset(810.1f, 1010.1f),
                    end = Offset(1000f, 1300f),
                )
            }
            .performMouseInput {
                Thread.sleep(500)
                dragAndDrop(
                    start = Offset(1009.9f, 1309.9f),
                    end = Offset(780f, 980f),
                )
            }
            .performMouseInput {
                Thread.sleep(500)
                dragAndDrop(
                    start = Offset(810.1f, 1010.1f),
                    end = Offset(1000f, 1200f),
                )
            }
        //bottom
        composeTestRule.onNodeWithContentDescription("tapJudgment")
            .assertIsDisplayed()
            .performMouseInput {
                Thread.sleep(500)
                dragAndDrop(
                    start = Offset(990f, 1209.9f),
                    end = Offset(780f, 980f),
                )
            }
            .performMouseInput {
                Thread.sleep(500)
                dragAndDrop(
                    start = Offset(990f, 1010.1f),
                    end = Offset(900f, 1500f),
                )
            }
            .performMouseInput {
                Thread.sleep(500)
                dragAndDrop(
                    start = Offset(810f, 1490.1f),
                    end = Offset(1000f, 1000f),
                )
            }
            .performMouseInput {
                Thread.sleep(500)
                dragAndDrop(
                    start = Offset(810f, 1029.9f),
                    end = Offset(1000f, 1200f),
                )
            }
//bottomLeft
        composeTestRule.onNodeWithContentDescription("tapJudgment")
            .assertIsDisplayed()
            .performMouseInput {
                Thread.sleep(500)
                dragAndDrop(
                    start = Offset(809.9f, 1209.9f),
                    end = Offset(1080f, 900f),
                )
            }
            .performMouseInput {
                Thread.sleep(500)
                dragAndDrop(
                    start = Offset(989.9f, 1029.9f),
                    end = Offset(600f, 1500f),
                )
            }
            .performMouseInput {
                Thread.sleep(500)
                dragAndDrop(
                    start = Offset(590.1f, 1490.1f),
                    end = Offset(1000f, 1000f),
                )
            }
            .performMouseInput {
                Thread.sleep(500)
                dragAndDrop(
                    start = Offset(970.1f, 1010.1f),
                    end = Offset(800f, 1200f),
                )
            }
        //left
        composeTestRule.onNodeWithContentDescription("tapJudgment")
            .assertIsDisplayed()
            .performMouseInput {
                Thread.sleep(500)
                dragAndDrop(
                    start = Offset(809.9f, 1010f),
                    end = Offset(1000f, 1500f),
                )
            }
            .performMouseInput {
                Thread.sleep(500)
                dragAndDrop(
                    start = Offset(989.9f, 1010f),
                    end = Offset(600f, 1500f),
                )
            }
            .performMouseInput {
                Thread.sleep(500)
                dragAndDrop(
                    start = Offset(590.1f, 1190f),
                    end = Offset(1000f, 1500f),
                )
            }
            .performMouseInput {
                Thread.sleep(500)
                dragAndDrop(
                    start = Offset(970.1f, 1190f),
                    end = Offset(800f, 1500f),
                )
            }
        composeTestRule.onNodeWithContentDescription("tapJudgment")
            .assertIsDisplayed()
            .performMouseInput {
                Thread.sleep(500)
                //move
                dragAndDrop(
                    start = Offset(810f, 1010f),
                    end = Offset(510f, 510f),
                )
            }
        composeTestRule.onNodeWithContentDescription("tapJudgment")
            .assertIsDisplayed()
            .performMouseInput {
                Thread.sleep(500)
                //moveAfterResize
                dragAndDrop(
                    start = Offset(500f, 500f),
                    end = Offset(500f, 400f),
                )
            }
        composeTestRule.onNodeWithContentDescription("tapJudgment")
            .assertIsDisplayed()
            .performMouseInput {
                Thread.sleep(500)
                //anotherOne
                dragAndDrop(
                    start = Offset(490f, 500f),
                    end = Offset(800f, 900f),
                )
            }
        Thread.sleep(800)
    }
}