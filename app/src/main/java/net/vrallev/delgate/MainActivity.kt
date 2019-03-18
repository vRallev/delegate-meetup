package net.vrallev.delgate

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var clickCount by instanceState(defaultValue = 0)
    private val button by view<Button>(R.id.button)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        updateButtonText()
    }

    fun click(@Suppress("UNUSED_PARAMETER") view: View) {
        clickCount++
        updateButtonText()
    }

    @SuppressLint("SetTextI18n")
    private fun updateButtonText() {
        button.text = "Clicked: $clickCount"
    }
}
