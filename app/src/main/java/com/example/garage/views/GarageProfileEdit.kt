package com.example.garage.views

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Environment
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
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.garage.R
import com.example.garage.models.Garage
import com.example.garage.repository.Screen
import com.example.garage.viewModels.GarageSharedViewModel
import com.example.garage.viewModels.LoginShearedViewModel
import com.example.garage.viewModels.MainViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.net.SocketTimeoutException


@Composable
fun GarageProfileEdit(
    navController: NavHostController,
    navStatus: String,
    garageSharedViewModel: GarageSharedViewModel,
    loginShearedViewModel: LoginShearedViewModel
) {

    val garageData= garageSharedViewModel.garage

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var garageName by remember { mutableStateOf(garageData?.garageName) }
    var ownerName by remember { mutableStateOf(garageData?.garageOwner) }
    var garageStatus by remember { mutableStateOf(garageData?.garageStatus) }
    var contactNumber by remember { mutableStateOf(garageData?.garageContactNumber)}
    var email by remember { mutableStateOf(garageData?.garageEmail) }

    var showExpertiseArias by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var showResponseDialog by remember { mutableStateOf(false) }
    val showDialogSelectPic = remember { mutableStateOf(false) }
    var showSweetAlert by remember { mutableStateOf(false) }
    var selectedServices by remember { mutableStateOf(emptyList<String>()) }

    val viewModel= viewModel<MainViewModel>()
    val coroutineScope = rememberCoroutineScope()

    var status by remember { mutableStateOf(0) }
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var buttonOneName by remember { mutableStateOf("") }
    var buttonTwoName by remember { mutableStateOf("") }
    var expertiseAriasList by remember { mutableStateOf("") }
    var showVerificationIDContent by remember { mutableStateOf(false) }
    var showVerificationIDContentDialog by remember { mutableStateOf(false) }

    if (email=="example@gmail.com"){
        showVerificationIDContentDialog=true
    }


    val context= LocalContext.current
    var img: Bitmap = BitmapFactory.decodeResource(Resources.getSystem(),android.R.drawable.ic_menu_report_image)
    var bitmap= remember { mutableStateOf(img) }

    // image loader and picker

    val launcher= rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = {
            if (it!=null){
                bitmap.value=it
            }
        })

    var launcherImage= rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ){
        if (Build.VERSION.SDK_INT<28){
            bitmap.value= MediaStore.Images.Media.getBitmap(context.contentResolver,it)
        }else{
            val source=it?.let { tempIt->
                ImageDecoder.createSource(context.contentResolver,tempIt)
            }
            bitmap.value=source?.let {tempIt->
                ImageDecoder.decodeBitmap(tempIt)
            }!!
        }
    }


    LaunchedEffect(Unit) {
        Log.d("TAG profileEdit", "${garageData?.garageProfileImageRef}")
        if (garageData?.garageProfileImageRef!="0") {
            bitmap.value=getSaveGarageImage(garageData?.garageProfileImageRef)
        }
    }

    // load response message
    if (showResponseDialog){
        sweetAlertDialog(
            title = title,
            message = message,
            buttonOneName = buttonOneName,
            buttonTwoName = buttonTwoName,
            onConfirm = {
                showResponseDialog=false
                navController.navigate(route = Screen.GarageDashboard.route)
            }
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                content = {
                    SidebarContent(navController,loginShearedViewModel) {
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
                Footer(navController, navStatus)
            }
        ) {
            Column(
                modifier = defaultBackground.padding(it),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Spacer(modifier = Modifier.height(32.dp))

                Card(
                    modifier = cardDefaultModifier.fillMaxHeight(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFB6C7E3)),
                    border = BorderStroke(width = 2.dp, Color.White),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Card(
                                shape = CircleShape,
                                border = BorderStroke(width = 2.dp, color = Color.Unspecified),
                                modifier = Modifier
                                    .padding(8.dp, 16.dp, 8.dp, 8.dp)
                                    .fillMaxHeight(0.15f)
                                    .fillMaxWidth(0.28f)
                            ) {


                                Image(
                                    bitmap=bitmap.value.asImageBitmap(),
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
                                    .background(Color(0xFF253555), shape = RoundedCornerShape(8.dp))
                                    .clickable {
                                        showDialogSelectPic.value = true
                                    }
                            )

                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                        ) {

                            Text(
                                text = "Garage Name",
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(0.dp, 0.dp, 160.dp, 0.dp),
                                style = textStyle2
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            garageName=garageName?.let {
                                CommonTextField(
                                    it, true, "Garage Name", Modifier.height(46.dp), false,
                                    KeyboardType.Text)
                            }

                            Spacer(modifier = Modifier.height(8.dp))


                            Text(
                                text = "Owner Name",
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(0.dp, 0.dp, 168.dp, 0.dp),
                                style = textStyle2
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            ownerName=ownerName?.let { CommonTextField(it, true, "Owner Name", Modifier.height(46.dp), false,KeyboardType.Text) }

                            Spacer(modifier = Modifier.height(8.dp))


                            Text(
                                text = "Garage Status",
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(0.dp, 0.dp, 155.dp, 0.dp),
                                style = textStyle2
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            garageStatus=garageStatus?.let { CommonTextField(it, false, "Garage Status", Modifier.height(46.dp), false,KeyboardType.Text) }

                            Spacer(modifier = Modifier.height(8.dp))


                            Text(
                                text = "Contact number",
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(0.dp, 0.dp, 140.dp, 0.dp),
                                style = textStyle2
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            contactNumber= contactNumber?.let { CommonTextField(it, false, "Contact number", Modifier.height(46.dp), false,KeyboardType.Number) }


                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Email",
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(0.dp, 0.dp, 200.dp, 0.dp),
                                style = textStyle2
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            email=email?.let { CommonTextField(it, true, "Email", Modifier.height(50.dp), false,KeyboardType.Email) }

                            Spacer(modifier = Modifier.height(8.dp))

                            //-----------------------------------------------------------------

                            /*if (showVerificationIDContentDialog) {
                                Text(
                                    text = "Your Nic Image",
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .padding(0.dp, 0.dp, 150.dp, 0.dp),
                                    style = textStyle2
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Image(
                                    bitmap=bitmap.value.asImageBitmap(),
                                    modifier = Modifier
                                        .fillMaxWidth(0.95f)
                                        .height(150.dp)
                                        .padding(20.dp,0.dp,0.dp,0.dp)
                                        .background(Color.Unspecified)
                                        .clip(RoundedCornerShape(20))
                                        .clickable { }
                                        .border(
                                            BorderStroke(2.dp, Color.Unspecified),
                                            shape = CircleShape
                                        ),
                                    contentDescription = "Garage Pitcher",
                                    contentScale = ContentScale.Crop,
                                )
                            }*/





                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Spacer(modifier = Modifier.width(8.dp))

                                CommonButton(btnName = "Cancel", modifier = Modifier, onClickButton = {

                                    navController.navigate(route = Screen.GarageDashboard.route)
                                })

                                Spacer(modifier = Modifier.width(16.dp))

                                CommonButton(btnName = "Save", modifier = Modifier) {

//                        showDialog=true
                                    bitmap.value.let { tempBitMap ->
                                        val saveLocation = saveImageGarage(context, tempBitMap, garageData?.garageId)
                                        if (saveLocation != null) {
                                            coroutineScope.launch {

                                                try {
                                                    if (garageData!=null) {
                                                        val garage = garageName?.let {
                                                            ownerName?.let { it1 ->
                                                                contactNumber?.let { it2 ->
                                                                    email?.let { it3 ->
                                                                        Garage(
                                                                            garageData.garageId,
                                                                            it, it1, it2, it3,saveLocation

                                                                        )
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        if (garage != null) {
                                                            viewModel.updateGarage(garage) { responseObject ->

                                                                if (responseObject != null) {
                                                                    if (responseObject.status == 200) {
                                                                        title = "Updated"
                                                                        message = responseObject.message.toString()
                                                                        buttonOneName = "null"
                                                                        buttonTwoName = "null"
                                                                        showDialog = false
                                                                        showResponseDialog = true

                                                                        garageName = garageName?.trim()
                                                                        ownerName = ownerName?.trim()
                                                                        garageStatus = garageStatus?.trim()
                                                                        contactNumber = contactNumber?.trim()
                                                                        email = email?.trim()


                                                                    } else if (responseObject.status == 400) {
                                                                        title = "Failed"
                                                                        message = responseObject.message.toString()
                                                                        buttonOneName = "null"
                                                                        buttonTwoName = "null"
                                                                        showDialog = false
                                                                        showResponseDialog = true
                                                                    } else {
                                                                        title = "Error..!"
                                                                        message = responseObject.data.toString()
                                                                        buttonOneName = "null"
                                                                        buttonTwoName = "null"
                                                                        showDialog = false
                                                                        showResponseDialog = true
                                                                    }
                                                                }
                                                            }
                                                        }

                                                    }
                                                } catch (e: SocketTimeoutException) {
                                                    message = e.message.toString()
                                                    buttonOneName = "null"
                                                    buttonTwoName = "null"
                                                    showDialog = false
                                                    showResponseDialog = true
                                                } catch (e: Exception) {
                                                    message = e.message.toString()
                                                    buttonOneName = "null"
                                                    buttonTwoName = "null"
                                                    showDialog = false
                                                    showResponseDialog = true
                                                }
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                            }

                            Spacer(modifier = Modifier.height(32.dp))
                        }


                    }

                }

                Spacer(modifier = Modifier.height(25.dp))

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
    }




}


fun saveImageGarage(context: Context, tempBitmap: Bitmap, garageId: String?):String? {
    val fileName="image_$garageId ${System.currentTimeMillis()}.jpg"
    val directory= File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            .toString()+ File.separator+"RodaRescue")

    if (!directory.exists()){
        directory.mkdirs()
    }
    val file = File(directory,fileName)
    try {
        val stream: OutputStream = FileOutputStream(file)
        tempBitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
        stream.flush()
        stream.close()
        Toast.makeText(context, "Image saved successfully", Toast.LENGTH_SHORT).show()
    }catch (e:Exception){
        e.printStackTrace()
        Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
    }
    return fileName
}


fun getSaveGarageImage(techImageRef:String?): Bitmap {
    var bitmap: Bitmap = BitmapFactory.decodeResource(Resources.getSystem(),android.R.drawable.ic_menu_report_image)
    val directory= File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            .toString()+ File.separator+"RodaRescue")

    if (directory.exists() && directory.isDirectory){
        val file = techImageRef?.let { File(directory, it) }
        if (file != null) {
            if(file.exists()){
                bitmap= BitmapFactory.decodeFile(file.absolutePath)
            }
        }
    }
    return bitmap
}
