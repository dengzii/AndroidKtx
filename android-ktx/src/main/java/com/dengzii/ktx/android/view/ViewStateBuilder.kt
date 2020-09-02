package com.dengzii.ktx.android.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.StateListDrawable
import com.dengzii.ktx.android.filterNullValue
import com.dengzii.ktx.android.content.getColorCompat
import com.dengzii.ktx.android.content.getDrawableCompat

/**
 * author : dengzi
 * e-mail : master@dengzii.com
 * time   : 2020/8/24
 * desc   : none
 *
 */
class ViewStateBuilder {

    var stateNormal: Int? = null
    var statePressed: Int? = null
    var stateDisabled: Int? = null
    var stateFocused: Int? = null

    fun toColorStateList(context: Context): ColorStateList {
        val stateMap = toMap()
        val states = stateMap.keys.toTypedArray()
        val colors = stateMap.values.map {
            context.getColorCompat(it)
        }.toIntArray()
        return ColorStateList(states, colors)
    }

    fun toDrawableStateList(context: Context): StateListDrawable {
        val stateList = StateListDrawable()
        toMap().forEach {
            stateList.addState(it.key, context.getDrawableCompat(it.value))
        }
        return stateList
    }

    private fun toMap() = mapOf(
        Pair(intArrayOf(android.R.attr.state_pressed), statePressed),
        Pair(intArrayOf(android.R.attr.state_focused), stateFocused),
        Pair(intArrayOf(android.R.attr.state_enabled), stateNormal),
        Pair(intArrayOf(), stateDisabled)
    ).filterNullValue()
}