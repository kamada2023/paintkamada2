package jp.co.abs.paintkamada2.ui.theme

import android.graphics.Bitmap
import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import jp.co.abs.paintkamada2.CustomDrawingPath
import jp.co.abs.paintkamada2.DrawingPathRoute
import kotlin.math.abs

@Suppress("UNUSED_EXPRESSION")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrawingCanvas(
    tracks: MutableState<List<CustomDrawingPath>?>,
    tempStorageSize: MutableState<List<DrawingPathRoute>?>,
    penSize: Float,
    penColor: Color,
    shapeType: MutableState<ShapeType>,
    modifier: Modifier,
    bitmap: MutableState<Bitmap?>
) {
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    var topSide by remember { mutableStateOf(0f) }
    var leftSide by remember { mutableStateOf(0f) }
    var rightSide by remember { mutableStateOf(0f) }
    var bottomSide by remember { mutableStateOf(0f) }
    var movementStartPosition by remember { mutableStateOf(Offset(-1f, -1f)) }
    var redrawTop by remember { mutableStateOf(-1f) }
    var redrawLeft by remember { mutableStateOf(-1f) }
    var redrawRight by remember { mutableStateOf(-1f) }
    var redrawBottom by remember { mutableStateOf(-1f) }
    var vertexMode by remember { mutableStateOf(false) }

    Canvas(modifier = modifier
        .semantics { contentDescription = "tapJudgment" }
        .fillMaxSize()
        .pointerInteropFilter { motionEvent: MotionEvent ->
            when (motionEvent.action) {
                // 描き始めの処理
                MotionEvent.ACTION_DOWN -> {
                    if (shapeType.value == ShapeType.BRUSH) {
                        tracks.value = ArrayList<CustomDrawingPath>().apply {
                            tracks.value?.let { addAll(it) }
                            add(
                                CustomDrawingPath(
                                    path = DrawingPathRoute.MoveTo(
                                        motionEvent.x, motionEvent.y
                                    ), color = penColor, size = penSize, type = shapeType.value
                                )
                            )
                            add(
                                CustomDrawingPath(
                                    path = DrawingPathRoute.LineTo(
                                        motionEvent.x + 1, motionEvent.y + 1
                                    ), color = penColor, size = penSize, type = shapeType.value
                                )
                            )
                        }
                    } else {
                        if (tempStorageSize.value == null) {
                            leftSide = 0f
                            topSide = 0f
                            rightSide = 0f
                            bottomSide = 0f
                        }
                        if ((leftSide - 10f < motionEvent.x && motionEvent.x < leftSide + 10f) && (topSide - 10f < motionEvent.y && motionEvent.y < topSide + 10f)) {
                            redrawTop = motionEvent.y
                            redrawLeft = motionEvent.x
                            vertexMode = true
                        } else if ((rightSide - 10f < motionEvent.x && motionEvent.x < rightSide + 10f) && (bottomSide - 10f < motionEvent.y && motionEvent.y < bottomSide + 10f)) {
                            redrawRight = motionEvent.x
                            redrawBottom = motionEvent.y
                            vertexMode = true
                        } else if ((rightSide - 10f < motionEvent.x && motionEvent.x < rightSide + 10f) && (topSide - 10f < motionEvent.y && motionEvent.y < topSide + 10f)) {
                            redrawRight = motionEvent.x
                            redrawTop = motionEvent.y
                            vertexMode = true
                        } else if ((leftSide - 10f < motionEvent.x && motionEvent.x < leftSide + 10f) && (bottomSide - 10f < motionEvent.y && motionEvent.y < bottomSide + 10f)) {
                            redrawLeft = motionEvent.x
                            redrawBottom = motionEvent.y
                            vertexMode = true
                        } else if ((leftSide - 10f < motionEvent.x && motionEvent.x < leftSide + 10f) && (topSide + 10 <= motionEvent.y && motionEvent.y <= bottomSide - 10f)) {
                            redrawLeft = motionEvent.x
                        } else if ((topSide - 10f < motionEvent.y && motionEvent.y < topSide + 10f) && (leftSide + 10f <= motionEvent.x && motionEvent.x <= rightSide - 10f)) {
                            redrawTop = motionEvent.y
                        } else if ((rightSide - 10f < motionEvent.x && motionEvent.x < rightSide + 10f) && (topSide + 10f <= motionEvent.y && motionEvent.y <= bottomSide - 10f)) {
                            redrawRight = motionEvent.x
                        } else if ((bottomSide - 10f < motionEvent.y && motionEvent.y < bottomSide + 10f) && (leftSide + 10f <= motionEvent.x && motionEvent.x <= rightSide - 10f)) {
                            redrawBottom = motionEvent.y
                        } else if ((leftSide + 10f <= motionEvent.x) && (motionEvent.x <= rightSide - 10f) && (topSide <= motionEvent.y + 10f) && (motionEvent.y <= bottomSide - 10f)) {
                            movementStartPosition = Offset(motionEvent.x, motionEvent.y)
                        } else {
                            tempStorageSize.value = null
                            topSide = motionEvent.y
                            leftSide = motionEvent.x
                            tracks.value = ArrayList<CustomDrawingPath>().apply {
                                tracks.value?.let { addAll(it) }
                                add(
                                    CustomDrawingPath(
                                        path = DrawingPathRoute.TopLeft(
                                            motionEvent.x, motionEvent.y
                                        ), color = penColor, size = penSize, type = shapeType.value
                                    )
                                )
                                add(
                                    CustomDrawingPath(
                                        path = DrawingPathRoute.SizeTo(
                                            motionEvent.x, motionEvent.y
                                        ), color = penColor, size = penSize, type = shapeType.value
                                    )
                                )
                            }
                        }
                    }
                }
                // 書いてる途中の処理
                MotionEvent.ACTION_MOVE -> {
                    if (shapeType.value == ShapeType.BRUSH) {
                        tracks.value = ArrayList<CustomDrawingPath>().apply {
                            tracks.value?.let { addAll(it) }
                            add(
                                CustomDrawingPath(
                                    path = DrawingPathRoute.LineTo(
                                        motionEvent.x, motionEvent.y
                                    ), color = penColor, size = penSize, type = shapeType.value
                                )
                            )
                        }
                    } else {
                        if (tempStorageSize.value == null) {
                            tracks.value = ArrayList<CustomDrawingPath>().apply {
                                tracks.value?.let { addAll(it) }
                                set(
                                    lastIndex, CustomDrawingPath(
                                        path = DrawingPathRoute.SizeTo(
                                            motionEvent.x, motionEvent.y
                                        ), color = penColor, size = penSize, type = shapeType.value
                                    )
                                )
                            }
                        } else if (movementStartPosition != Offset(-1f, -1f)) {
                            //位置変更
                            redrawLeft = leftSide + (motionEvent.x - movementStartPosition.x)
                            redrawTop = topSide + (motionEvent.y - movementStartPosition.y)
                            redrawRight = rightSide + (motionEvent.x - movementStartPosition.x)
                            redrawBottom = bottomSide + (motionEvent.y - movementStartPosition.y)

                            tracks.value = ArrayList<CustomDrawingPath>().apply {
                                tracks.value?.let { addAll(it) }
                                set(
                                    lastIndex - 1, CustomDrawingPath(
                                        path = DrawingPathRoute.TopLeft(redrawLeft, redrawTop),
                                        color = penColor,
                                        size = penSize,
                                        type = shapeType.value
                                    )
                                )
                                set(
                                    lastIndex, CustomDrawingPath(
                                        path = DrawingPathRoute.SizeTo(
                                            redrawRight, redrawBottom
                                        ), color = penColor, size = penSize, type = shapeType.value
                                    )
                                )
                            }
                            tempStorageSize.value = ArrayList<DrawingPathRoute>().apply {
                                tempStorageSize.value?.let { addAll(it) }
                                set(
                                    lastIndex - 1, DrawingPathRoute.TopLeft(redrawLeft, redrawTop)
                                )
                                set(
                                    lastIndex, DrawingPathRoute.SizeTo(redrawRight, redrawBottom)
                                )
                            }
                        } else {
                            //サイズ変更
                            if (redrawTop != -1f && redrawLeft != -1f && vertexMode) {
                                leftSide =
                                    if (motionEvent.x > rightSide - 20f) rightSide - 20f else motionEvent.x
                                topSide =
                                    if (motionEvent.y > bottomSide - 20f) bottomSide - 20f else motionEvent.y
                            } else if (redrawRight != -1f && redrawBottom != -1f && vertexMode) {
                                rightSide =
                                    if (motionEvent.x < leftSide + 20f) leftSide + 20f else motionEvent.x
                                bottomSide =
                                    if (motionEvent.y < topSide + 20f) topSide + 20f else motionEvent.y
                            } else if (redrawTop != -1f && redrawRight != -1f && vertexMode) {
                                topSide =
                                    if (motionEvent.y > bottomSide - 20f) bottomSide - 20f else motionEvent.y
                                rightSide =
                                    if (motionEvent.x < leftSide + 20f) leftSide + 20f else motionEvent.x
                            } else if (redrawLeft != -1f && redrawBottom != -1f && vertexMode) {
                                leftSide =
                                    if (motionEvent.x > rightSide - 20f) rightSide - 20f else motionEvent.x
                                bottomSide =
                                    if (motionEvent.y < topSide + 20f) topSide + 20f else motionEvent.y
                            } else if (redrawLeft != -1f) {
                                leftSide =
                                    if (motionEvent.x > rightSide - 20f) rightSide - 20f else motionEvent.x
                            } else if (redrawTop != -1f) {
                                topSide =
                                    if (motionEvent.y > bottomSide - 20f) bottomSide - 20f else motionEvent.y
                            } else if (redrawRight != -1f) {
                                rightSide =
                                    if (motionEvent.x < leftSide + 20f) leftSide + 20f else motionEvent.x
                            } else if (redrawBottom != -1f) {
                                bottomSide =
                                    if (motionEvent.y < topSide + 20f) topSide + 20f else motionEvent.y
                            }
                            tracks.value = ArrayList<CustomDrawingPath>().apply {
                                tracks.value?.let { addAll(it) }
                                set(
                                    lastIndex - 1, CustomDrawingPath(
                                        path = DrawingPathRoute.TopLeft(leftSide, topSide),
                                        color = penColor,
                                        size = penSize,
                                        type = shapeType.value
                                    )
                                )
                                set(
                                    lastIndex, CustomDrawingPath(
                                        path = DrawingPathRoute.SizeTo(
                                            rightSide, bottomSide
                                        ), color = penColor, size = penSize, type = shapeType.value
                                    )
                                )
                            }
                            tempStorageSize.value = ArrayList<DrawingPathRoute>().apply {
                                tempStorageSize.value?.let { addAll(it) }
                                set(
                                    lastIndex - 1, DrawingPathRoute.TopLeft(leftSide, topSide)
                                )
                                set(
                                    lastIndex, DrawingPathRoute.SizeTo(rightSide, bottomSide)
                                )
                            }
                        }
                    }
                }
                //書き終わりの処理
                MotionEvent.ACTION_UP -> {
                    if (shapeType.value != ShapeType.BRUSH) {
                        if (tempStorageSize.value == null) {
                            //パスの整列
                            if (leftSide > motionEvent.x) {
                                rightSide = leftSide
                                leftSide = motionEvent.x
                            } else {
                                rightSide = motionEvent.x
                            }
                            if (topSide > motionEvent.y) {
                                bottomSide = topSide
                                topSide = motionEvent.y
                            } else {
                                bottomSide = motionEvent.y
                            }
                            tracks.value = ArrayList<CustomDrawingPath>().apply {
                                tracks.value?.let { addAll(it) }
                                set(
                                    lastIndex - 1, CustomDrawingPath(
                                        path = DrawingPathRoute.TopLeft(leftSide, topSide),
                                        color = penColor,
                                        size = penSize,
                                        type = shapeType.value
                                    )
                                )
                                set(
                                    lastIndex, CustomDrawingPath(
                                        path = DrawingPathRoute.SizeTo(rightSide, bottomSide),
                                        color = penColor,
                                        size = penSize,
                                        type = shapeType.value
                                    )
                                )
                            }

                            tempStorageSize.value = ArrayList<DrawingPathRoute>().apply {
                                tempStorageSize.value?.let { addAll(it) }
                                add(DrawingPathRoute.TopLeft(leftSide, topSide))
                                add(DrawingPathRoute.SizeTo(rightSide, bottomSide))
                            }
                        } else if (movementStartPosition == Offset(-1f, -1f)) {
                            vertexMode = false
                        } else {
                            movementStartPosition = Offset(-1f, -1f)
                            topSide = redrawTop
                            leftSide = redrawLeft
                            rightSide = redrawRight
                            bottomSide = redrawBottom
                        }
                        redrawTop = -1f
                        redrawLeft = -1f
                        redrawRight = -1f
                        redrawBottom = -1f
                    }
                }

                else -> false
            }
            true
        }) {
        val imageBitmap = ImageBitmap(width = size.width.toInt(), height = size.height.toInt())

        CanvasDrawScope().draw(
            density, layoutDirection, androidx.compose.ui.graphics.Canvas(imageBitmap), size
        ) {
            var currentPath = Path()
            var firstPath = Offset.Zero
            var top: Float
            var bottom: Float
            var left: Float

            if (tempStorageSize.value != null) {
                tracks.value?.lastOrNull()?.color = penColor
                tracks.value?.lastOrNull()?.size = penSize
                tracks.value?.lastOrNull()?.type = shapeType.value
            }
            clipRect {
                drawRect(
                    color = Color.White, size = size.copy(width = size.width, height = size.height)
                )
                tracks.let {
                    tracks.value?.forEach { customDrawingPath ->
                        when (customDrawingPath.path) {
                            is DrawingPathRoute.MoveTo -> {
                                currentPath = Path().apply {
                                    moveTo(customDrawingPath.path.x, customDrawingPath.path.y)
                                }
                            }

                            is DrawingPathRoute.LineTo -> {
                                drawPath(
                                    path = currentPath.apply {
                                        lineTo(customDrawingPath.path.x, customDrawingPath.path.y)
                                    },
                                    color = customDrawingPath.color,
                                    style = Stroke(width = customDrawingPath.size),
                                    blendMode = BlendMode.SrcOver
                                )
                            }

                            is DrawingPathRoute.TopLeft -> {
                                firstPath = Offset(
                                    customDrawingPath.path.x, customDrawingPath.path.y
                                )
                            }

                            is DrawingPathRoute.SizeTo -> {
                                left = if (firstPath.x > customDrawingPath.path.x) {
                                    customDrawingPath.path.x
                                } else {
                                    firstPath.x
                                }
                                if (firstPath.y > customDrawingPath.path.y) {
                                    top = customDrawingPath.path.y
                                    bottom = firstPath.y
                                } else {
                                    top = firstPath.y
                                    bottom = customDrawingPath.path.y
                                }
                                when (customDrawingPath.type) {
                                    ShapeType.ARC -> {
                                        drawOval(
                                            color = customDrawingPath.color,
                                            topLeft = Offset(left, top),
                                            size = Size(
                                                abs(customDrawingPath.path.x - firstPath.x),
                                                abs(customDrawingPath.path.y - firstPath.y)
                                            ),
                                            style = Stroke(width = customDrawingPath.size),
                                            blendMode = BlendMode.SrcOver
                                        )
                                    }

                                    ShapeType.TRIANGLE -> {
                                        val half = (customDrawingPath.path.x + firstPath.x) / 2f
                                        drawPath(
                                            path = Path().apply {
                                                moveTo(half, top)
                                                lineTo(firstPath.x, bottom)
                                                lineTo(customDrawingPath.path.x, bottom)
                                                lineTo(half, top)
                                            },
                                            color = customDrawingPath.color,
                                            style = Stroke(width = customDrawingPath.size),
                                            blendMode = BlendMode.SrcOver
                                        )
                                    }

                                    ShapeType.RECT -> {
                                        drawRect(
                                            color = customDrawingPath.color,
                                            topLeft = Offset(left, top),
                                            size = Size(
                                                abs(customDrawingPath.path.x - firstPath.x),
                                                abs(customDrawingPath.path.y - firstPath.y)
                                            ),
                                            style = Stroke(width = customDrawingPath.size),
                                            blendMode = BlendMode.SrcOver
                                        )
                                    }

                                    else -> {}
                                }
                            }
                        }
                    }
                    if (tempStorageSize.value?.lastIndex == 1) {
                        if (tempStorageSize.value != null) {
                            val dashedOffset =
                                Offset(tempStorageSize.value!![0].x, tempStorageSize.value!![0].y)
                            val dashedSize = Size(
                                tempStorageSize.value!![1].x - tempStorageSize.value!![0].x,
                                tempStorageSize.value!![1].y - tempStorageSize.value!![0].y
                            )
                            drawRect(
                                color = Color.White,
                                topLeft = dashedOffset,
                                size = dashedSize,
                                style = Stroke(width = 2.0f),
                                blendMode = BlendMode.SrcOver
                            )
                            drawRect(
                                color = Color.Black,
                                topLeft = dashedOffset,
                                size = dashedSize,
                                style = Stroke(
                                    width = 2.0f, pathEffect = PathEffect.dashPathEffect(
                                        intervals = floatArrayOf(20f, 10f), phase = 30f
                                    )
                                ),
                                blendMode = BlendMode.SrcOver
                            )
                        }
                    }
                }
            }
        }
        bitmap.value = imageBitmap.asAndroidBitmap()
    }
}
