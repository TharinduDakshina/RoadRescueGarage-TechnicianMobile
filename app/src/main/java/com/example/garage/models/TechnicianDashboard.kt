package com.example.garage.models

class TechnicianDashboard(
    private var _serviceId:String,
    private var _time:String,
    private var _description:String,
    private var _issueCategory:String,
    private var _customerName:String,
    private var _customerContact:String,
    private var _vehicleModel:String,
    private var _customerLocation:String
) {

    fun getServiceId(): String {
        return _serviceId
    }

    fun setServiceId(id: String) {
        _serviceId = id
    }


    fun getTime(): String {
        return _time
    }

    fun setTime(time: String) {
        _time = time
    }

    fun getDescription(): String {
        return _description
    }

    fun setDescription(description: String) {
        _description = description
    }

    fun getIssueCategory(): String {
        return _issueCategory
    }

    fun setIssueCategory(issueCategory: String) {
        _issueCategory = issueCategory
    }

    fun getCustomerName(): String {
        return _customerName
    }
    fun grtCustomerLocation():String{
        return _customerLocation
    }

    fun setCustomerName(customerName: String) {
        _customerName = customerName
    }

    fun getCustomerContact(): String {
        return _customerContact
    }


    fun setCustomerContact(customerContact: String) {
        _customerContact = customerContact
    }

    fun getVehicleModel(): String {
        return _vehicleModel
    }

    fun setVehicleModel(vehicleModel: String) {
        _vehicleModel = vehicleModel
    }

    override fun toString(): String {
        return "TechnicianDashboard(_serviceId='$_serviceId', _time='$_time', _description='$_description', _issueCategory='$_issueCategory', _customerName='$_customerName', _customerContact='$_customerContact', _vehicleModel='$_vehicleModel')"
    }


}


