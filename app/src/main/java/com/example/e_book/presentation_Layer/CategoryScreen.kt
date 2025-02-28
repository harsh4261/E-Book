package com.example.e_book.presentation_Layer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.e_book.presentation_Layer.Navigation.BookByCategory
import com.example.e_book.viewModels.ViewModel

@Composable
fun CategoryScreen(

    viewModel: ViewModel = hiltViewModel(),
    navController: NavHostController
) {

    LaunchedEffect(true) {
        viewModel.getAllBookCategoryData()
    }

    val res = viewModel.state.value

    if (res.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = Color.White,
                trackColor = Color.Red
            )

        }
    }
    else if (res.error.isNotEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = res.error)
        }
    }
    else if (res.category.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(res.category) { category ->

                Card(
                    modifier = Modifier
                        .padding(top=30.dp)
                        .fillMaxWidth(0.8f)
                        .height(60.dp),
                    shape = RoundedCornerShape(40.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(255, 244, 229)
                    ),
                    elevation = CardDefaults.elevatedCardElevation(
                        4.dp
                    )// Set elevation as needed
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                navController.navigate(BookByCategory(category.Name))
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Text(
                            text = category.Name,
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )

                        )
                    }
                }

            }

        }
    }

}