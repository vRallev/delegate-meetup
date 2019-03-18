package net.vrallev.delgate

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

private const val COUNT_KEY = "count_key"

class MainActivity : AppCompatActivity() {

    private var clickCount = 0

    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.button)

        clickCount = savedInstanceState?.getInt(COUNT_KEY, clickCount) ?: clickCount
        updateButtonText()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(COUNT_KEY, clickCount)
    }

    fun click(view: View) {
        clickCount++
        updateButtonText()
    }

    @SuppressLint("SetTextI18n")
    private fun updateButtonText() {
        button.text = "Clicked: $clickCount"
    }
}