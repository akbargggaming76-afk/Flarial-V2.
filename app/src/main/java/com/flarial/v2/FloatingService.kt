
package com.flarial.v2

import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class FloatingService : Service() {

    private lateinit var windowManager: WindowManager
    private var bubble: TextView? = null
    private var menu: LinearLayout? = null

    private lateinit var bubbleParams: WindowManager.LayoutParams
    private lateinit var menuParams: WindowManager.LayoutParams

    private var menuVisible = false

    override fun onCreate() {
        super.onCreate()

        if (!Settings.canDrawOverlays(this)) {
            stopSelf()
            return
        }

        windowManager =
            getSystemService(WINDOW_SERVICE) as WindowManager

        createBubble()
    }

    private fun overlayType(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }
    }

    private fun createBubble() {
        val button = TextView(this).apply {
            text = "F"
            textSize = 24f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            background = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(Color.rgb(151, 75, 225))
                setStroke(3, Color.WHITE)
            }
            elevation = 10f
        }

        bubbleParams = WindowManager.LayoutParams(
            58,
            58,
            overlayType(),
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 20
            y = 250
        }

        var startX = 0
        var startY = 0
        var touchX = 0f
        var touchY = 0f
        var moved = false

        button.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startX = bubbleParams.x
                    startY = bubbleParams.y
                    touchX = event.rawX
                    touchY = event.rawY
                    moved = false
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    val dx = (event.rawX - touchX).toInt()
                    val dy = (event.rawY - touchY).toInt()

                    if (kotlin.math.abs(dx) > 5 ||
                        kotlin.math.abs(dy) > 5) {
                        moved = true
                    }

                    bubbleParams.x = startX + dx
                    bubbleParams.y = startY + dy

                    windowManager.updateViewLayout(
                        button,
                        bubbleParams
                    )
                    true
                }

                MotionEvent.ACTION_UP -> {
                    if (!moved) {
                        toggleMenu()
                    }
                    true
                }

                else -> true
            }
        }

        bubble = button
        windowManager.addView(button, bubbleParams)
    }

    private fun toggleMenu() {
        if (menuVisible) {
            closeMenu()
        } else {
            showMenu()
        }
    }

    private fun showMenu() {
        if (menuVisible) return

        val panel = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(20, 18, 20, 18)
            background = GradientDrawable().apply {
                setColor(Color.rgb(35, 18, 55))
                cornerRadius = 28f
                setStroke(2, Color.rgb(151, 75, 225))
            }
        }

        val title = TextView(this).apply {
            text = "FLARIAL V2"
            textSize = 21f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(8, 8, 8, 18)
        }

        panel.addView(title)

        addMenuItem(panel, "Quick Settings")
        addMenuItem(panel, "HUD Notes")
        addMenuItem(panel, "Coordinates Notes")
        addMenuItem(panel, "Overlay Settings")

        val close = TextView(this).apply {
            text = "CLOSE"
            textSize = 15f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(12, 16, 12, 12)
            background = GradientDrawable().apply {
                setColor(Color.rgb(115, 48, 175))
                cornerRadius = 16f
            }
            setOnClickListener {
                closeMenu()
            }
        }

        panel.addView(close)

        menuParams = WindowManager.LayoutParams(
            260,
            WindowManager.LayoutParams.WRAP_CONTENT,
            overlayType(),
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.CENTER
        }

        menu = panel
        windowManager.addView(panel, menuParams)
        menuVisible = true
    }

    private fun addMenuItem(
        panel: LinearLayout,
        label: String
    ) {
        val item = TextView(this).apply {
            text = label
            textSize = 15f
            setTextColor(Color.WHITE)
            setPadding(14, 15, 14, 15)
            background = GradientDrawable().apply {
                setColor(Color.rgb(67, 39, 95))
                cornerRadius = 14f
            }
        }

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            bottomMargin = 9
        }

        panel.addView(item, params)
    }

    private fun closeMenu() {
        menu?.let {
            if (it.isAttachedToWindow) {
                windowManager.removeView(it)
            }
        }

        menu = null
        menuVisible = false
    }

    override fun onDestroy() {
        closeMenu()

        bubble?.let {
            if (it.isAttachedToWindow) {
                windowManager.removeView(it)
            }
        }

        bubble = null
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
