package com.example.poc_camera_acessobio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var navCamera:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navCamera = findViewById(R.id.nav_camera)
        navCamera.setOnClickListener {
            val intencao = Intent(this,StartCameraActivity::class.java)
            startActivity(intencao)
        }
    }
}