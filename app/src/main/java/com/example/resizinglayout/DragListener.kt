package com.example.resizinglayout

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import kotlin.math.roundToInt

class DragListener : View.OnTouchListener {

    private var initialX = 0
    private var initialY = 0
    private var initialTouchX = 0f
    private var initialTouchY = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        val params = view.layoutParams as WindowManager.LayoutParams
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                initialX = params.x
                initialY = params.y
                initialTouchX = event.rawX
                initialTouchY = event.rawY
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                params.x = (initialX + (event.rawX - initialTouchX)).roundToInt()
                params.y = (initialY + (event.rawY - initialTouchY)).roundToInt()
                FloatingViewService.instance.updateFloatingView(params)
                return true
            }

            MotionEvent.ACTION_UP -> {
                return true
            }
        }
        return false
    }

}