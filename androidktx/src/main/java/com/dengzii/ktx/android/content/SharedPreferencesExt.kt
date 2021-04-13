package com.dengzii.ktx.android.content

/**
 * Return a property delegate class instance of [Preferences.PreferenceDelegate], the getter and setter of
 * field will synchronized with [SharedPreferences].
 *
 * @param T The type of preference, be consistent with the type supported by [SharedPreferences].
 * @param defaultValue The default value when preference not found.
 * @param keyName The key name of preference, if null, the delegate property's name will be used.
 */
inline fun <reified T : Any?> Preferences.preference(
    defaultValue: T,
    keyName: String? = null
): Preferences.PreferenceDelegate<T> {
    return when (defaultValue) {
        is Int?,
        is Boolean?,
        is Float?,
        is String?,
        is Long?,
        is Set<*>? -> {
            PreferenceDelegate<T>(defaultValue, T::class, keyName)
        }
        else -> throw RuntimeException("Unsupported type ${T::class}")
    }
}

/**
 * Update [Preferences] in function [action] and auto commit changes finally.
 *
 * @param action The function with `this` as its receiver
 */
inline fun <T : Preferences> T.update(action: T.() -> Unit): T {
    action()
    commit()
    return this
}

/**
 * Calls the specified function [action] with `this` value as its receiver and returns `this` value.
 */
inline fun <T : Preferences> T.use(action: T.() -> Unit): T {
    action()
    return this
}