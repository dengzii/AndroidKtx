@file:Suppress("NOTHING_TO_INLINE")

package com.dengzii.ktx.android

import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.widget.ImageViewCompat
import com.dengzii.ktx.android.content.getColorCompat
import com.dengzii.ktx.android.content.getDrawableCompat
import com.dengzii.ktx.currentTimeMillis

var viewShakeClickInterval = 300L

inline var <T : View> T.lastClickTime: Long
    get() = getTag(R.id.tag_view_last_click_time) as? Long ?: Long.MAX_VALUE
    set(value) = setTag(R.id.tag_view_last_click_time, value)

/**
 * Set ClickListener with anti-shake.
 * @param clickInterval the interval of shake.
 * @param onClickListener the onClickListener.
 */
inline fun <T : View> T.antiShakeClick(
    clickInterval: Long = viewShakeClickInterval,
    crossinline onClickListener: (View) -> Unit
) {
    setOnClickListener {
        val isShakeClick = currentTimeMillis - lastClickTime < clickInterval
        if (isShakeClick) return@setOnClickListener
        lastClickTime = currentTimeMillis
        onClickListener(it)
    }
}

inline fun ViewGroup.contains(child: View): Boolean {
    return indexOfChild(child) != -1
}

inline fun ViewGroup.forEach(action: (View) -> Unit) {
    for (i in 0 until childCount) {
        action(getChildAt(i))
    }
}

inline fun ViewGroup.forEachIndexed(action: (Int, View) -> Unit) {
    for (i in 0 until childCount) {
        action(i, getChildAt(i))
    }
}

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

inline fun View.toggleVisible() {
    visibility = when (visibility) {
        View.VISIBLE -> View.INVISIBLE
        View.INVISIBLE -> View.VISIBLE
        else -> View.GONE
    }
}

inline fun View.toggleEnable() {
    isEnabled = !isEnabled
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
 * @param measureSpec The measure spec.
 * @return The pair of mode, size.
 */
inline fun View.resolveMeasureSpec(measureSpec: Int): Pair<Int, Int> {
    return Pair(View.MeasureSpec.getMode(measureSpec), View.MeasureSpec.getSize(measureSpec))
}

/**
 * Set [LinearLayout.LayoutParams] as View's layoutParams, return action scope.
 * @param block Setup your layoutParam here.
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

/**
 * Drawing view into Bitmap
 */
inline fun View.toBitmap(config: Bitmap.Config = Bitmap.Config.ARGB_8888): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, config)
    val canvas = Canvas(bitmap)
    canvas.translate(-scrollX.toFloat(), -scrollY.toFloat())
    draw(canvas)
    return bitmap
}

/**
 * Start an alphaAnimation, from view's alpha into 1.0f.
 * @param duration how long should the animation last.
 */
inline fun View.fadeIn(duration: Long) {
    this.clearAnimation()
    val anim = AlphaAnimation(this.alpha, 1.0f)
    anim.duration = duration
    this.startAnimation(anim)
}

/**
 * Start an alphaAnimation, from view's alpha down to 0.5.
 * @param duration how long should the animation last.
 */
inline fun View.fadeOut(duration: Long) {
    this.clearAnimation()
    val anim = AlphaAnimation(this.alpha, 0.5f)
    anim.duration = duration
    this.startAnimation(anim)
}