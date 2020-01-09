package com.unicorn.ticket.bs

import android.os.Handler
import android.view.KeyEvent
import com.unicorn.ticket.bs.ui.base.BaseAct

abstract class ScanAct : BaseAct() {

    abstract fun onScanResult(result: String)

    private val sb = StringBuilder()
    private val myHandler = Handler()

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        when (event.action) {
            KeyEvent.ACTION_DOWN -> {
                if (event.keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                    return super.dispatchKeyEvent(event)
                }
                if (event.keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
                    return super.dispatchKeyEvent(event)
                }
                if (event.keyCode == KeyEvent.KEYCODE_BACK) {
                    return super.dispatchKeyEvent(event)
                }
                if (event.keyCode == KeyEvent.KEYCODE_MENU) {
                    return super.dispatchKeyEvent(event)
                }
                if (event.keyCode == KeyEvent.KEYCODE_HOME) {
                    return super.dispatchKeyEvent(event)
                }
                if (event.keyCode == KeyEvent.KEYCODE_POWER) {
                    return super.dispatchKeyEvent(event)
                }
                val unicodeChar = event.unicodeChar
                sb.append(unicodeChar.toChar())
                len++
                startScan()
                return true
            }
            else -> {
            }
        }
        return super.dispatchKeyEvent(event)
    }

    private var isScaning = false
    private var len = 0
    private var oldLen = 0

    private fun startScan() {
        if (isScaning) {
            return
        }
        isScaning = true
        timerScanCal()
    }

    private fun timerScanCal() {
        oldLen = len
        myHandler.postDelayed(scan, 100)
    }

    private var scan: Runnable = Runnable {
        if (oldLen != len) {
            timerScanCal()
            return@Runnable
        }
        isScaning = false
        if (sb.isNotEmpty()) {
            val result = sb.substring(0, sb.length - 2)
            onScanResult(result)
            sb.setLength(0)
        }
    }

}