@file:Suppress("NOTHING_TO_INLINE")

package com.dengzii.ktx.android

import android.content.res.ColorStateList
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.res.ColorStateListInflaterCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.ImageViewCompat

inline fun View.hide() {
    visibility = View.INVISIBLE
}

inline fun View.gone() {
    visibility = View.GONE
}

inline fun View.show() {
    visibility = View.VISIBLE
}

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

inline fun ImageView.setImageTintListCompat(stateList: ColorStateList?){
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