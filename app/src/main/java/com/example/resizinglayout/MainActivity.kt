package com.example.resizinglayout

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startButton = findViewById<Button>(R.id.btnStart)
        val stopButton = findViewById<Button>(R.id.btnStop)

        startButton.setOnClickListener {
            if (!isServiceRunning) startFloatingService()
        }

        stopButton.setOnClickListener {
            closeService()
        }

        initService()

    }

    private fun closeService() {
        if (isServiceRunning) FloatingViewService.instance.destroyService()
    }

    private fun initService() {
        val serviceInstance = Intent()
        serviceInstance.component = ComponentName(
            "com.example.resizinglayout",
            "com.example.resizinglayout.FloatingViewService"
        )
        bindService(serviceInstance, serviceConnection, BIND_AUTO_CREATE)
    }

    override fun finish() {
        closeService()
        if (bound) unbindService(serviceConnection)
        super.finish()
    }

    private var messenger: Messenger? = null
    private var bound: Boolean = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            messenger = Messenger(service)
            bound = true
        }

        override fun onServiceDisconnected(className: ComponentName) {
            messenger = null
            bound = false
        }
    }

    private fun startFloatingService() {
        if (bound) {
            val msg = Message()
            msg.what = 1
            try {
                messenger!!.send(msg)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        var isServiceRunning = false
    }

}