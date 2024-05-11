package com.example.garage.models

import com.google.gson.annotations.SerializedName

data class NewTechnician(
    @SerializedName("techFirstName")
    val techFirstName: String,
    @SerializedName("techLastName")
    val techLastName: String,
    @SerializedName("techContactNumber")
    val techContactNumber: String,
    @SerializedName("techExpertiseAreas")
    val techExpertiseAreas: List<String>,
    @SerializedName("techStatus")
    val techStatus: Int,
)

data class UpdateTechnicianByGarage(
    @SerializedName("techId")
    val techId: String,
    @SerializedName("techFirstName")
    val techFirstName: String,
    @SerializedName("techLastName")
    val techLastName: String,
    @SerializedName("imageRef")
    val techImageRef:String,
    @SerializedName("techExpertiseAreas")
    val techExpertiseAreas: List<String>,
    )

data class UpdateGarage(
    @SerializedName("garageId")
    val garageId: String,
    @SerializedName("garageName")
    val garageName: String,
    @SerializedName("ownerName")
    val ownerName: String,
    @SerializedName("contactNumber")
    val contactNumber: String,
    @SerializedName("garageMail")
    val garageMail: String,
    @SerializedName("imageRef")
    val imageRef: String,
)

data class NewSupportTicket(
    @SerializedName("garageId")
    val garageId: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("option")
    val option: String
)

data class NewUser(
    @SerializedName("ownerName")
    val ownerName: String,
    @SerializedName("garageName")
    val garageName: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("longitude")
    val longitude: String
)

data class UpdateTechnicianByTechnician(
    @SerializedName("techId")
    val techId: String,
    @SerializedName("techFirstName")
    val techFirstName: String,
    @SerializedName("techLastName")
    val techLastName: String,
    @SerializedName("imageRef")
    val techImageRef:String,
    @SerializedName("techEmail")
    val techEmail:String
)

data class AddBankDetails(
    @SerializedName("bank")
    val bank: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("branch")
    val branch: String,
    @SerializedName("accountNumber")
    val accountNumber: String,
    @SerializedName("garageId")
    val garageId: String,
    )



