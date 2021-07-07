package com.example.poc_camera_acessobio
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.acesso.acessobio_android.AcessoBio
import com.example.poc_camera_acessobio.databinding.ActivityMainBinding
import com.example.poc_camera_acessobio.utils.Constants.Companion.CAMERA_TYPE

class MainActivity : AppCompatActivity() {
    private lateinit var bindingView: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingView = DataBindingUtil.setContentView(this, R.layout.activity_main)
        actionListener()
    }

    private fun actionListener(){
        bindingView.navCameraCnh.setOnClickListener {
            navAction(AcessoBio.CNH)
        }
        bindingView.navCameraRgBack.setOnClickListener {
            navAction(AcessoBio.RG_VERSO)
        }
        bindingView.navCameraRgFront.setOnClickListener {
            navAction(AcessoBio.RG_FRENTE)
        }
        bindingView.navCameraSelfie.setOnClickListener {
            navAction(AcessoBio.NONE)
        }
    }

    private fun navAction(cameraType: Int) {
        val intencao = Intent(this, StartCameraActivity::class.java)
        intencao.putExtra(CAMERA_TYPE, cameraType)
        startActivity(intencao)
    }
}