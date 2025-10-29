package com.example.udaraku

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.udaraku.InputPredictActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startButton: Button = findViewById(R.id.button_start)
        val aboutButton: Button = findViewById(R.id.button_about)
        val usageButton: Button = findViewById(R.id.button_usage)

        startButton.setOnClickListener {
            startActivity(Intent(this, InputPredictActivity::class.java))
        }

        aboutButton.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }

        usageButton.setOnClickListener {
            startActivity(Intent(this, UsageActivity::class.java))
        }
    }
}
