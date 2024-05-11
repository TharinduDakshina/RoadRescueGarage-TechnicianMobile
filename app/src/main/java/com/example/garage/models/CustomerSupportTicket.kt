package com.example.garage.models

class CustomerSupportTicket(
    private var customerSupportName:String,
    private var customerSupportContact:String
) {


    fun getCustomerSupportName():String{
       return this.customerSupportName
    }

    fun setCustomerSupportName(name:String){
        this.customerSupportName=name
    }

    fun getCustomerSupportContactNumber():String{
        return this.customerSupportContact
    }

    fun setCustomerSupportContactNumber(contact:String){
        this.customerSupportContact=contact
    }

    override fun toString(): String {
        return "CustomerSupportTicket(customerSupportName='$customerSupportName', customerSupportContact='$customerSupportContact')"
    }
}