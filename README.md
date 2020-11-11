# AndroidKtx

**Android 开发中最常用的扩展函数**

## 目录

- [File](https://github.com/dengzii/AndroidKtx#file)
- [View](https://github.com/dengzii/AndroidKtx#View)
- [Context](https://github.com/dengzii/AndroidKtx#Context)
- [Activity](https://github.com/dengzii/AndroidKtx#Activity)
- [Intent](https://github.com/dengzii/AndroidKtx#Intent)
- [Bitmap](https://github.com/dengzii/AndroidKtx#Bitmap)
- [Uri](https://github.com/dengzii/AndroidKtx#Uri)
- [SharedPreferences](https://github.com/dengzii/AndroidKtx#SharedPreferences)

## Sample

### File

```kotlin
// 重命名文件
fun File.rename(newName: String): Boolean
// 获取或创建文件
fun File.createOrExistsFile(): Boolean
// 获取文件 md5
fun File.md5(): ByteArray?
```

### View

- 点击防抖, 默认 300ms
```kotlin
 fun <T : View> T.antiShakeClick(
    clickInterval: Long = viewShakeClickInterval,
    onClickListener: (View) -> Unit
)
```
- 遍历子 View
```kotlin
fun ViewGroup.forEachIndexed(action: (Int, View) -> Unit)
```
- 隐藏显示, 反转可见性, 反转可点击
```kotlin
fun View.gone()
fun View.show()
fun View.hide()
fun View.toggleVisible()
fun View.toggleEnable()
```
- 转换成 Bitmap
```kotlin
fun View.toBitmap(config: Bitmap.Config): Bitmap
```
- TextView 监听
```kotlin
mEtInput.addTextWatcher {
    onChange { s, start, before, count ->

    }
    afterChange {

    }
}
```
- 设置 Drawable
```kotlin
fun TextView.setDrawableEnd(@DrawableRes drawableEnd: Int)
```
- 快速设置 ColorStateList, 各种状态的颜色
```kotlin
fun TextView.setTextColorStateList(block: ViewStateBuilder.() -> Unit)
```

### Context

- 获取资源
```kotlin
val color = getColorCompat(R.color.colorPrimary)
val drawable = getDrawableCompat(R.drawable.ic_launcher_foreground)
```
- 检查权限
```kotlin
val canReadStorage = isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)
```
- UI 相关
```kotlin
// 获取状态栏高度
fun Context.getStatusBarHeight():Int
// 获取屏幕高度
fun Context.getScreenHeight(): Int
```

### Activity

- 通过委托延迟绑定 View 到字段
```kotlin
private val mBtSubmit by lazyFindView<Button>(R.id.bt_submit) 
```
- 通过委托延迟获取 Intent 字段
```kotlin
private val mExtOrderId by intentExtra<Int>("ext_order_id", -1)
```
- 无参数快速启动Activity
```kotlin
startActivity<SecondActivity>()
```
- 带参数启动 Activity
```kotlin
startActivity<SecondActivity> {
    putExtra("id", 1)
}
```
- 快速获取 ActivityResult
```kotlin
startActivityForResult<SecondActivity>(1) { requestCode, resultCode, data ->
    // TODO
}
val intent = Intent(this, SecondActivity::class.java).run {
    putExtra("id", 1)
}
startActivityForResult(intent, 0){ requestCode, resultCode, data ->
    // TODO
}
```
- 快速从系统选择文件
```kotlin
requestSelectFile("image/*") {
    println("image: " + it?.path)
}
```

### Intent

- 检查键是否存在
```kotlin
intent.checkExtraExists("Key1", "Key2", "Key3") {
    println("Key $it does not exist.")
}
```
- 默认值
```kotlin
intent.getStringExtraOrDefault("Key", "DefaultValue")
```

### Bitmap

```kotlin
// 转换为 Drawable
fun Bitmap.toDrawable(): Bitmap
// 转换为圆角
fun Bitmap.toRound( borderSize: Int, borderColor: Int = 0, recycle: Boolean): Bitmap
// 保存到文件
fun Bitmap.saveTo(file: File, format: Bitmap.CompressFormat, quality: Int): Boolean
// 保存到相册
fun Bitmap.saveToAlbum(format: Bitmap.CompressFormat, quality: Int): Boolean
// 模糊
fun Bitmap.blur(radius: Int, recycle: Boolean): Bitmap
```

### Uri

- 从 Uri 获取绝对路径
```kotlin
fun Uri.getRealPath(context: Context): String?
```

### SharedPreferences

- 便捷配置和操作 SharedPreferences
```kotlin
import android.content.Context
import com.dengzii.ktx.android.content.Preferences
import com.dengzii.ktx.android.content.preference
import com.dengzii.ktx.android.content.update

class AppConfig(context: Context) : Preferences(context, "spf_app_config") {
    var userName:String by preference("Tom")
    var age by preference(10)
    var weight by preference(200f)
    var hobbits by preference(mutableSetOf("volleyball", "codding"))
}
fun sample(context: Context){
    val appConfig = AppConfig(context).update {
        userName = "Tome"
        age = 18
    }
    println(appConfig.weight) // 10
    println(appConfig.age) // 18
    appConfig.hobbits.add("eat")
    appConfig.commit()
}
```
