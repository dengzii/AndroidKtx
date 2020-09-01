package com.dengzii.ktx.android

import android.content.Context
import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * author : dengzi.
 * e-mail : master@dengzii.com
 * time   : 2020/9/1
 * desc   : SharedPreferences delegate class.
 */
open class Preferences(sharedPreferences: SharedPreferences) :
    SharedPreferences by sharedPreferences {

    val editor: SharedPreferences.Editor by lazy { edit() }

    companion object {
        const val DEFAULT_PREFERENCES_NAME = "shared_preferences"
    }

    constructor(context: Context, name: String = DEFAULT_PREFERENCES_NAME)
            : this(context.getSharedPreferences(name, Context.MODE_PRIVATE))

    inner class Preference<T>(private val default: T) :
        ReadWriteProperty<SharedPreferences, T> {

        override fun getValue(thisRef: SharedPreferences, property: KProperty<*>): T {
            val type = property.toString().removeSuffix("?")
            val keyName = property.name
            if (!thisRef.contains(keyName)) {
                return default
            }
            @Suppress("UNCHECKED_CAST")
            return with(thisRef) {
                when (type) {
                    "kotlin.Int" -> getInt(keyName, 0)
                    "kotlin.Boolean" -> getBoolean(keyName, false)
                    "kotlin.String" -> getString(keyName, "")
                    "kotlin.Float" -> getFloat(keyName, 0F)
                    "kotlin.Long" -> getLong(keyName, 0L)
                    else -> throw RuntimeException("Type $type is unsupported.")
                } as T
            }
        }

        override fun setValue(thisRef: SharedPreferences, property: KProperty<*>, value: T) {
            val keyName = property.name
            with(editor) {
                when (value) {
                    is Int -> putInt(keyName, value)
                    is Boolean -> putBoolean(keyName, value)
                    is String -> putString(keyName, value)
                    is Float -> putFloat(keyName, value)
                    is Long -> putLong(keyName, value)
                    null -> remove(keyName)
                    else -> throw RuntimeException("Type of $value is unsupported.")
                }
            }
            editor.commit()
        }
    }
}
