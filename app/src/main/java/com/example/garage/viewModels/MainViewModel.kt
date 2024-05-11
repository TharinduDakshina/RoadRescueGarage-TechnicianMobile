package com.example.garage.viewModels

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.garage.models.AddBankDetails
import com.example.garage.models.BankDetail
import com.example.garage.models.Garage
import com.example.garage.models.GarageTechnician
import com.example.garage.models.IssueSupportTicket
import com.example.garage.models.NewSupportTicket
import com.example.garage.models.NewTechnician
import com.example.garage.models.NewUser
import com.example.garage.models.RegisterModel
import com.example.garage.models.ResponseObject
import com.example.garage.models.TechnicianModel
import com.example.garage.models.UpdateGarage
import com.example.garage.repository.garageService
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    suspend fun  CancleRequest(serviceRequestId: Int) {

    }

    suspend fun  registerUser(
        registerModel: RegisterModel,
        option:String,
        onResponseReceived: (ResponseObject?) -> Unit
    ) {
        Log.d(ContentValues.TAG, "SignUpBox: 4")
        val deferred = CompletableDeferred<ResponseObject>()
        try {
            Log.d(ContentValues.TAG, "SignUpBox: 5")
            val new= NewUser(
                registerModel.getOwnerName(),
                registerModel.getGarageName(),
                registerModel.gePhoneNumber(),
                registerModel.getLatitude().toString(),
                registerModel.getLongitude().toString()
            )
            Log.d(ContentValues.TAG, "SignUpBox: 7")
            val call = garageService.regUSer(new)
            Log.d(ContentValues.TAG, "SignUpBox:8")
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        responseBody?.let {
                            val jsonString = it.string() // Convert response body to JSON string
                            val jsonObject = JSONObject(jsonString)
                            val status = jsonObject.optString("status").toInt()
                            val message = jsonObject.optString("message")
                            val data = jsonObject.optString("data")

                            val responseObject = ResponseObject(status, message, data)

                            onResponseReceived(responseObject)
                            deferred.complete(responseObject)
                        }

                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    deferred.completeExceptionally(t)
                }

            })

        } catch (e: Exception) {
            deferred.completeExceptionally(e)
        }catch (e:JSONException){
            deferred.completeExceptionally(e)
        }
        deferred.await()

    }


    suspend fun chanePhoneNumber(
        garageId: String,
        newPhoneNumber: String,
        option:String,
        onResponseReceived: (ResponseObject?) -> Unit
    ){
        val deferred = CompletableDeferred<ResponseObject>()
        try {

            val call = garageService.changePhoneNumber(garageId,newPhoneNumber,option)

            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        responseBody?.let {
                            val jsonString = it.string() // Convert response body to JSON string
                            val jsonObject = JSONObject(jsonString)
                            val status = jsonObject.optString("status").toInt()
                            val message = jsonObject.optString("message")
                            val data = jsonObject.optString("data")

                            val responseObject = ResponseObject(status, message, data)

                            onResponseReceived(responseObject)
                            deferred.complete(responseObject)
                        }

                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    deferred.completeExceptionally(t)
                }

            })

        } catch (e: Exception) {
            deferred.completeExceptionally(e)
        }catch (e:JSONException){
            deferred.completeExceptionally(e)
        }
        deferred.await()
    }

    suspend fun  updateLocation(
        latitude: Double,
        longitude: Double,
        character: String,
        id: String,
        onResponseReceived: (ResponseObject?) -> Unit
    ) {
        val deferred = CompletableDeferred<ResponseObject>()
        try {
            val call = garageService.locationUpdate(latitude,longitude,character,id)
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        responseBody?.let {
                            val jsonString = it.string() // Convert response body to JSON string
                            val jsonObject = JSONObject(jsonString)
                            val status = jsonObject.optString("status").toInt()
                            val message = jsonObject.optString("message")
                            val data = jsonObject.optString("data")

                            val responseObject = ResponseObject(status, message, data)

                            onResponseReceived(responseObject)
                            deferred.complete(responseObject)
                        }

                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    deferred.completeExceptionally(t)
                }

            })

        } catch (e: Exception) {
            deferred.completeExceptionally(e)
        }catch (e:JSONException){
            deferred.completeExceptionally(e)
        }
        deferred.await()
    }


//    val backendState = MutableLiveData(ResponseState())

    //Login page

    suspend fun checkPhoneNumberIsExists(
        phoneNumber:String,
        option:String,
        onResponseReceived: (ResponseObject?) -> Unit,
    ){
        val deferred = CompletableDeferred<ResponseObject>()
        try {
            val call = garageService.login(phoneNumber,option)
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        responseBody?.let {
                            val jsonString = it.string() // Convert response body to JSON string
                            val jsonObject = JSONObject(jsonString)
                            val status = jsonObject.optString("status").toInt()
                            val message = jsonObject.optString("message")
                            val data = jsonObject.optString("data")

                            val responseObject = ResponseObject(status, message, data)

                            onResponseReceived(responseObject)
                            deferred.complete(responseObject)
                        }

                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    deferred.completeExceptionally(t)
                }

            })

        } catch (e: Exception) {
            deferred.completeExceptionally(e)
        }catch (e:JSONException){
            deferred.completeExceptionally(e)
        }
        deferred.await()
    }




    suspend fun getExpertiseArias(
        searchId:String,
        option:String,
        onResponseReceived: (ResponseObject?) -> Unit
    ){
        val deferred = CompletableDeferred<ResponseObject>()

        viewModelScope.launch {
            try {
                val call = garageService.getTechnician(searchId,option)
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            responseBody?.let {
                                val jsonString = it.string() // Convert response body to JSON string
                                val jsonObject = JSONObject(jsonString)
                                val status = jsonObject.optString("status").toInt()
                                val message = jsonObject.optString("message")
                                val data = jsonObject.optString("data")

                                val responseObject = ResponseObject(status, message, data)

                                onResponseReceived(responseObject)
                                deferred.complete(responseObject)
                            }

                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        deferred.completeExceptionally(t)
                    }

                })

            } catch (e: Exception) {
                deferred.completeExceptionally(e)
            }catch (e:JSONException){
            deferred.completeExceptionally(e)
        }
        }
        deferred.await()
    }

    suspend fun getTechnicians(
        searchId:String,
        option:String,
        onResponseReceived: (ResponseObject?) -> Unit
    ) {

        val deferred = CompletableDeferred<ResponseObject>()

        viewModelScope.launch {
            try {
                val call = garageService.getTechnician(searchId,option)
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            responseBody?.let {
                                val jsonString = it.string() // Convert response body to JSON string
                                val jsonObject = JSONObject(jsonString)
                                val status = jsonObject.optString("status").toInt()
                                val message = jsonObject.optString("message")
                                val data = jsonObject.optString("data")

                                val responseObject = ResponseObject(status, message, data)

                                onResponseReceived(responseObject)
                                deferred.complete(responseObject)
                            }

                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        deferred.completeExceptionally(t)
                    }



                })

            } catch (e: Exception) {
                deferred.completeExceptionally(e)
            }catch (e:JSONException){
            deferred.completeExceptionally(e)
        }
        }
        deferred.await()
    }


    suspend fun getTechnicianServices(
        techId:String,
        option:String,
        onResponseReceived: (ResponseObject?) -> Unit
    ){

        val deferred = CompletableDeferred<ResponseObject>()

        viewModelScope.launch {
            try {
                val call = garageService.getTechnician(techId,option)
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            responseBody?.let {
                                val jsonString = it.string() // Convert response body to JSON string
                                val jsonObject = JSONObject(jsonString)
                                val status = jsonObject.optString("status").toInt()
                                val message = jsonObject.optString("message")
                                val data = jsonObject.optString("data")

                                val responseObject = ResponseObject(status, message, data)

                                onResponseReceived(responseObject)
                                deferred.complete(responseObject)
                            }

                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        deferred.completeExceptionally(t)
                    }



                })

            } catch (e: Exception) {
                deferred.completeExceptionally(e)
            }catch (e:JSONException){
                deferred.completeExceptionally(e)
            }
        }
        deferred.await()
    }

    suspend fun addTechnician(
        technician: GarageTechnician,
        onResponseReceived: (ResponseObject?) -> Unit // Lambda parameter to execute after receiving the response
    ) {
        val deferred = CompletableDeferred<ResponseObject>()

        viewModelScope.launch {
            try {
                val call = garageService.postTechnician(
                    NewTechnician(
                        technician.getTechFirstName(),
                        technician.getTechLastName(),
                        technician.getTechContactNumber(),
                        technician.getTechExpertiseAreas(),
                        technician.getTechStatus()
                    )
                )

                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            responseBody?.let {
                                val jsonString = it.string()
                                val jsonObject = JSONObject(jsonString)
                                val status = jsonObject.optString("status").toInt()
                                val message = jsonObject.optString("message")
                                val data = jsonObject.optString("data")

                                val responseObject = ResponseObject(status, message, data)


                                onResponseReceived(responseObject) // Execute the function passed as lambda parameter
                                deferred.complete(responseObject)
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        // Handle failure
                        deferred.completeExceptionally(t)
                    }
                })
            } catch (e: Exception) {
                // Handle exception
                deferred.completeExceptionally(e)
            }catch (e:JSONException){
                deferred.completeExceptionally(e)
            }
        }
        deferred.await()
    }


    suspend fun delTechnician(
        delId:String,
        onResponseReceived: (ResponseObject?) -> Unit
    ){
        val deferred = CompletableDeferred<ResponseObject>()

        viewModelScope.launch {
            try {
               val call = garageService.deleteTechnician(delId)
                call.enqueue(object :Callback<ResponseBody>{

                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            responseBody?.let {
                                val jsonString = it.string() // Convert response body to JSON string
                                val jsonObject = JSONObject(jsonString)
                                val status = jsonObject.optString("status").toInt()
                                val message = jsonObject.optString("message")
                                val data = jsonObject.optString("data")

                                val responseObject = ResponseObject(status, message, data)

                                onResponseReceived(responseObject)
                                deferred.complete(responseObject)
                            }

                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        deferred.completeExceptionally(t)
                    }

                })
            }catch (e:Exception){
                deferred.completeExceptionally(e)
            }catch (e:JSONException){
                deferred.completeExceptionally(e)
            }
        }
        deferred.await()
    }

    suspend fun updateTechnician(
        technician: GarageTechnician,
        onResponseReceived: (ResponseObject?) -> Unit // Lambda parameter to execute after receiving the response
    ){
        val deferred = CompletableDeferred<ResponseObject>()

        viewModelScope.launch {
            try{
                val call = garageService.updateTechnician(
                    "garage",com.example.garage.models.UpdateTechnicianByGarage(
                        technician.getTechId(),
                        technician.getTechFirstName(),
                        technician.getTechLastName(),
                        technician.getTechImageRef(),
                        technician.getTechExpertiseAreas()
                    )
                )

                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            responseBody?.let {
                                val jsonString = it.string()
                                val jsonObject = JSONObject(jsonString)
                                val status = jsonObject.optString("status").toInt()
                                val message = jsonObject.optString("message")
                                val data = jsonObject.optString("data")

                                val responseObject = ResponseObject(status, message, data)


                                onResponseReceived(responseObject) // Execute the function passed as lambda parameter
                                deferred.complete(responseObject)
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        // Handle failure
                        deferred.completeExceptionally(t)
                    }
                })
            }catch (e:Exception) {
                deferred.completeExceptionally(e)
            }catch (e:JSONException){
                deferred.completeExceptionally(e)
            }
        }
        deferred.await()
    }


    suspend fun updateTechnicianByTechnicianApp(
        technician: TechnicianModel,
        onResponseReceived: (ResponseObject?) -> Unit
    ){
        val deferred = CompletableDeferred<ResponseObject>()

        viewModelScope.launch {
            try{
                val call = garageService.updateTechnicianByTechnicianApp(
                    "technician",com.example.garage.models.UpdateTechnicianByTechnician(
                        technician.getTechId(),
                        technician.getTechFName(),
                        technician.getTechLName(),
                        technician.getImgRef(),
                        technician.getTechEmail()
                    )
                )

                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            responseBody?.let {
                                val jsonString = it.string()
                                val jsonObject = JSONObject(jsonString)
                                val status = jsonObject.optString("status").toInt()
                                val message = jsonObject.optString("message")
                                val data = jsonObject.optString("data")

                                val responseObject = ResponseObject(status, message, data)


                                onResponseReceived(responseObject) // Execute the function passed as lambda parameter
                                deferred.complete(responseObject)
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        // Handle failure
                        deferred.completeExceptionally(t)
                    }
                })
            }catch (e:Exception) {
                deferred.completeExceptionally(e)
            }catch (e:JSONException){
                deferred.completeExceptionally(e)
            }
        }
        deferred.await()
    }







    //---------------------------------------------------------Garage Section---------------------------------------------------------------

    suspend fun getGarageDetails(
        searchId:String,
        option:String,
        onResponseReceived: (ResponseObject?) -> Unit
    ) {

        val deferred = CompletableDeferred<ResponseObject>()

        viewModelScope.launch {
            try {
                val call = garageService.getGarageData(searchId,option)
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            responseBody?.let {
                                val jsonString = it.string() // Convert response body to JSON string
                                val jsonObject = JSONObject(jsonString)
                                val status = jsonObject.optString("status").toInt()
                                val message = jsonObject.optString("message")
                                val data = jsonObject.optString("data")

                                val responseObject = ResponseObject(status, message, data)

                                onResponseReceived(responseObject)
                                deferred.complete(responseObject)
                            }

                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        deferred.completeExceptionally(t)
                    }

                })

            } catch (e: Exception) {
                deferred.completeExceptionally(e)
            }catch (e:JSONException){
                deferred.completeExceptionally(e)
            }
        }
        deferred.await()
    }


    suspend fun updateGarage(
        garage:Garage,
        onResponseReceived: (ResponseObject?) -> Unit
    ){
        val deferred = CompletableDeferred<ResponseObject>()

        viewModelScope.launch{
            try {

                val call = garageService.updateGarage(UpdateGarage(
                    garage.getGarageId(),
                    garage.getGarageName(),
                    garage.getOwnerName(),
                    garage.getGarageContactNumber(),
                    garage.getGarageEmail(),
                    garage.getGarageProfilePicRef()
                ))

                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            responseBody?.let {
                                val jsonString = it.string()
                                val jsonObject = JSONObject(jsonString)
                                val status = jsonObject.optString("status").toInt()
                                val message = jsonObject.optString("message")
                                val data = jsonObject.optString("data")

                                val responseObject = ResponseObject(status, message, data)


                                onResponseReceived(responseObject) // Execute the function passed as lambda parameter
                                deferred.complete(responseObject)
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        // Handle failure
                        deferred.completeExceptionally(t)
                    }
                })
            }catch (e:Exception){
                deferred.completeExceptionally(e)
            }catch (e:JSONException){
                deferred.completeExceptionally(e)
            }
        }
        deferred.await()
    }

    suspend fun getGarageServiceRequest(
        searchId:String,
        option:String,
        onResponseReceived: (ResponseObject?) -> Unit
    ){
        val deferred = CompletableDeferred<ResponseObject>()

        viewModelScope.launch {
            try {
                val call = garageService.getServiceRequests(searchId,option)
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            responseBody?.let {
                                val jsonString = it.string() // Convert response body to JSON string
                                val jsonObject = JSONObject(jsonString)
                                val status = jsonObject.optString("status").toInt()
                                val message = jsonObject.optString("message")
                                val data = jsonObject.optString("data")

                                val responseObject = ResponseObject(status, message, data)

                                onResponseReceived(responseObject)
                                deferred.complete(responseObject)
                            }

                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        deferred.completeExceptionally(t)
                    }

                })

            } catch (e: Exception) {
                deferred.completeExceptionally(e)
            }catch (e:JSONException){
                deferred.completeExceptionally(e)
            }
        }
        deferred.await()
    }


    suspend fun assignTechnicianForService(
        option: String,
        serviceRequestId:Int,
        serviceProviderId:String,
        technicianId:String,
        onResponseReceived: (ResponseObject?) -> Unit
    ){
        val deferred = CompletableDeferred<ResponseObject>()

        viewModelScope.launch {
            try {
                val call = garageService.updateServiceRequest(option = option,serviceId = serviceRequestId, serviceProviderId =serviceProviderId, technicianId = technicianId)
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            responseBody?.let {
                                val jsonString = it.string() // Convert response body to JSON string
                                val jsonObject = JSONObject(jsonString)
                                val status = jsonObject.optString("status").toInt()
                                val message = jsonObject.optString("message")
                                val data = jsonObject.optString("data")

                                val responseObject = ResponseObject(status, message, data)

                                onResponseReceived(responseObject)
                                deferred.complete(responseObject)
                            }

                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        deferred.completeExceptionally(t)
                    }

                })

            } catch (e: Exception) {
                deferred.completeExceptionally(e)
            }catch (e:JSONException){
                deferred.completeExceptionally(e)
            }
        }
        deferred.await()
    }


   suspend fun completeJob(
       amount:String,
       option:String,
       serviceId:String,
       onResponseReceived: (ResponseObject?) -> Unit
   ){
       val deferred = CompletableDeferred<ResponseObject>()

       viewModelScope.launch {
           try {
               val call = garageService.completeJob(option,serviceId,amount)
               call.enqueue(object : Callback<ResponseBody> {
                   override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                       if (response.isSuccessful) {
                           val responseBody = response.body()
                           responseBody?.let {
                               val jsonString = it.string() // Convert response body to JSON string
                               val jsonObject = JSONObject(jsonString)
                               val status = jsonObject.optString("status").toInt()
                               val message = jsonObject.optString("message")
                               val data = jsonObject.optString("data")

                               val responseObject = ResponseObject(status, message, data)

                               onResponseReceived(responseObject)
                               deferred.complete(responseObject)
                           }

                       }
                   }

                   override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                       deferred.completeExceptionally(t)
                   }

               })

           } catch (e: Exception) {
               deferred.completeExceptionally(e)
           }catch (e:JSONException){
               deferred.completeExceptionally(e)
           }
       }
       deferred.await()
   }





    suspend fun getActivities(
        searchId:String,
        option: String,
        onResponseReceived: (ResponseObject?) -> Unit
    ){

        val deferred = CompletableDeferred<ResponseObject>()

        viewModelScope.launch {
            try {
                val call = garageService.getGarageData(searchId,option)
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            responseBody?.let {
                                val jsonString = it.string() // Convert response body to JSON string
                                val jsonObject = JSONObject(jsonString)
                                val status = jsonObject.optString("status").toInt()
                                val message = jsonObject.optString("message")
                                val data = jsonObject.optString("data")

                                val responseObject = ResponseObject(status, message, data)

                                onResponseReceived(responseObject)
                                deferred.complete(responseObject)
                            }

                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        deferred.completeExceptionally(t)
                    }

                })

            } catch (e: Exception) {
                deferred.completeExceptionally(e)
            }catch (e:JSONException){
                deferred.completeExceptionally(e)
            }
        }
        deferred.await()
    }

    suspend fun getCustomerSupport(
        searchId:String,
        option:String,
        onResponseReceived: (ResponseObject?) -> Unit)
    {
        val deferred = CompletableDeferred<ResponseObject>()

        viewModelScope.launch {
            try {
                val call = garageService.getGarageData(searchId,option)
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            responseBody?.let {
                                val jsonString = it.string() // Convert response body to JSON string
                                val jsonObject = JSONObject(jsonString)
                                val status = jsonObject.optString("status").toInt()
                                val message = jsonObject.optString("message")
                                val data = jsonObject.optString("data")

                                val responseObject = ResponseObject(status, message, data)

                                onResponseReceived(responseObject)
                                deferred.complete(responseObject)
                            }

                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        deferred.completeExceptionally(t)
                    }

                })

            } catch (e: Exception) {
                deferred.completeExceptionally(e)
            }catch (e:JSONException){
                deferred.completeExceptionally(e)
            }
        }
        deferred.await()
    }

    suspend fun sendSupportTicket(
        supportTicket: IssueSupportTicket,
        option:String,
        onResponseReceived: (ResponseObject?) -> Unit
    ){
        val deferred = CompletableDeferred<ResponseObject>()

        viewModelScope.launch {
            try {
                val call = garageService.postSupportTicket(
                    NewSupportTicket(
                        supportTicket.getSpId(),
                        supportTicket.getTitle(),
                        supportTicket.getDescription(),option
                    )
                )
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            responseBody?.let {
                                val jsonString = it.string() // Convert response body to JSON string
                                val jsonObject = JSONObject(jsonString)
                                val status = jsonObject.optString("status").toInt()
                                val message = jsonObject.optString("message")
                                val data = jsonObject.optString("data")

                                val responseObject = ResponseObject(status, message, data)

                                onResponseReceived(responseObject)
                                deferred.complete(responseObject)
                            }

                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        deferred.completeExceptionally(t)
                    }

                })

            } catch (e: Exception) {
                deferred.completeExceptionally(e)
            }catch (e:JSONException){
                deferred.completeExceptionally(e)
            }
        }
        deferred.await()
    }

    suspend fun checkPayment(
        serviceId: String,
        option: String,
        onResponseReceived: (ResponseObject?) -> Unit,
    ) {
        val deferred = CompletableDeferred<ResponseObject>()

        viewModelScope.launch {
            try {
                val call = garageService.getServiceRequests(serviceId,option)
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            Log.d("TAG response", "onResponse: ${response}")
                            val responseBody = response.body()
                            responseBody?.let {
                                val jsonString = it.string() // Convert response body to JSON string
                                val jsonObject = JSONObject(jsonString)
                                val status = jsonObject.optString("status").toInt()
                                val message = jsonObject.optString("message")
                                val data = jsonObject.optString("data")

                                Log.d("status", "onResponse: $status")
                                Log.d("message", "onResponse: $message")
                                Log.d("data", "onResponse: $data")

                                val responseObject = ResponseObject(status, message, data)

                                onResponseReceived(responseObject)
                                deferred.complete(responseObject)
                            }

                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        deferred.completeExceptionally(t)
                    }

                })

            } catch (e: Exception) {
                deferred.completeExceptionally(e)
            }catch (e:JSONException){
                deferred.completeExceptionally(e)
            }
        }
        deferred.await()
    }

   suspend fun loadTechActivities(
        techId: String,
        option: String,
        onResponseReceived: (ResponseObject?) -> Unit
    ) {

        val deferred = CompletableDeferred<ResponseObject>()

        viewModelScope.launch {
            try {
                val call = garageService.getTechnician(techId,option)
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            Log.d("TAG response", "onResponse: ${response}")
                            val responseBody = response.body()
                            responseBody?.let {
                                val jsonString = it.string() // Convert response body to JSON string
                                val jsonObject = JSONObject(jsonString)
                                val status = jsonObject.optString("status").toInt()
                                val message = jsonObject.optString("message")
                                val data = jsonObject.optString("data")

                                Log.d("status", "onResponse: $status")
                                Log.d("message", "onResponse: $message")
                                Log.d("data", "onResponse: $data")

                                val responseObject = ResponseObject(status, message, data)

                                onResponseReceived(responseObject)
                                deferred.complete(responseObject)
                            }

                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        deferred.completeExceptionally(t)
                    }

                })

            } catch (e: Exception) {
                deferred.completeExceptionally(e)
            }catch (e:JSONException){
                deferred.completeExceptionally(e)
            }
        }
        deferred.await()

    }

    suspend fun addBankDetails(
        garageId:String,
        bankDetail: BankDetail,
        onResponseReceived: (ResponseObject?) -> Unit
    ){
        val deferred = CompletableDeferred<ResponseObject>()

        viewModelScope.launch {
            try {
                val call = garageService.addBankDetails(
                    AddBankDetails(
                        bankDetail.getBank(),
                        bankDetail.getName(),
                        bankDetail.getBranch(),
                        bankDetail.getAccountNumber(),
                        garageId
                    )
                )
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            Log.d("TAG", "onResponse: $response")
                            Log.d("TAG", "onResponse: ${response.body().toString()}")
                            val responseBody = response.body()
                            responseBody?.let {
                                val jsonString = it.string() // Convert response body to JSON string
                                val jsonObject = JSONObject(jsonString)
                                val status = jsonObject.optString("status").toInt()
                                val message = jsonObject.optString("message")
                                val data = jsonObject.optString("data")

                                val responseObject = ResponseObject(status, message, data)

                                onResponseReceived(responseObject)
                                deferred.complete(responseObject)
                            }

                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        deferred.completeExceptionally(t)
                    }

                })

            } catch (e: Exception) {
                deferred.completeExceptionally(e)
            }catch (e:JSONException){
                deferred.completeExceptionally(e)
            }
        }
        deferred.await()
    }

    suspend fun changeStatus(
        id: String,
        status: String,
        option:String,
        onResponseReceived: (ResponseObject?) -> Unit
    ) {
        val deferred = CompletableDeferred<ResponseObject>()

        viewModelScope.launch {
            try {
                val call = garageService.changeStatus(option,status,id)
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            Log.d("TAG", "onResponse: $response")
                            Log.d("TAG", "onResponse: ${response.body().toString()}")
                            val responseBody = response.body()
                            responseBody?.let {
                                val jsonString = it.string() // Convert response body to JSON string
                                val jsonObject = JSONObject(jsonString)
                                val status = jsonObject.optString("status").toInt()
                                val message = jsonObject.optString("message")
                                val data = jsonObject.optString("data")

                                val responseObject = ResponseObject(status, message, data)

                                onResponseReceived(responseObject)
                                deferred.complete(responseObject)
                            }

                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        deferred.completeExceptionally(t)
                    }

                })

            } catch (e: Exception) {
                deferred.completeExceptionally(e)
            }catch (e:JSONException){
                deferred.completeExceptionally(e)
            }
        }
        deferred.await()

    }


}