package com.dengzii.ktx.android

import android.content.Context
import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * author : dengzi.
 * e-mail : master@dengzii.com
 * time   : 2020/9/1
 * desc   : SharedPreferences delegate class.
 */
open class Preferences(sharedPreferences: SharedPreferences) :
    SharedPreferences by sharedPreferences {

    private val mEditor: SharedPreferences.Editor by lazy { edit() }

    companion object {
        const val DEFAULT_PREFERENCES_NAME = "shared_preferences"
    }

    constructor(context: Context, name: String = DEFAULT_PREFERENCES_NAME)
            : this(context.getSharedPreferences(name, Context.MODE_PRIVATE))

    inner class Preference<T : Any?> constructor(
        default: T,
        clazz: KClass<*>,
        keyName: String? = null
    ) :
        ReadWriteProperty<SharedPreferences, T> {

        private val mDefault: T = default
        private val mKeyName: String? = keyName
        private val mClazz: KClass<*> = clazz

        @Suppress("UNCHECKED_CAST")
        override fun getValue(thisRef: SharedPreferences, property: KProperty<*>): T {
            val keyName = mKeyName ?: property.name
            if (!thisRef.contains(keyName)) {
                return mDefault
            }
            val value = thisRef.all.getOrDefaultCompat(keyName, null) ?: return mDefault
            return if (value::class == mClazz) {
                value as T
            } else {
                mDefault
            }
        }

        override fun setValue(thisRef: SharedPreferences, property: KProperty<*>, value: T) {
            val keyName = property.name
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
                    else -> throw RuntimeException("Type is unsupported.")
                }
            }
            mEditor.commit()
        }
    }
}
