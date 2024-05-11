package com.example.garage.models

import android.content.Context
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.sql.Connection
import java.sql.DriverManager

data class GarageDataClass(
    var garageName: String?,
    var ownerName: String?,
    var phoneNumber: String?,
    )

/*
data class TechnicianDataClass(
    var
)
*/

interface AddUserCallback {
    fun onUserAddedSuccessfully(id: String)
    fun onUserAlreadyExists()
    fun onError(errorMessage: String)
}

class SingUpRepository() {
    fun addUser(
        context: Context,
        user: GarageDataClass, callback: AddUserCallback
    ) {
        val DATABASE_NAME = "road_rescue"
        val TABLE_NAME = "service_provider"
        val url =
            "jdbc:mysql://database-1.cxaiwakqecm4.eu-north-1.rds.amazonaws.com:3306/$DATABASE_NAME"
        val username = "admin"
        val databasePassword = "admin123"
        Thread {
            try {
                // Load the JDBC driver
                Class.forName("com.mysql.jdbc.Driver")
                // Establish connection to the database
                val connection: Connection =
                    DriverManager.getConnection(url, username, databasePassword)

                // Prepare a statement to check if the phone number already exists
                val checkStmt =
                    connection.prepareStatement("SELECT COUNT(*) FROM $TABLE_NAME WHERE phone_number = ?")
                checkStmt.setString(1, user.phoneNumber)

                // Execute the query
                val resultSet = checkStmt.executeQuery()

                // Check if the phone number exists
                var phoneNumberExists = false
                if (resultSet.next()) {
                    phoneNumberExists = resultSet.getInt(1) > 0
                }

                resultSet.close()

                // Insert the new user if the phone number does not exist
                if (!phoneNumberExists) {
                    val insertStmt =
                        connection.prepareStatement("INSERT INTO $TABLE_NAME(phone_number, garage_name, owner_name) VALUES(?, ?, ?)")
                    insertStmt.setString(1, user.phoneNumber)
                    insertStmt.setString(2, user.garageName)
                    insertStmt.setString(3, user.ownerName)

                    insertStmt.executeUpdate()




                    // Prepare a statement to check if the phone number already exists
                    val checkStmt1 =
                        connection.prepareStatement("SELECT * FROM $TABLE_NAME WHERE phone_number = ?")
                    checkStmt1.setString(1, user.phoneNumber)

                    // Execute the query
                    val resultSet1 = checkStmt1.executeQuery()

                    if (resultSet1.next()) {
                        val id = resultSet1.getInt("id").toString()
                        MainScope().launch {
                            callback.onUserAddedSuccessfully(id)
                        }
                    }



                } else {
                    MainScope().launch {
                        callback.onUserAlreadyExists()
                    }
                }
                // Close the connection
                connection.close()
            } catch (e: Exception) {
                MainScope().launch {
                    callback.onError(e.message ?: "An error occurred.")
                }
                e.printStackTrace()
            }
        }.start()
    }
}




//class SingUpRepository() {
//    fun addUser(
//        context: Context,
//        user: GarageDataClass, callback: AddUserCallback
//    ) {
//        val DATABASE_NAME = "road_rescue"
//        val TABLE_NAME1 = "service_provider"
//        val TABLE_NAME2 = "technician"
//        val url =
//            "jdbc:mysql://database-1.cxaiwakqecm4.eu-north-1.rds.amazonaws.com:3306/$DATABASE_NAME"
//        val username = "admin"
//        val databasePassword = "admin123"
//        Thread {
//            try {
//                // Load the JDBC driver
//                Class.forName("com.mysql.jdbc.Driver")
//                // Establish connection to the database
//                val connection: Connection =
//                    DriverManager.getConnection(url, username, databasePassword)
//
//                // Prepare a statement to check if the phone number already exists
//                val checkStmt1=
//                    connection.prepareStatement("SELECT COUNT(*) FROM $TABLE_NAME1 WHERE phone_number = ?")
//                checkStmt1.setString(1, user.phoneNumber)
//
//                val checkStmt2 =
//                    connection.prepareStatement("SELECT COUNT(*) FROM $TABLE_NAME1 WHERE phone_number = ?")
//                checkStmt2.setString(1, user.phoneNumber)
//
//                // Execute the query
//                val resultSet1= checkStmt1.executeQuery()
//                val resultSet2= checkStmt2.executeQuery()
//
//                // Check if the phone number exists
//                var phoneNumberExists = false
//                if (resultSet1.next()) {
//                    phoneNumberExists = resultSet1.getInt(1) > 0
//                }else if (resultSet2.next()){
//                    phoneNumberExists = resultSet1.getInt(1) > 0
//                }
//
//                resultSet1.close()
//                resultSet2.close()
//
//                // Insert the new user if the phone number does not exist
//                if (!phoneNumberExists) {
//                    val insertStmt =
//                        connection.prepareStatement("INSERT INTO $TABLE_NAME1(phone_number, garage_name, owner_name) VALUES(?, ?, ?)")
//                    insertStmt.setString(1, user.phoneNumber)
//                    insertStmt.setString(2, user.garageName)
//                    insertStmt.setString(3, user.ownerName)
//
//                    insertStmt.executeUpdate()
//
//
//
//
//                    // Prepare a statement to check if the phone number already exists
//                    val checkStmt1 =
//                        connection.prepareStatement("SELECT * FROM $TABLE_NAME WHERE phone_number = ?")
//                    checkStmt1.setString(1, user.phoneNumber)
//
//                    // Execute the query
//                    val resultSet1 = checkStmt1.executeQuery()
//
//                    if (resultSet1.next()) {
//                        val id = resultSet1.getInt("id").toString()
//                        MainScope().launch {
//                            callback.onUserAddedSuccessfully(id)
//                        }
//                    }
//
//
//
//                } else {
//                    MainScope().launch {
//                        callback.onUserAlreadyExists()
//                    }
//                }
//                // Close the connection
//                connection.close()
//            } catch (e: Exception) {
//                MainScope().launch {
//                    callback.onError(e.message ?: "An error occurred.")
//                }
//                e.printStackTrace()
//            }
//        }.start()
//    }
//}