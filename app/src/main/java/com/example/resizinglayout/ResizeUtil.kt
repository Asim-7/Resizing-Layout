package com.example.resizinglayout

import android.content.res.Resources
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import kotlin.math.roundToInt

object ResizeUtil {

    var originalHeight = 150.dpToPx()

    fun resizeLayout(scaleFactor: Float) {
        with(FloatingViewService.instance.getFloatingViewBinding()) {
            val value2 = (2.dpToPx() * scaleFactor).roundToInt()
            val value18 = (18.dpToPx() * scaleFactor).roundToInt()
            val value20 = (20.dpToPx() * scaleFactor).roundToInt()
            val value30 = (30.dpToPx() * scaleFactor).roundToInt()

            listOf(closeView, rightScaleView, leftScaleView).forEach {
                it.updateLayoutParams {
                    width = value20
                    height = value20
                }
            }
            container.updateLayoutParams {
                height = value30
            }
            listOf(textOne, textTwo, textThree, textFour, textFive).forEach {
                it.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    width = value18
                    height = value30
                    setMargins(value2, value2, value2, value2)
                }
                it.textSize = 3.dpToPx() * scaleFactor
            }
        }
    }

    private fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

}