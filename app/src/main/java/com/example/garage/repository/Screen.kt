package com.example.garage.repository

sealed class Screen(val route :String) {

    object Login:Screen(route="loginScreen")
    object Register:Screen(route="RegisterScreen")
    object GarageDashboard:Screen(route="garageDashboard_Screen")

    object Activities:Screen(route = "GarageActivities_Screen")

    object AddTechnician:Screen(route = "addTechnician_Screen")

    object EditTechnician:Screen(route = "editTechnician_Screen")

    object GarageProfile:Screen(route = "garageProfile_Screen")

    object GarageProfileEdit:Screen(route = "garageProfileEdit_Screen")

    object TechnicianProfile:Screen(route = "technicianProfile_Screen")

    object TechnicianList:Screen(route = "technicianList_Screen")
    object TechnicianDashboard:Screen(route = "technicianDashboard_Screen")
    object TechnicianCompleteJob:Screen(route = "technicianCompleteJob_Screen")
    object TechnicianActivities:Screen(route = "technicianActivities")
    object SettingsScreen:Screen(route = "Setting_Screen")
    object HelpScreen:Screen(route = "Help")
    object Earning:Screen(route = "Earning")
    object ChangeTechnicianPhoneNumber:Screen(route = "ChangeTechnicianPhoneNumber")
    object TechHelpScreen:Screen(route = "TechHelpScreen")
    object TechnicianTrackLocation:Screen(route = "TechnicianTrackLocation")




}