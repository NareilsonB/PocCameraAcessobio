package com.example.poc_camera_acessobio.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StartCameraActivityViewModel:ViewModel() {

    private val _base64ResultObservable = MutableLiveData<String>()
    private var _cameraType = 99
    val base64ResultObservable:LiveData<String>
    get() = _base64ResultObservable
    var cameraType:Int
    get() = _cameraType
    set(value) {_cameraType = value}

    fun postResultBase64(base64:String){
        _base64ResultObservable.postValue(base64)
    }
}