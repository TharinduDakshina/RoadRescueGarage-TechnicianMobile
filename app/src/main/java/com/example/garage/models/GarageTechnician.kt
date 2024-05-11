package com.example.garage.models

class GarageTechnician {
    private var _techId:String=""
    private var _techFirstName: String = ""
    private var _techLastName: String = ""
    private var _techContactNumber = ""
    private var _techExpertiseAreas: List<String> = emptyList()
    private var _techStatus: Int = 0
    private var _techImageRef: String = ""
    private var _techEmail: String = ""




    constructor(
        _techFirstName: String,
        _techLastName: String,
        _techContactNumber: String,
        _techExpertiseAreas: List<String>,
        _techEmail: String,
        _techImageRef:String
    ) {
        this._techFirstName = _techFirstName
        this._techLastName = _techLastName
        this._techContactNumber = _techContactNumber
        this._techExpertiseAreas = _techExpertiseAreas
        this._techEmail = _techEmail
        this._techImageRef=_techImageRef
    }

    constructor(
        _techFirstName: String,
        _techLastName: String,
        _techContactNumber: String,
        _techExpertiseAreas: List<String>,
        _techStatus: Int
    ) {
        this._techFirstName = _techFirstName
        this._techLastName = _techLastName
        this._techContactNumber = _techContactNumber
        this._techExpertiseAreas = _techExpertiseAreas
        this._techStatus = _techStatus
    }

    constructor(
        _techId: String,
        _techFirstName: String,
        _techLastName: String,
        _techImageRef: String,
        _techExpertiseAreas: List<String>
    ) {
        this._techId = _techId
        this._techFirstName = _techFirstName
        this._techLastName = _techLastName
        this._techImageRef = _techImageRef
        this._techExpertiseAreas = _techExpertiseAreas
    }

    constructor()


    fun getTechId(): String {
        return this._techId
    }

    fun setTechId(techId: String) {
        this._techId = techId
    }

    fun getTechFirstName(): String {
        return this._techFirstName
    }

    fun setTechFirstName(techFirstName: String) {
        this._techFirstName = techFirstName
    }

    fun getTechLastName(): String {
        return this._techLastName
    }

    fun setTechLastName(techLastName: String) {
        this._techLastName = techLastName
    }

    fun getTechStatus(): Int {
        return this._techStatus
    }

    fun setTechStatus(status: Int) {
        this._techStatus = status
    }

    fun getTechContactNumber(): String {
        return this._techContactNumber
    }

    fun setTechContactNumber(contactNumber: String) {
        this._techContactNumber = contactNumber
    }

    fun getTechExpertiseAreas(): List<String> {
        return this._techExpertiseAreas
    }

    fun setTechExpertiseAreas(techExpertiseAreas: List<String>) {
        this._techExpertiseAreas=techExpertiseAreas
    }

    fun getTechImageRef(): String {
        return this._techImageRef
    }

    fun setTechImageRef(imageRef: String) {
        this._techImageRef = imageRef
    }

    fun getTechEmail(): String {
        return this._techEmail
    }

    fun setTechEmail(email: String) {
        this._techEmail = email
    }
}



