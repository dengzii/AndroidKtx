package com.dengzii.ktx.android.content

import android.content.Context
import android.content.SharedPreferences
import com.dengzii.ktx.android.getOrDefaultCompat
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * author : dengzi.
 * e-mail : master@dengzii.com
 * time   : 2020/9/1
 */
open class Preferences(sharedPreferences: SharedPreferences) :
        SharedPreferences by sharedPreferences {

    private val mEditor: SharedPreferences.Editor by lazy { edit() }
    private val mCache = mutableMapOf<String, Any?>()

    companion object {
        const val DEFAULT_PREFERENCES_NAME = "shared_preferences"
    }

    constructor(context: Context, name: String = DEFAULT_PREFERENCES_NAME)
            : this(context.getSharedPreferences(name, Context.MODE_PRIVATE))

    inner class PreferenceDelegate<T : Any?> constructor(
            default: T,
            clazz: KClass<*>,
            keyName: String? = null,
            private val cache: Boolean = true
    ) : ReadWriteProperty<SharedPreferences, T> {

        private val mDefault: T = default
        private val mKeyName: String? = keyName
        private val mClazz: KClass<*> = clazz

        @Suppress("UNCHECKED_CAST")
        override fun getValue(thisRef: SharedPreferences, property: KProperty<*>): T {
            val keyName = mKeyName ?: property.name
            if (cache && mCache.containsKey(keyName)) {
                val ret = mCache[keyName] as T
                if (ret != null) {
                    return ret
                }
            }
            val ret = if (!thisRef.contains(keyName)) {
                mDefault
            } else {
                val value = thisRef.all.getOrDefaultCompat(keyName, null) ?: return mDefault
                if (value::class == mClazz) {
                    value as? T ?: mDefault
                } else {
                    mDefault
                }
            }
            mCache[keyName] = ret as? Any
            return ret
        }

        override fun setValue(thisRef: SharedPreferences, property: KProperty<*>, value: T) {
            val keyName = mKeyName ?: property.name
            with(mEditor) {
                when (value) {
                    is Int -> putInt(keyName, value)
                    is Boolean -> putBoolean(keyName, value)
                    is String -> putString(keyName, value)
                    is Float -> putFloat(keyName, value)
                    is Long -> putLong(keyName, value)
                    is Set<*> -> {
                        val set = value.filterIsInstance<String>().toSet()
                        putStringSet(keyName, set)
                    }
                    null -> remove(keyName)
                    else -> throw RuntimeException("Type unsupported.")
                }
            }
        }
    }

    fun commit() {
        mEditor.commit()
    }
}
