package com.example.garage.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel


@Composable
fun GridWithTwoRows() {
  Column(
      modifier = Modifier
          .fillMaxSize()
          .background(Color.Gray)
  ) {

      CommonButton(btnName = "Connect", modifier = Modifier, onClickButton = {})

  }
}
