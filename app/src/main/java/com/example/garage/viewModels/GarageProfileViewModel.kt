package com.example.garage.viewModels


class GarageProfileViewModel {

    private var _garageName: String = ""
    private var _garageId: String = ""
    private var _ownerName: String = ""
    private var _garageContactNumber: String = ""
    private var _garageEmail: String = ""
    private var _iconPath: Int = 0
    private var _IconName: String = ""
    private var _routPath: String = ""


    constructor(iconPath: Int, iconName: String,path:String) {
        this._IconName = iconName
        this._iconPath = iconPath
        this._routPath =path
    }

    constructor(
        garageName: String,
        garageId: String,
        ownerName: String,
        garageContactNumber: String,
        garageEmail: String,
    ) {
        this._garageName = garageName
        this._garageId = garageId
        this._ownerName = ownerName
        this._garageContactNumber = garageContactNumber
        this._garageEmail = garageEmail
    }

    constructor(_iconPath: Int, _IconName: String) {
        this._iconPath = _iconPath
        this._IconName = _IconName
    }

    fun getIconPath(): Int {
        return _iconPath
    }

    fun setIconPath(iconPath: Int) {
        this._iconPath = iconPath
    }

    fun getIconName(): String {
        return this._IconName
    }

    fun setIconName(numberOfPersons: String) {
        _IconName = numberOfPersons
    }

    fun getGarageName(): String {
        return this._garageName
    }

    fun setGarageName(garageName: String) {
        this._garageName = garageName
    }

    fun getGarageId(): String {
        return this._garageId
    }

    fun setGarageId(garageId: String) {
        this._garageId = garageId
    }

    fun getOwnerName(): String {
        return this._ownerName
    }

    fun setOwnerName(ownerName: String) {
        this._ownerName = ownerName
    }

    fun getGarageContactNumber(): String {
        return this._garageContactNumber
    }

    fun setGarageContactNumber(contactNumber: String) {
        this._garageContactNumber = contactNumber
    }

    fun getGarageEmail(): String {
        return this._garageEmail
    }

    fun setGarageEmail(email: String) {
        this._garageEmail = email
    }


}