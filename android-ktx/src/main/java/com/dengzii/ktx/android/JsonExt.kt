package com.dengzii.ktx.android

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

fun JSONObject.getBoolean(key: String, default: Boolean = false): Boolean = getValue(key, default)

fun JSONObject.getInt(key: String, default: Int = -1): Int = getValue(key, default)

fun JSONObject.getString(key: String, default: String = ""): String = getValue(key, default)

fun JSONObject.getDouble(key: String, default: Double = -1.0): Double = getValue(key, default)

fun JSONObject.getLong(key: String, default: Long = -1): Long = getValue(key, default)

fun JSONObject.getJSONObject(key: String, default: JSONObject): JSONObject = getValue(key, default)

fun JSONObject.getJSONArray(key: String, default: JSONArray): JSONArray = getValue(key, default)

@Suppress("IMPLICIT_CAST_TO_ANY")
private inline fun <reified T> JSONObject.getValue(key: String, default: T): T {
    return try {
        getValue<T>(key) ?: default
    } catch (e: JSONException) {
        e.printStackTrace()
        default
    }
}

@Suppress("IMPLICIT_CAST_TO_ANY")
@Throws(JSONException::class, ClassCastException::class)
private inline fun <reified T> JSONObject.getValue(key: String): T? {
    if (key.isEmpty()) {
        return null
    }
    return when (T::class.java) {
        Boolean::class.java -> getBoolean(key)
        Int::class.java -> getInt(key)
        String::class.java -> getString(key)
        Double::class.java -> getDouble(key)
        Long::class.java -> getLong(key)
        JSONObject::class.java -> getJSONObject(key)
        JSONArray::class.java -> getJSONArray(key)
        else -> null
    } as? T
}
