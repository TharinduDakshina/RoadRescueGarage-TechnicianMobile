package com.example.garage.models

class ServicesRequestModel(
    private var _serviceRequestId: Int,
    private var _customerContactNumber: String,
    private var _time: String,
    private var _issue: String,
    private var _description: String,
    private var _serviceFee: Double,
    private var _indicatorLightStatus: String,
    private var _customerLocation: String

    ) {

    fun getServiceRequestId(): Int {
        return this._serviceRequestId
    }

    fun setServiceRequestId(serviceRequestId: Int) {
        this._serviceRequestId=serviceRequestId
    }

    fun getCustomerContactNumber(): String {
        return this._customerContactNumber
    }

    fun setCustomerContactNumber(contactNumber: String) {
        this._customerContactNumber = contactNumber
    }

    fun getTime(): String {
        return this._time
    }

    fun getCustomerLocation(): String {
        return this._customerLocation
    }

    fun setTime(time: String) {
        this._time
    }

    fun getIssue(): String {
        return this._issue
    }

    fun setIssue(issue: String) {
        this._issue = issue
    }

    fun getDescription(): String {
        return this._description
    }

    fun setDescription(description: String) {
        this._description = description
    }

    fun getServiceFee(): Double {
        return this._serviceFee
    }

    fun setServiceFee(serviceFee: Double) {
        this._serviceFee
    }

    fun getIndicatorState(): String {
        return this._indicatorLightStatus
    }

    fun setIndicatorState(indicatorState: String) {
        this._indicatorLightStatus
    }


}