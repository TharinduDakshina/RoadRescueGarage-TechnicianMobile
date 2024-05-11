package com.example.garage.models

class IssueSupportTicket(
    private var sp_Id:String,
    private var title:String,
    private var description:String
) {
    fun getSpId():String{
        return this.sp_Id
    }

    fun setSpId(sp_Id:String){
        this.sp_Id=sp_Id
    }

    fun getTitle():String{
        return this.title
    }

    fun setTitle(title:String){
        this.title=title
    }

    fun getDescription():String{
        return this.description
    }

    fun setDescription(description:String){
        this.description=description
    }
}