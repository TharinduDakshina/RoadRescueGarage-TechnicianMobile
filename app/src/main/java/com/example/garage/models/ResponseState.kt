package com.example.garage.models


data class ResponseObject(
    val status:Int,
    val message:String?,
    val data:Any?=null
)
