package jp.co.abs.paintkamada2

import androidx.compose.ui.graphics.Color
import jp.co.abs.paintkamada2.ui.theme.ShapeType
import java.io.Serializable

// 描画の記録するためにpathを表現する
sealed class DrawingPathRoute : Serializable {
    abstract val x: Float
    abstract val y: Float

    data class MoveTo(override val x: Float, override val y: Float) : DrawingPathRoute()
    data class LineTo(override val x: Float = 0f, override val y: Float = 0f) : DrawingPathRoute()
    data class TopLeft(override val x: Float, override val y: Float) : DrawingPathRoute()
    data class SizeTo(override val x: Float = 0f, override val y: Float = 0f) : DrawingPathRoute()
}

//シリアライズして、データを格納
data class CustomDrawingPath(
    var path: DrawingPathRoute,
    var color: Color,
    var size: Float,
    var type: ShapeType
) : Serializable