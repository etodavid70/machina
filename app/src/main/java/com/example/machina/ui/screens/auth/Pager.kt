package com.example.machina.ui.screens.auth
import RequestNotificationPermission
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.machina.data.model.onboarding_models.PagerModel
import com.example.machina.utils.saveOnboardingSeen
import kotlinx.coroutines.launch
import com.example.machina.utils.NotificationHelper

@Composable
fun Pager(
    onFinish: () -> Unit // Callback to navigate to main screen
) {

    RequestNotificationPermission()
    val context = LocalContext.current
    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()

    val pages = listOf(
        PagerModel.PagerScreen1,
        PagerModel.PagerScreen2,
        PagerModel.PagerScreen3,
    )

    HorizontalPager(state = pagerState) { page ->
        PagerScreenUI(
            onboardingModel = pages[page],
            currentPage = page,
            totalPages = pages.size,
            onContinue = {
                coroutineScope.launch {
                    if (page < pages.lastIndex) {
                        pagerState.animateScrollToPage(page + 1)
                    } else {
                        saveOnboardingSeen(context)
                        onFinish()
                    }
                }
            },
            onSkip = {
                saveOnboardingSeen(context)
                onFinish()
            }
        )
    }
}
