package com.example.resizinglayout

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.math.roundToInt

class ResizeListener : View.OnTouchListener {
    private var initialWidth = 0
    private var initialHeight = 0
    private var initialTouchX = 0f
    private var initialTouchY = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        val binding = FloatingViewService.instance.getFloatingViewBinding()
        val params = binding.floatingView.layoutParams as ConstraintLayout.LayoutParams
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                initialWidth = params.width
                initialHeight = params.height
                initialTouchX = event.rawX
                initialTouchY = event.rawY
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                params.width = (initialWidth + (event.rawX - initialTouchX)).roundToInt()
                params.height = (initialHeight + (event.rawY - initialTouchY)).roundToInt()
                binding.floatingView.layoutParams = params
                return true
            }

            MotionEvent.ACTION_UP -> {
                return true
            }
        }
        return false
    }
}


