@file:Suppress("NOTHING_TO_INLINE")

package com.dengzii.ktx.android

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.widget.TextViewCompat
import com.dengzii.ktx.android.content.getColorCompat
import com.dengzii.ktx.android.content.getDrawableCompat

class TextChangeWatcher {
    var after: ((Editable?) -> Unit)? = null
    var before: ((CharSequence?, Int, Int, Int) -> Unit)? = null
    var on: ((CharSequence?, Int, Int, Int) -> Unit)? = null

    fun afterChange(block: (Editable?) -> Unit) {
        after = block
    }

    fun beforeChange(block: (s: CharSequence?, start: Int, count: Int, after: Int) -> Unit) {
        before = block
    }

    fun onChange(block: (s: CharSequence?, start: Int, before: Int, count: Int) -> Unit) {
        on = block
    }
}

inline fun TextView.addTextWatcher(action: TextChangeWatcher.() -> Unit) {
    val watcher = TextChangeWatcher()
    action(watcher)
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            watcher.after?.invoke(s)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            watcher.before?.invoke(s, start, count, after)
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            watcher.on?.invoke(s, start, before, count)
        }
    })
}

inline fun TextView.setTextColorStateList(block: ViewStateBuilder.() -> Unit) {
    val viewStatesBuilder = ViewStateBuilder()
    block.invoke(viewStatesBuilder)
    setTextColor(viewStatesBuilder.toColorStateList(context))
}

inline fun TextView.setCompoundDrawableTintList(block: ViewStateBuilder.() -> Unit) {
    val viewStatesBuilder = ViewStateBuilder()
    block.invoke(viewStatesBuilder)
    setCompoundDrawableTintListCompat(viewStatesBuilder.toColorStateList(context))
}

inline fun TextView.setTextColorCompat(@ColorRes resId: Int) {
    setTextColor(context.getColorCompat(resId))
}

inline fun TextView.setHintTextColorCompat(@ColorRes resId: Int) {
    setHintTextColor(context.getColorCompat(resId))
}

inline fun TextView.setDrawableStart(drawableStart: Drawable) {
    val drawable = getCompoundDrawablesRelativeCompat()
    setDrawablesRelativeWithBoundsCompat(drawableStart, drawable[1], drawable[2], drawable[3])
}

inline fun TextView.setDrawableEnd(drawableEnd: Drawable) {
    val drawable = getCompoundDrawablesRelativeCompat()
    setDrawablesRelativeWithBoundsCompat(drawable[0], drawable[1], drawableEnd, drawable[3])
}

inline fun TextView.setDrawableStart(@DrawableRes drawableStart: Int) {
    val drawable = getCompoundDrawablesRelativeCompat()
    setDrawablesRelativeWithBoundsCompat(
        context.getDrawableCompat(drawableStart),
        drawable[1],
        drawable[2],
        drawable[3]
    )
}

inline fun TextView.setDrawableEnd(@DrawableRes drawableEnd: Int) {
    val drawable = getCompoundDrawablesRelativeCompat()
    setDrawablesRelativeWithBoundsCompat(
        drawable[0],
        drawable[1],
        context.getDrawableCompat(drawableEnd),
        drawable[3]
    )
}

inline fun TextView.setDrawablesRelativeWithBoundsCompat(
    start: Drawable?, top: Drawable?, end: Drawable?,
    bottom: Drawable?
) {
    TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(this, start, top, end, bottom)
}

inline fun TextView.getCompoundDrawablesRelativeCompat(): Array<Drawable?> {
    return TextViewCompat.getCompoundDrawablesRelative(this)
}

inline fun TextView.setCompoundDrawableTintListCompat(tint: ColorStateList?) {
    TextViewCompat.setCompoundDrawableTintList(this, tint)
}