package com.catsnews.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.catsnews.catnews.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.splash_screen)
       Handler().postDelayed({
           val intent=Intent(this, MainActivity::class.java)
           startActivity(intent)
       },3000)
    }
}