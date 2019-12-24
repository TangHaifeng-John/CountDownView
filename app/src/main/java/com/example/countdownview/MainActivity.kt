package com.example.countdownview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_main)

        countdown_view.startCountDown()

        start_countdown.setOnClickListener {
            countdown_view.startCountDown()
        }

        stop_countdown.setOnClickListener {
            countdown_view.stopCountDown()
        }
        pause_countdown.setOnClickListener {
            countdown_view.pauseCountDown()
        }

        resume_countdown.setOnClickListener {
            countdown_view.resumeCountDown()
        }
    }


}
