package com.example.e_book.presentation_Layer

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.e_book.presentation_Layer.Navigation.Home
import com.example.e_book.presentation_Layer.Navigation.PdfUrl
import com.rizzi.bouquet.ResourceType
import com.rizzi.bouquet.VerticalPDFReader
import com.rizzi.bouquet.rememberVerticalPdfReaderState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfViewerScreen(pdfUrl: PdfUrl, navController: NavHostController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // State for PDF loading indicator
    var isLoading by remember { mutableStateOf(true) }

    // State for PDF Viewer
    val pdfState = pdfUrl.url?.let { url ->
        rememberVerticalPdfReaderState(
            resource = ResourceType.Remote(url),
            isZoomEnable = true
        )
    }

    var isDownloadEnabled by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
    ) {
        // TopAppBar
        TopAppBar(
            title = { Text(text = pdfUrl.bookName) },
            navigationIcon = {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back Button",
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .size(30.dp)
                        .clickable {
                            navController.navigate(Home)
                        },
                    tint = if(isSystemInDarkTheme() ) Color.White else Color.Black,
                )
            },
            actions = {
                if (isDownloadEnabled) {
                    IconButton(
                        onClick = {
                            pdfUrl.url?.let {
                                coroutineScope.launch {
                                    downloadPdf(context, it, pdfUrl.bookName)
                                }
                            }
                        },
                        content = {
                            Icon(
                                imageVector = Icons.Filled.Download,
                                contentDescription = "Download"
                            )
                        }
                    )
                }
            }
        )

        Row(modifier = Modifier.fillMaxSize()) {
            // Main PDF Viewer with Loading Indicator
            Box(modifier = Modifier.fillMaxSize()) {
                pdfState?.let {
                    VerticalPDFReader(
                        state = it,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    )

                    // Page Number Display at Bottom Center
                    Text(
                        text = "${it.currentPage} / ${it.pdfPageCount}",
                        color = Color.Black,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(16.dp)
                            .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    )
                }

                // Show Loading Indicator Until PDF Loads
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(50.dp),
                        color = Color.Red
                    )
                    Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show()

                }
            }
        }


        // Enable download and hide loading indicator when PDF is loaded
        pdfState?.let {
            LaunchedEffect(it.file) {
                if (it.file != null) {
                    isLoading = false
                    isDownloadEnabled = true
                }
            }
        }
    }
}


// ✅ Function to Download PDF and Open After Downloading
// ✅ Function to Download PDF and Show a Message After Completion
suspend fun downloadPdf(context: Context, pdfUrl: String, pdfName: String) {
    withContext(Dispatchers.IO) {
        try {
            val url = URL(pdfUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.connect()

            if (connection.responseCode == 200) {
                val inputStream = connection.inputStream
                val storageDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val pdfFile = File(storageDir, "$pdfName.pdf")

                val outputStream = FileOutputStream(pdfFile)
                inputStream.copyTo(outputStream)
                outputStream.close()
                inputStream.close()

                // Notify user after download completes
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "PDF Downloaded: ${pdfFile.absolutePath}", Toast.LENGTH_LONG).show()
                    openPdf(context, pdfFile)
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Download Failed. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}

// ✅ Function to Open Downloaded PDF
fun openPdf(context: Context, pdfFile: File) {
    val uri = Uri.fromFile(pdfFile)
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "application/pdf")
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NO_HISTORY
    }
    context.startActivity(intent)
}
