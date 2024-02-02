package jp.co.abs.paintkamada2.ui.theme

import android.annotation.SuppressLint
import android.content.ContentValues
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import jp.co.abs.paintkamada2.CustomDrawingPath
import jp.co.abs.paintkamada2.DrawingPathRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

@SuppressLint("SimpleDateFormat")
@Composable
fun SettingsTopAppBar(
    tracks: MutableState<List<CustomDrawingPath>?>,
    bitmap: MutableState<Bitmap?>,
    tempStorageSize: MutableState<List<DrawingPathRoute>?>,
    saveJudge: Boolean
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val downloadPath = File(Environment.getExternalStorageDirectory().path + "/Kamada_Picture")
    Row(
        modifier = Modifier
            .drawBehind {
                drawRect(
                    color = Color.LightGray,
                    size = size.copy(width = size.width, height = size.height)
                )
            }
    ) {
        Button(
            onClick = {
                tracks.value = null
                tempStorageSize.value = null
            },
            modifier = Modifier.weight(1f, fill = true),
            shape = MaterialTheme.shapes.small
        ) {
            Text(text = AnnotatedString("CLEAR"))
        }
        Button(
            onClick = {
                tempStorageSize.value = null
                if (saveJudge) {
                    if (bitmap.value != null) {
                        //別スレッド起動
                        coroutineScope.launch(Dispatchers.IO) {
                            try {
                                val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
                                val current = sdf.format(Date())
                                // 保存先のファイル作成
                                val fileName = "$current.png"
                                val fos = FileOutputStream(File(downloadPath, fileName))
                                // ファイルに書き込み
                                //PNG形式で出力
                                bitmap.value?.compress(Bitmap.CompressFormat.PNG, 100, fos)
                                fos.close()

                                // 処理が終わったら、メインスレッドに切り替える。
                                withContext(Dispatchers.Main) {
                                    // ContentResolver にファイル情報の書き込み。
                                    val newValues = ContentValues().apply {
                                        put(MediaStore.Images.Media.DATA, "$downloadPath/$fileName")
                                        put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                                    }
                                    context.applicationContext.contentResolver.insert(
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        newValues
                                    )

                                    Toast.makeText(context, "画像を保存しました", Toast.LENGTH_SHORT).show()
                                }

                            } catch (e: FileNotFoundException) {
                                e.printStackTrace()
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }
                    } else {
                        Toast.makeText(context, "画像取得に失敗しました", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "画像を確定しました", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.weight(1f, fill = true),
            shape = MaterialTheme.shapes.small
        ) {
            Text(text = AnnotatedString("SAVE"))
        }
    }
}