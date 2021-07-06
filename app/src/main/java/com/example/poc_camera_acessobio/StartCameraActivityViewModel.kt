package com.example.poc_camera_acessobio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StartCameraActivityViewModel:ViewModel() {
    private val _base64ResultObservable = MutableLiveData<String>()

    val base64ResultObservable:LiveData<String>
    get() = _base64ResultObservable

    fun postResultBase64(base64:String){
        _base64ResultObservable.postValue(base64)
    }
}