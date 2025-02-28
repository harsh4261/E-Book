package com.example.e_book.presentation_Layer

import android.content.Context
import android.os.Environment
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.e_book.presentation_Layer.Navigation.PdfUrl
import com.example.e_book.viewModels.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL


@Composable
fun BookScreen(

    viewModel: ViewModel = hiltViewModel(),
    navController: NavHostController
) {
    LaunchedEffect(true) {
        viewModel.getAllBooks()
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
                trackColor = Color.Green
            )
        }
    } else if (res.error.isNotEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = res.error)
        }
    } else if (res.items.isNotEmpty()) {
        val context = LocalContext.current
        var isScrolling by remember { mutableStateOf(false) }
        val scrollState = rememberScrollState()

        LaunchedEffect(isScrolling) {
            while (isScrolling) {
                scrollState.animateScrollTo(scrollState.maxValue, animationSpec = tween(durationMillis = 1500, easing = LinearEasing))
                delay(1000)
                scrollState.scrollTo(0) // Reset position
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            state = rememberLazyGridState(),
            contentPadding = PaddingValues(8.dp),
            reverseLayout = false,
            verticalArrangement = Arrangement.Top,
            horizontalArrangement = Arrangement.Start,
            flingBehavior = ScrollableDefaults.flingBehavior(),
            userScrollEnabled = true
        ) {
            items(res.items) { item ->
                var clicked by remember { mutableStateOf(false) }

                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .clickable {
                            clicked = !clicked
                        }
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                            .height(if (clicked) 200.dp else 120.dp),
                        shape = RoundedCornerShape(8.dp),
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
                                .padding(16.dp), // Padding inside the card
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = item.bookName,
                                style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .clickable {
                                        clicked = !clicked
                                        isScrolling = !isScrolling // Start scrolling on click
                                    }
                                    .horizontalScroll(scrollState)
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            if (clicked) {
                                Button(
                                    modifier = Modifier.padding(10.dp),
                                    onClick = {
                                        if(item.bookUrl.isNullOrEmpty()){
                                            Toast.makeText(context, "Book is currently not available", Toast.LENGTH_SHORT).show()
                                            clicked = !clicked
                                            Log.d("Test","$item.bookUr")
                                        }else {
                                            navController.navigate(
                                                PdfUrl(
                                                    item.bookUrl,
                                                    item.bookName
                                                )
                                            )
                                        }
                                    }
                                ) {
                                    Text(text = "Book Link")
                                }
                            }
                        }
                    }
                }
            }
        }

    }

}
