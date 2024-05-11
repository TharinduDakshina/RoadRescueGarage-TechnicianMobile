package com.example.garage.models

class Garage {
    private var _garageId:String=""
    private var _garageName:String=""
    private var _ownerName:String=""
    private var _garageContactNumber:String=""
    private var _garageStatus=""
    private var _garageEmail:String=""
    private var _garageRating:Float=0F
    private var _type:String=""
    private var _garageProfilePicRef=""

    constructor()
    constructor(
        _garageId: String,
        _garageName: String,
        _ownerName: String,
        _garageContactNumber: String,
        _garageStatus: String,
        _garageEmail: String,
        _garageRating: Float,
        _type: String,
        _garageProfilePicRef: String
    ) {
        this._garageId = _garageId
        this._garageName = _garageName
        this._ownerName = _ownerName
        this._garageContactNumber = _garageContactNumber
        this._garageStatus = _garageStatus
        this._garageEmail = _garageEmail
        this._garageRating = _garageRating
        this._type = _type
        this._garageProfilePicRef = _garageProfilePicRef
    }

    constructor(_garageName: String, _ownerName: String, _garageContactNumber: String) {
        this._garageName = _garageName
        this._ownerName = _ownerName
        this._garageContactNumber = _garageContactNumber
    }

    constructor(
        _garageId: String,
        _garageName: String,
        _ownerName: String,
        _garageContactNumber: String,
        _garageEmail: String,
        _garageProfilePicRef: String
    ) {
        this._garageId = _garageId
        this._garageName = _garageName
        this._ownerName = _ownerName
        this._garageContactNumber = _garageContactNumber
        this._garageEmail = _garageEmail
        this._garageProfilePicRef = _garageProfilePicRef
    }


    fun getGarageId():String{
        return this._garageId
    }

    fun setGarageId(garageId:String){
        this._garageId=garageId
    }

    fun getGarageName():String{
        return this._garageName
    }

    fun setGarageName (garageName:String){
        this._garageName =garageName
    }

    fun getOwnerName():String{
        return this._ownerName
    }

    fun setOwnerName(ownerName:String){
        this._ownerName=ownerName
    }

    fun getGarageContactNumber():String{
        return this._garageContactNumber
    }

    fun setGarageContactNumber(contactNumber:String){
        this._garageContactNumber=contactNumber
    }

    fun getGarageStatus():String{
        return this._garageStatus
    }

    fun setGarageStatus(status:String){
        this._garageStatus=status
    }

    fun getGarageEmail():String{
        return this._garageEmail
    }

    fun setGarageEmail(email:String){
        this._garageEmail=email
    }

    fun getGarageRating():Float{
        return this._garageRating
    }

    fun setGarageRating(garageRate:Float){
        this._garageRating=garageRate
    }

    fun getGarageType():String{
        return this._type
    }

    fun setGarageType(type:String){
        this._type=type
    }

    fun getGarageProfilePicRef():String{
        return this._garageProfilePicRef
    }

    fun setGarageProfilePicRef(img:String){
        this._garageProfilePicRef=img
    }
}


// type eken mokkda kiyawenne
// garage ownwerge name eka database danna
//