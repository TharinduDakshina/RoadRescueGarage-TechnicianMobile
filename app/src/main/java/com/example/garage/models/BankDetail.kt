package com.example.garage.models

class BankDetail(
    private  var bank:String,
    private var name:String,
    private var branch:String,
    private var accountNumber:String
) {

    fun getBank(): String {
        return bank
    }

    fun setBank(newBank: String) {
        bank = newBank
    }

    fun getName(): String {
        return name
    }

    fun setName(newName: String) {
        name = newName
    }

    fun getBranch(): String {
        return branch
    }

    fun setBranch(newBranch: String) {
        branch = newBranch
    }

    fun getAccountNumber(): String {
        return accountNumber
    }

    fun setAccountNumber(newAccountNumber: String) {
        accountNumber = newAccountNumber
    }

}