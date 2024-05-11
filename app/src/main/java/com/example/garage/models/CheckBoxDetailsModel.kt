package com.example.garage.models

class CheckBoxDetailsModel (
    private var _checkBoxId:String,
    private var _checkBoxName:String,
    private  var _isSelected:Boolean
){
    fun getCheckBoxId():String{
        return this._checkBoxId
    }

    fun setCheckBoxId(checkBoxId:String){
        this._checkBoxId=checkBoxId
    }

    fun getCheckBoxName():String{
        return this._checkBoxName
    }

    fun setCheckBoxName(checkBoxName:String){
       this._checkBoxName=checkBoxName
    }

    fun getIsSelected():Boolean{
        return this._isSelected
    }

    fun setIsSelected(isSelected:Boolean){
        this._isSelected=isSelected
    }

}