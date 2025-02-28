package com.example.e_book.presentation_Layer

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.e_book.presentation_Layer.Navigation.PdfUrl
import com.example.e_book.viewModels.ViewModel

@Composable
fun MainScreen(modifier: Modifier, navController: NavHostController) {

    val pagerState = rememberPagerState(
        pageCount = { 2 }
    )
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {

        Tabs(pagerState)
        TabContent(pagerState, navController)

    }
}

data class TabItem(
    var title: String,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector
)

@Composable
fun Tabs(pagerState: PagerState) {

    val tabItems = listOf(

        TabItem(
            title = "Category",
            unselectedIcon = Icons.Outlined.Category,
            selectedIcon = Icons.Filled.Category
        ),
        TabItem(
            title = "All Books",
            unselectedIcon = Icons.Outlined.Book,
            selectedIcon = Icons.Filled.Book
        ),
    )

    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }
    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            selectedTabIndex = pagerState.currentPage
        }

    }

    TabRow(
        selectedTabIndex = selectedTabIndex
    ) {
        tabItems.forEachIndexed { index, tabItem ->

            Tab(
                selected = selectedTabIndex == index,
                onClick = {
                    selectedTabIndex = index
                },
                text = {
                    Text(text = tabItem.title)
                },
                icon = {
                    Icon(
                        imageVector = if (index == selectedTabIndex) {
                            tabItem.selectedIcon
                        } else {
                            tabItem.unselectedIcon
                        },
                        contentDescription = tabItem.title
                    )

                }

            )
        }
    }

}

@Composable
fun TabContent(
    pagerState: PagerState,
    navController: NavHostController,
) {

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
    ) { index ->
        when (index) {
            0 -> {
                CategoryScreen(navController = navController)
            }

            1 -> {
                BookScreen(navController = navController)
            }

        }
    }
}







