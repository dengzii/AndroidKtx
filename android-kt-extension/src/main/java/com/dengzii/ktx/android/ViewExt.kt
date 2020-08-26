@file:Suppress("NOTHING_TO_INLINE")

package com.dengzii.ktx.android

import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.widget.ImageViewCompat

/**
 * Set View's visibility to [View.INVISIBLE]
 */
inline fun View.hide() {
    visibility = View.INVISIBLE
}

/**
 * Set View's visibility to [View.GONE]
 */
inline fun View.gone() {
    visibility = View.GONE
}
/**
 * Set View's visibility to [View.VISIBLE]
 */
inline fun View.show() {
    visibility = View.VISIBLE
}

/**
 * Simplify [Context.obtainStyledAttributes] to action scope.
 */
inline fun View.withStyledAttrs(
    attrs: AttributeSet?,
    styleArray: IntArray,
    defStyle: Int,
    block: TypedArray.() -> Unit
) {
    val typeArray = context.obtainStyledAttributes(attrs, styleArray, defStyle, 0)
    block(typeArray)
    typeArray.recycle()
}

/**
 * Resolve measure spec to pair of spec mode and size.
 * @param measureSpec The measure spec
 * @return The pair of mode, size
 */
inline fun View.resolveMeasureSpec(measureSpec: Int): Pair<Int, Int> {
    return Pair(View.MeasureSpec.getMode(measureSpec), View.MeasureSpec.getSize(measureSpec))
}

/**
 * Set [LinearLayout.LayoutParams] as View's layoutParams, return action scope.
 */
inline fun View.setLinearLayoutParam(block: LinearLayout.LayoutParams.() -> Unit) {
    val linearLayoutParam = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    block(linearLayoutParam)
    layoutParams = linearLayoutParam
}

inline fun View.setFrameLayoutParam(block: FrameLayout.LayoutParams.() -> Unit) {
    val frameLayoutParam = FrameLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    block(frameLayoutParam)
    layoutParams = frameLayoutParam
}

inline fun View.setBackgroundColorCompat(@ColorRes resId: Int) {
    setBackgroundColor(context.getColorCompat(resId))
}

inline fun ImageView.setImageDrawableCompat(@DrawableRes resId: Int) {
    setImageDrawable(context.getDrawableCompat(resId))
}

inline fun ImageView.setImageTintListCompat(stateList: ColorStateList?) {
    ImageViewCompat.setImageTintList(this, stateList)
}

inline fun ImageView.setImageTintColor(@ColorRes resId: Int) {
    val stateList = ColorStateList.valueOf(context.getColorCompat(resId))
    setImageTintListCompat(stateList)
}

inline fun ImageView.setImageTintList(block: ViewStateBuilder.() -> Unit) {
    val viewStatesBuilder = ViewStateBuilder()
    block.invoke(viewStatesBuilder)
    setImageTintListCompat(viewStatesBuilder.toColorStateList(context))
}