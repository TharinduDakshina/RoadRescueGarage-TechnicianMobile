package com.example.garage.views.TechnicianApp

import android.content.ContentValues.TAG
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.garage.R
import com.example.garage.models.ResponseObject
import com.example.garage.models.TechnicianModel
import com.example.garage.repository.Screen
import com.example.garage.viewModels.LoginShearedViewModel
import com.example.garage.viewModels.MainViewModel
import com.example.garage.viewModels.TechShearedViewModel
import com.example.garage.views.CircularProcessingBar
import com.example.garage.views.CommonButton
import com.example.garage.views.CommonDropdown
import com.example.garage.views.CommonTextField
import com.example.garage.views.Footer
import com.example.garage.views.Header
import com.example.garage.views.cardDefaultModifier
import com.example.garage.views.defaultBackground
import com.example.garage.views.getSaveImage
import com.example.garage.views.saveImage
import com.example.garage.views.textStyle1
import com.example.garage.views.textStyle4
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

@Composable
fun TechnicianProfile(
    navController: NavController,
    navyStatus: String,
    loginShearedViewModel: LoginShearedViewModel,
    techShearedViewModel: TechShearedViewModel,
) {
    var techData = techShearedViewModel.techDetails
    var techName by remember { mutableStateOf("") }
    var techEmail by remember { mutableStateOf("") }
    var techcontactnumber by remember { mutableStateOf("") }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val showDialogSelectPic = remember { mutableStateOf(false) }
    var processingBarStatus = remember { mutableStateOf(false) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val viewModel: MainViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()


    val img: Bitmap =
        BitmapFactory.decodeResource(Resources.getSystem(), android.R.drawable.ic_menu_report_image)
    val bitmap = remember { mutableStateOf(img) }


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = {
            if (it != null) {
                bitmap.value = it
            }
        })

    var launcherImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        if (Build.VERSION.SDK_INT < 28) {
            bitmap.value = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
        } else {
            val source = it?.let { tempIt ->
                ImageDecoder.createSource(context.contentResolver, tempIt)
            }
            bitmap.value = source?.let { tempIt ->
                ImageDecoder.decodeBitmap(tempIt)
            }!!
        }
    }


    LaunchedEffect(Unit) {
        if (techData?.imgRef != "0") {
            bitmap.value = getSaveImage(techData?.imgRef)
        }
    }




    Column(
        modifier = defaultBackground,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    content = {
                        TechnicianSliderContent(navController,loginShearedViewModel) {
                            scope.launch {
                                drawerState.close()
                            }
                        }
                    }
                )
            }
        ) {
            Scaffold(
                topBar = {
                    Header {
                        scope.launch { drawerState.open() }
                    }
                },
                bottomBar = {
                    TechnicianFooter(navController, "navStatus")
                }
            ) {

                CircularProcessingBar(isShow = processingBarStatus.value)

                Column(
                    modifier = defaultBackground.padding(it),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Technician Profile",
                        style = textStyle4,
                        modifier = Modifier,
                        fontSize = 26.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = cardDefaultModifier
                            .align(Alignment.CenterHorizontally),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFB6C7E3)),
                        border = BorderStroke(width = 2.dp, Color.White),
                    ) {

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Row(
                                modifier = Modifier
                                    .weight(0.5f)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Card(
                                    shape = CircleShape,
                                    modifier = Modifier
                                        .background(Color.Red, shape = CircleShape)
                                        .fillMaxWidth(0.4f)
                                        .fillMaxHeight(0.73f)
                                        .border(
                                            BorderStroke(2.dp, Color.Unspecified),
                                            shape = CircleShape
                                        )
                                ) {
                                    Image(
                                        bitmap = bitmap.value.asImageBitmap(),
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color.Unspecified)
                                            .clip(CircleShape)
                                            .clickable { }
                                            .border(
                                                BorderStroke(2.dp, Color.Unspecified),
                                                shape = CircleShape
                                            ),
                                        contentDescription = "Garage Pitcher",
                                        contentScale = ContentScale.Crop,
                                    )
                                }

                                Icon(imageVector = Icons.Rounded.Edit,
                                    contentDescription = "edit Image",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .align(Alignment.Bottom)
                                        .background(
                                            Color(0xFF253555),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .clickable {
                                            showDialogSelectPic.value = true
                                        }
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .verticalScroll(state = rememberScrollState()),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {


                                Text(
                                    text = "Name",
                                    style = textStyle1,
                                    modifier = Modifier.padding(0.dp, 0.dp, 230.dp, 0.dp)
                                )


                                if (techData?.techFName != null) {
                                    techName = CommonTextField(
                                        value = techData.techFName + " " + techData.techLName,
                                        isEditing = true,
                                        placeholderName = " Name",
                                        modifier = Modifier,
                                        false,
                                        KeyboardType.Text
                                    )
                                }



                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "Email",
                                    style = textStyle1,
                                    modifier = Modifier.padding(0.dp, 0.dp, 230.dp, 0.dp)
                                )


                                techEmail = techData?.techEmail?.let { it1 ->
                                    CommonTextField(
                                        value = it1,
                                        isEditing = true,
                                        placeholderName = " Email",
                                        modifier = Modifier,
                                        false,
                                        KeyboardType.Text
                                    )
                                }.toString()

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "Expertise Areas",
                                    style = textStyle1,
                                    modifier = Modifier.padding(0.dp, 0.dp, 155.dp, 0.dp)
                                )

                                techData?.techExpatiateList?.let { it1 ->
                                    CommonDropdown(
                                        optionList = it1, defaultSelection = "Expertise Areas",
                                        Modifier
                                            .fillMaxWidth(0.85f)
                                            .height(50.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "Phone Number",
                                    style = textStyle1,
                                    modifier = Modifier.padding(0.dp, 0.dp, 155.dp, 0.dp)
                                )

                                techcontactnumber = techData?.techContactNumber?.let { it1 ->
                                    CommonTextField(
                                        value = it1,
                                        isEditing = false,
                                        placeholderName = "Phone Number",
                                        modifier = Modifier.height(52.dp),
                                        true,
                                        KeyboardType.Number
                                    )
                                }
                                    .toString()

                                Spacer(modifier = Modifier.height(16.dp))

                                CommonButton(btnName = "Edit Profile", modifier = Modifier
                                    .width(150.dp)
                                    .height(48.dp), onClickButton = {

                                    processingBarStatus.value=true

                                    bitmap.value.let { tempBitmap ->
                                        val saveLocation = saveImage(
                                            context,
                                            tempBitmap,
                                            loginShearedViewModel.loginId
                                        )

                                        if (saveLocation != null) {
                                            coroutineScope.launch {



                                                val trimmedName = techName.trim()
                                                val parts = trimmedName.split(Regex("\\s+"))
                                                val firstName = parts[0]
                                                val lastName = parts.drop(1).joinToString(" ")

                                                val technician =
                                                    techData?.techExpatiateList?.let { it1 ->
                                                        loginShearedViewModel.loginId?.let { it2 ->
                                                            TechnicianModel(
                                                                it2,
                                                                firstName,
                                                                lastName,
                                                                techcontactnumber,
                                                                techEmail,
                                                                saveLocation,
                                                                it1
                                                            )
                                                        }
                                                    }

                                                if (technician != null) {
                                                    val response=editProfileTechnician(
                                                        viewModel,
                                                        technician
                                                    )

                                                    if (response != null) {
                                                        if (response.status == 200) {
                                                            processingBarStatus.value=false
                                                            Toast.makeText(
                                                                context,
                                                                "Updated",
                                                                Toast.LENGTH_SHORT
                                                            ).show()

                                                            navController.navigate(Screen.TechnicianDashboard.route)

                                                        } else if (response.status == 400) {
                                                            processingBarStatus.value=false
                                                            Toast.makeText(
                                                                context,
                                                                response.message,
                                                                Toast.LENGTH_SHORT
                                                            ).show()

                                                        } else if (response.status == 404) {
                                                            processingBarStatus.value=false
                                                            Toast.makeText(
                                                                context,
                                                                response.message,
                                                                Toast.LENGTH_SHORT
                                                            ).show()

                                                        } else if (response.status == 500) {
                                                            processingBarStatus.value=false
                                                            Toast.makeText(
                                                                context,
                                                                response.message,
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        } else if (response.status == 508) {
                                                            processingBarStatus.value=false
                                                            Toast.makeText(
                                                                context,
                                                                response.message,
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        } else {
                                                            processingBarStatus.value=false
                                                            Toast.makeText(
                                                                context,
                                                                response.message,
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                    } else {
                                                        processingBarStatus.value=false
                                                        Toast.makeText(
                                                            context,
                                                            "Cannot call the sever",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }

                                                }
                                            }
                                        }
                                    }

                                })


                            }

                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                }

                Spacer(modifier = Modifier.height(26.dp))
                Footer(navController, navyStatus)
            }
        }
    }

    // image loader

    if (showDialogSelectPic.value) {
        Dialog(
            onDismissRequest = { showDialogSelectPic.value = false },
            content = {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .width(300.dp)
                        .height(80.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF253555))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.baseline_camera_alt_24),
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.White)
                                .clickable {
                                    launcher.launch()
                                    showDialogSelectPic.value = false
                                }
                        )
                        Text(
                            text = "Camera",
                            style = textStyle4
                        )
                    }
                    Spacer(modifier = Modifier.weight(0.2f))

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.baseline_image_24),
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.White)
                                .clickable {
                                    launcherImage.launch("image/*")
                                    showDialogSelectPic.value = false
                                }
                        )
                        Text(
                            text = "Gallery",
                            style = textStyle4
                        )
                    }

                }
            }
        )

    }

}


suspend fun editProfileTechnician(
    viewModel: MainViewModel,
    technician: TechnicianModel,
): ResponseObject? {
    var response: ResponseObject? = null

    Log.d(TAG, "editProfileTechnician: ${technician.getTechId()}")
    Log.d(TAG, "editProfileTechnician: ${technician.getImgRef()}")
    Log.d(TAG, "editProfileTechnician: ${technician.getTechFName()}")
    Log.d(TAG, "editProfileTechnician: ${technician.getTechLName()}")
    Log.d(TAG, "editProfileTechnician: ${technician.getTechContactNumber()}")
    Log.d(TAG, "editProfileTechnician: ${technician.getTechEmail()}")
    Log.d(TAG, "editProfileTechnician: ${technician.getTechExpatiateList()}")

    try {
        viewModel.updateTechnicianByTechnicianApp(technician) { responseObject ->
            if (responseObject != null) {
                response = responseObject
            } else {
                response = ResponseObject(400, "response is null", null)
            }
        }
    } catch (e: SocketTimeoutException) {
        response = ResponseObject(508, "Request time out.\n Please try again.", e.localizedMessage)
    } catch (e: Exception) {
        response = ResponseObject(404, "Exception error.", e.localizedMessage)
    }
    return response
}

suspend fun loadTechnicianData(viewModel: MainViewModel, techId: String): ResponseObject? {
    var response: ResponseObject? = null

    try {
        viewModel.getTechnicians(techId, "getTechnician") { responseObject ->
            if (responseObject != null) {
                response = responseObject
            } else {
                response = ResponseObject(400, "response is null", null)
            }
        }
    } catch (e: SocketTimeoutException) {
        response = ResponseObject(508, "Request time out.\n Please try again.", e.localizedMessage)
    } catch (e: Exception) {
        response = ResponseObject(404, "Exception error.", e.localizedMessage)
    }
    return response
}
