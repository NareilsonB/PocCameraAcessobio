package com.example.poc_camera_acessobio

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.acesso.acessobio_android.AcessoBio
import com.acesso.acessobio_android.iAcessoBioCamera
import com.acesso.acessobio_android.iAcessoBioDocument
import com.acesso.acessobio_android.services.dto.ErrorBio
import com.acesso.acessobio_android.services.dto.ResultCamera
import com.example.poc_camera_acessobio.databinding.ActivityStartCameraBinding
import com.example.poc_camera_acessobio.utils.Constants.Companion.CAMERA_TYPE
import com.example.poc_camera_acessobio.viewmodel.StartCameraActivityViewModel
import com.google.android.material.snackbar.Snackbar
import okhttp3.internal.Internal

class StartCameraActivity : AppCompatActivity(), iAcessoBioCamera, iAcessoBioDocument {
    private lateinit var bindingView:ActivityStartCameraBinding
    private lateinit var viewModel: StartCameraActivityViewModel
    private lateinit var acessoBio:AcessoBio
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingView = DataBindingUtil.setContentView(this,R.layout.activity_start_camera)
        viewModel = ViewModelProvider(this).get(StartCameraActivityViewModel::class.java)
        viewModel.cameraType = intent.getIntExtra(CAMERA_TYPE,AcessoBio.NONE)
        acessoBio = AcessoBio(this,this)
        iniView()
        observable()
    }
    private fun observable(){
        viewModel.base64ResultObservable.observe(this,{ base64ImageResult->
            publishPreviewImage(base64ImageResult)
        })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun iniView(){
        configAcessoBio()
        if(!permissionOk()){
            requestPermissiom()
        }else{
            openCamera(viewModel.cameraType)
        }
        bindingView.button.setOnClickListener {
            if(permissionOk()){
                openCamera(viewModel.cameraType)
            }else{
                requestPermissiom()
            }
        }
    }

    private fun configAcessoBio(){
        acessoBio.setColorSilhoutte(R.color.success_stroke_color,R.color.error_stroke_color)
        acessoBio.setColorBackground(R.color.transparent)
        acessoBio.setColorBoxMessage(R.color.white)
        acessoBio.setColorTextMessage(R.color.colorBlack)
        acessoBio.setColorBackgroundPopupError(R.color.colorAccent)
        acessoBio.setColorTextPopupError(R.color.colorGreen)
        acessoBio.setColorBackgroundButtonPopupError(R.color.red_btn_bg_color)
        acessoBio.setColorTextButtonPopupError(R.color.colorPrimary)
        acessoBio.setColorBackgroundTakePictureButton(R.color.colorGreyDark)
        acessoBio.setColorIconTakePictureButton(R.color.colorOrange)
        acessoBio.setColorBackgroundBottomDocument(R.color.red_btn_bg_color)
        acessoBio.setColorTextBottomDocument(R.color.colorGreen)
        acessoBio.disableAutoCapture()
        acessoBio.setColorBoxMessage(R.color.white)
        acessoBio.setTimeoutSession(40.5)
        acessoBio.setTimeoutToFaceInference(15.0)
    }

    override fun onErrorAcessoBio(p0: ErrorBio?) {
        Toast.makeText(this,"onErrorAcessoBio: ${p0.toString()}",Toast.LENGTH_SHORT).show()
    }

    override fun userClosedCameraManually() {
        onBackPressed()
    }

    override fun systemClosedCameraTimeoutSession() {
        Toast.makeText(this,"Tempo da sessão expirou",Toast.LENGTH_SHORT).show()
    }

    override fun systemChangedTypeCameraTimeoutFaceInference() {
        Toast.makeText(this,"Tempo de inferecncia da camera disparado",Toast.LENGTH_SHORT).show()
    }

    override fun onSuccesstDocument(p0: String?) {
        p0?.let {docString->
            viewModel.postResultBase64(docString)
        }
    }

    override fun onErrorDocument(p0: String?) {
        Toast.makeText(this, "onErrorDocument: $p0",Toast.LENGTH_SHORT).show()
    }

    override fun onSuccessCamera(p0: ResultCamera?) {
        p0?.let { result->
          viewModel.postResultBase64(result.base64)
        }
    }

    override fun onErrorCamera(p0: ErrorBio?) {
        Toast.makeText(this,"onErrorCamera: ${p0.toString()}",Toast.LENGTH_SHORT).show()
    }

    private fun permissionOk():Boolean{
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true
        if(checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun requestPermissiom(){
        if(shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)){
            openCamera(viewModel.cameraType)
        }
        requestPermissions(arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun publishPreviewImage(resultBase64:String){
        val resultList = resultBase64.split(",")
        val base64 = resultList[1]
        val decoderString:ByteArray = Base64.decode(base64, Base64.DEFAULT)
        val imgBitMap = BitmapFactory.decodeByteArray(decoderString,0,decoderString.size)
        if(imgBitMap != null){
            bindingView.imgPreviewResult.setImageBitmap(imgBitMap)
        }else{
            bindingView.imgPreviewResult.setImageDrawable(resources.getDrawable(R.color.red_btn_bg_color,theme))
        }
    }

    private fun openCamera(cameraType:Int){
        if(cameraType!= AcessoBio.NONE){
            acessoBio.openCameraDocument(cameraType)
        }else{
            acessoBio.openCamera()
        }
    }

}