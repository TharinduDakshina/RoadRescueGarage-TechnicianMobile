package com.example.garage.models

class RegisterModel(
    private var ownerName:String,
    private var garageName:String,
    private var phoneNumber:String,
    private var latitude: Double,
    private var longitude: Double

) {

    fun getOwnerName():String{
        return this.ownerName
    }


    fun getGarageName():String{
        return this.garageName
    }


    fun gePhoneNumber():String{
       return this.phoneNumber
    }


    fun setOwnerName(ownerName:String){
        this.ownerName=ownerName
    }

    fun setGarageName(garageName:String){
        this.garageName=garageName
    }

    fun setPhoneNumber(phoneNumber:String){
        this.phoneNumber=phoneNumber
    }

    fun getLatitude():Double{
        return this.latitude
    }

    fun getLongitude():Double{
        return this.longitude
    }

    fun setLatitude(latitude:Double){
        this.latitude=latitude
    }
    fun setLongitude(longitude:Double){
        this.longitude=longitude
    }

}