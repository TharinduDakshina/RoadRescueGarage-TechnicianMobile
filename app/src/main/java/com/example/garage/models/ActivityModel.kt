package com.example.garage.models

class ActivityModel {
    private var _date:String=""
    private var _time:String=""
    private var _customerName:String=""
    private var _vehicle:String=""
    private var _technicianId:String=""
    private var _technicianName:String=""
    private var _amount:Double=0.0
    private var _description:String=""

    constructor(
        _date: String,
        _time: String,
        _customerName: String,
        _vehicle: String,
        _technicianId: String,
        _technicianName: String,
        _amount: Double,
        _description: String,
    ) {
        this._date = _date
        this._time = _time
        this._customerName = _customerName
        this._vehicle = _vehicle
        this._technicianId = _technicianId
        this._technicianName = _technicianName
        this._amount = _amount
        this._description = _description
    }

    constructor()


    fun getDate():String{
        return this._date
    }

    fun setDate(date:String){
        this._date=date
    }

    fun getTime():String{
        return this._time
    }

    fun setTime(time:String){
        this._time=time
    }

    fun getCustomerName():String{
        return this._customerName
    }

    fun setCustomerName(customerName:String){
        this._customerName=customerName
    }

    fun getVehicle():String{
        return this._vehicle
    }

    fun setVehicle(vehicle:String){
        this._vehicle=vehicle
    }

    fun getTechnicianId():String{
        return this._technicianId
    }

    fun setTechnicianId(technicianId:String){
        this._technicianId=technicianId
    }

    fun getTechnicianName():String{
        return this._technicianName
    }

    fun setTechnicianName(technicianName:String){
        this._technicianName=technicianName
    }

    fun getAmount():Double{
        return  this._amount
    }

    fun setAmount(amount:Double){
        this._amount=amount
    }

    fun getDescription():String{
        return this._description
    }

    fun setDescription(description:String){
        this._description=description
    }

    override fun toString(): String {
        return "ActivityModel(_date='$_date', _time='$_time', _customerName='$_customerName', _vehicle='$_vehicle', _technicianId='$_technicianId', _technicianName='$_technicianName', _amount=$_amount, _description='$_description')"
    }


}