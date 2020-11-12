package com.dengzii.ktx.android

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.*
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.RequiresApi
import com.dengzii.ktx.ContextUtils
import com.dengzii.ktx.android.content.getColorCompat

/**
 * Set span with range.
 */
fun Spannable.setSpan(what: Any, range: IntRange) {
    setSpan(what, range.first, range.last, SpannableString.SPAN_INCLUSIVE_INCLUSIVE)
}

fun Spannable.fgColorSpan(@ColorInt color: Int, range: IntRange) {
    setSpan(ForegroundColorSpan(color), range)
}

fun Spannable.bgColorSpan(@ColorInt color: Int, range: IntRange) {
    setSpan(BackgroundColorSpan(color), range)
}

fun Spannable.absSizeSpan(@androidx.annotation.IntRange(from = 0) px: Int, range: IntRange) {
    setSpan(AbsoluteSizeSpan(px), range)
}

fun Spannable.relativeSizeSpan(@FloatRange(from = 0.0) px: Float, range: IntRange) {
    setSpan(RelativeSizeSpan(px), range)
}

fun Spannable.underlineSpan(range: IntRange) {
    setSpan(UnderlineSpan(), range)
}

fun Spannable.urlSpan(url: String, range: IntRange) {
    setSpan(URLSpan(url), range)
}

@RequiresApi(Build.VERSION_CODES.P)
fun Spannable.typefaceSpan(typeface: Typeface, range: IntRange) {
    setSpan(TypefaceSpan(typeface), range)
}

fun Spannable.typefaceSpan(family: String, range: IntRange) {
    setSpan(TypefaceSpan(family), range)
}

fun Spannable.imageSpan(drawable: Drawable, range: IntRange){
    setSpan(ImageSpan(drawable), range)
}

/**
 * Append a [ClickableSpan] simply.
 * @param range The span range.
 * @param color The span color.
 * @param underLine Weather display span underline.
 * @param textView The [TextView] which set the SpannableString.
 * @param clickListener The span click callback.
 */
fun Spannable.clickableSpan(
    range: IntRange,
    @ColorInt color: Int,
    underLine: Boolean = false,
    textView: TextView,
    clickListener: (View) -> Unit
) {
    textView.movementMethod = LinkMovementMethod()
    textView.setHintTextColor(ContextUtils.getApp().getColorCompat(android.R.color.transparent))
    setSpan(XClickableSpan(color, underLine, clickListener), range)
}

fun Spannable.textAppearanceSpan(
    family: String,
    style: Int,
    size: Int,
    colorStateList: ColorStateList,
    linkColor: ColorStateList,
    range: IntRange
) {
    setSpan(TextAppearanceSpan(family, style, size, colorStateList, linkColor), range)
}

internal class XClickableSpan(
    @ColorInt private val color: Int,
    private val underLine: Boolean,
    private val clickListener: (View) -> Unit
) : ClickableSpan() {

    override fun onClick(widget: View) {
        clickListener(widget)
    }

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.color = color
        ds.isUnderlineText = underLine
    }
}