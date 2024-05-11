package com.example.garage.viewModels

import java.sql.Date
import java.sql.Time

class GarageActivityDetails {
    private var _serviceRequestTime: String? = null
    private var _serviceRequestDate: String? = null
    private var _customerName: String = ""
    private var _vehicle: String = ""
    private var _technicianId: String = ""
    private var _amount: Float = 0f
    private var _description: String = ""
    private var _technicianName: String = ""


    constructor(
        _serviceRequestTime: String?,
        _serviceRequestDate: String?,
        _customerName: String,
        _vehicle: String,
        _technicianId: String,
        _amount: Float,
        _description: String,
        _technicianName: String,
    ) {
        this._serviceRequestTime = _serviceRequestTime
        this._serviceRequestDate = _serviceRequestDate
        this._customerName = _customerName
        this._vehicle = _vehicle
        this._technicianId = _technicianId
        this._amount = _amount
        this._description = _description
        this._technicianName = _technicianName
    }


    fun getServiceRequestTime(): String? {
        return this._serviceRequestTime
    }

    fun setServiceRequestTime(serviceRequestTime: String?) {
        this._serviceRequestTime = serviceRequestTime
    }

    fun getServiceRequestDate(): String? {
        return this._serviceRequestDate
    }

    fun setServiceRequestDate(date: String?) {
        this._serviceRequestDate = date
    }

    fun getCustomerName(): String {
        return this._customerName
    }

    fun setCustomerName(customerName: String) {
        this._customerName = customerName
    }

    fun getVehicle(): String {
        return this._vehicle
    }

    fun setVehicle(vehicle: String) {
        this._vehicle = vehicle
    }

    fun getTechnicianId(): String {
        return this._technicianId
    }

    fun setTechnicianId(techId: String) {
        this._technicianId = techId
    }

    fun getAmount(): Float {
        return this._amount
    }

    fun setAmount(amount: Float) {
        this._amount = amount
    }

    fun getDescription(): String {
        return this._description
    }

    fun setDescription(description: String) {
        this._description = description
    }

    fun getTechnicianName(): String {
        return this._technicianName
    }

    fun setTechnicianName(techName: String) {
        this._technicianName = techName
    }
}
