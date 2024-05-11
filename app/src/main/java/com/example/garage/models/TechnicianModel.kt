package com.example.garage.models

class TechnicianModel(
    private var _techId:String,
    private var _techFName: String,
    private var _techLName: String,
    private var _techContactNumber: String,
    private var _techEmail: String,
    private var _imgRef: String,
    private var _techExpatiateList: List<String>
) {


    fun getTechId():String{
        return _techId
    }

    fun setTechId(id:String){
        this._techId=id
    }

    fun getTechFName(): String {
        return _techFName
    }

    fun setTechFName(techFName: String) {
        _techFName = techFName
    }

    fun getTechLName(): String {
        return _techLName
    }

    fun setTechLName(techLName: String) {
        _techLName = techLName
    }

    fun getTechContactNumber(): String {
        return _techContactNumber
    }

    fun setTechContactNumber(techContactNumber: String) {
        _techContactNumber = techContactNumber
    }

    fun getTechEmail(): String {
        return _techEmail
    }

    fun setTechEmail(techEmail: String) {
        _techEmail = techEmail
    }

    fun getImgRef(): String {
        return _imgRef
    }

    fun setImgRef(imgRef: String) {
        _imgRef = imgRef
    }

    fun getTechExpatiateList(): List<String> {
        return _techExpatiateList
    }

    fun setTechExpatiateList(techExpatiateList: List<String>) {
        _techExpatiateList = techExpatiateList
    }

    override fun toString(): String {
        return "TechnicianModel(_techFName='$_techFName', _techLName='$_techLName', _techContactNumber='$_techContactNumber', _techEmail='$_techEmail', _imgRef='$_imgRef', _techExpatiateList=$_techExpatiateList)"
    }
}

