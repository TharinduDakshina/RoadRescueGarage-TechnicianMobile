package com.example.garage.viewModels

import androidx.lifecycle.ViewModel

class LoginViewModel:ViewModel() {
    interface PhoneNumberCheckCallback {
        fun onResult(exists: Boolean)
    }

    interface UserRetrievalCallback {
        fun onUserRetrieved(
            success: Boolean,
            firstName: String?,
            lastName: String?,
            email: String?,
            id: String?
        )
    }

//    fun getUserDetails(customer: Customer, callback: UserRetrievalCallback) {
//        val DATABASE_NAME = "road_rescue"
//        val TABLE_NAME = "customer"
//        val url =
//            "jdbc:mysql://database-1.cxaiwakqecm4.eu-north-1.rds.amazonaws.com:3306/$DATABASE_NAME"
//        val username = "admin"
//        val databasePassword = "admin123"
//
//        Thread {
//            var connection: Connection? = null
//            try {
//                Class.forName("com.mysql.jdbc.Driver")
//                connection = DriverManager.getConnection(url, username, databasePassword)
//
//                val queryStmt =
//                    connection.prepareStatement("SELECT * FROM $TABLE_NAME WHERE phone_number = ?")
//                queryStmt.setString(1, customer.phoneNumber)
//
//                val resultSet = queryStmt.executeQuery()
//
//                if (resultSet.next()) {
//                    val firstName = resultSet.getString("f_name")
//                    val id = resultSet.getInt("id")
//                    val lastName = resultSet.getString("l_name")
//                    val email = resultSet.getString("email")
//
//                    // Success, user found
//                    callback.onUserRetrieved(true, firstName, lastName, email, id.toString())
//                } else {
//                    // User not found
//                    callback.onUserRetrieved(false, null, null, null, null)
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//                callback.onUserRetrieved(false, null, null, null, null) // In case of error
//            } finally {
//                try {
//                    connection?.close()
//                } catch (e: SQLException) {
//                    e.printStackTrace()
//                }
//            }
//        }.start()
//    }
//
//    fun checkPhoneNumberExists(customer: Customer, callback: PhoneNumberCheckCallback) {
//        val DATABASE_NAME = "road_rescue"
//        val TABLE_NAME = "customer"
//        val url =
//            "jdbc:mysql://database-1.cxaiwakqecm4.eu-north-1.rds.amazonaws.com:3306/$DATABASE_NAME"
//        val username = "admin"
//        val databasePassword = "admin123"
//
//        Thread {
//            try {
//                Class.forName("com.mysql.jdbc.Driver")
//                val connection: Connection =
//                    DriverManager.getConnection(url, username, databasePassword)
//
//                val checkStmt =
//                    connection.prepareStatement("SELECT COUNT(*) FROM $TABLE_NAME WHERE phone_number = ?")
//                checkStmt.setString(1, customer.phoneNumber)
//                val resultSet = checkStmt.executeQuery()
//                val exists = resultSet.next() && resultSet.getInt(1) > 0
//                // Use the callback to return the result
//                callback.onResult(exists)
//
//                connection.close()
//            } catch (e: Exception) {
//                e.printStackTrace()
//                callback.onResult(false) // Consider how you want to handle errors
//            }
//        }.start()
//    }
}