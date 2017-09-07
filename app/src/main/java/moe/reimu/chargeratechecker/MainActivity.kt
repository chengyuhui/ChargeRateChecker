package moe.reimu.chargeratechecker

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.IdRes
import android.view.View
import android.widget.TextView
import java.io.File

class MainActivity : AppCompatActivity() {

    val state_view: TextView by bindView(R.id.charge_state)
    val listener = object: BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            updateState()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        updateState()
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(listener, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(listener)
    }

    fun getChargeState() = File("/sys/class/power_supply/battery/charge_rate").readText()

    fun updateState() {
        val state = getChargeState().trim()
        state_view.text = state
        state_view.setTextColor(when (state) {
            "None" -> Color.BLACK
            "Turbo" -> Color.RED
            "Normal" -> Color.GREEN
            else -> Color.BLACK
        })
    }
}

fun <T : View> Activity.bindView(@IdRes res: Int): Lazy<T> {
    return lazy { findViewById<T>(res) }
}