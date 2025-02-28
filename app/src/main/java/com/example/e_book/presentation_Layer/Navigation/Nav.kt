package com.example.e_book.presentation_Layer.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.e_book.presentation_Layer.BookByCategoryScreen
import com.example.e_book.presentation_Layer.MainScreen
import com.example.e_book.presentation_Layer.PdfViewerScreen
import kotlinx.serialization.Serializable

@Composable
fun Nav(modifier: Modifier) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Home
    ) {
        composable<Home>{
            MainScreen(modifier=modifier,navController=navController)
        }

        composable<PdfUrl>{ backStackEntry ->
            val pdfUrl : PdfUrl = backStackEntry.toRoute()
            PdfViewerScreen(pdfUrl = pdfUrl,navController=navController)
        }

        composable<BookByCategory> { backStackEntry ->
            val category : BookByCategory = backStackEntry.toRoute()
            BookByCategoryScreen(category,navController=navController)
        }
    }

}




@Serializable
object Home

@Serializable
data class  PdfUrl(val url : String? = null, val bookName : String)

@Serializable
data class BookByCategory(val category: String)