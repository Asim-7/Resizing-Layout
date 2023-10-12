package com.example.resizinglayout

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import com.example.resizinglayout.MainActivity.Companion.isServiceRunning
import com.example.resizinglayout.databinding.FloatingViewBinding

class FloatingViewService : Service() {

    private lateinit var windowManagerApp: WindowManager
    private lateinit var binding: FloatingViewBinding

    override fun onCreate() {
        super.onCreate()

        instance = this
        windowManagerApp = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        binding = FloatingViewBinding.inflate(LayoutInflater.from(this))

        with(binding) {
            binding.root.setOnTouchListener(DragListener())
            closeView.setOnClickListener {
                destroyService()
            }
            rightScaleView.setOnTouchListener(ResizeListener())
            leftScaleView.setOnTouchListener(ResizeListener())
        }
    }

    fun getFloatingViewBinding(): FloatingViewBinding {
        return binding
    }

    private fun showFloatingView() {
        isServiceRunning = true
        addInWindow(binding.root, getCommonLayoutParams())
    }

    private fun addInWindow(targetView: View, targetParams: WindowManager.LayoutParams) {
        windowManagerApp.addView(targetView, targetParams)
        targetView.visibility = View.VISIBLE
    }

    fun updateFloatingView(layoutParams: WindowManager.LayoutParams) {
        windowManagerApp.updateViewLayout(binding.root, layoutParams)
    }

    private fun getCommonLayoutParams(): WindowManager.LayoutParams {
        val layoutFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_PHONE
        return WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutFlag,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
    }

    override fun onBind(p0: Intent?): IBinder? {
        return Messenger(IncomingHandler()).binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    fun destroyService() {
        try {
            binding.root.visibility = View.GONE
            windowManagerApp.removeViewImmediate(binding.root)
        } catch (e: Exception) {
        }
        stopSelf()
        isServiceRunning = false
    }

    internal class IncomingHandler : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                1 -> {
                    instance.showFloatingView()
                }

                else -> super.handleMessage(msg)
            }
        }
    }

    companion object {
        lateinit var instance: FloatingViewService
    }

}