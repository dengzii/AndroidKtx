# AndroidKtx

**Android 开发中最常用的扩展函数**

## Sample

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
fun Context.getStatusBarHeight():Int    // 获取状态栏高度
fun Context.getScreenHeight(): Int  // 获取屏幕高度
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

### View

- TextView 监听
```kotlin
mEtInput.addTextWatcher { 
    onChange { s, start, before, count -> 
        
    }
    afterChange { 
        
    }
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

### Uri

- 从 Uri 获取绝对路径
```kotlin
fun Uri.getRealPath(context: Context): String?
```

### SharedPreferences

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
