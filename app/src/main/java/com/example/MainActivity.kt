package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.components.StudyBottomNavigation
import com.example.ui.components.StudyTopAppBar
import com.example.ui.screens.AddEditTaskScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ProgressScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.StudySessionCompletedScreen
import com.example.ui.screens.StudySessionScreen
import com.example.ui.screens.SubjectDetailScreen
import com.example.ui.screens.TasksScreen
import com.example.ui.theme.StudyFlowTheme
import com.example.ui.viewmodel.BottomNavTab
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.StudyViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: StudyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val uiState by viewModel.uiState.collectAsState()

            StudyFlowTheme(darkTheme = uiState.isDarkMode) {
                StudyFlowApp(
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun StudyFlowApp(
    viewModel: StudyViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    // Handle back button press for nested sub-screens
    BackHandler(enabled = uiState.currentScreen != Screen.Main && uiState.currentScreen != Screen.Splash) {
        when (uiState.currentScreen) {
            Screen.SubjectDetail,
            Screen.AddTask,
            Screen.EditTask,
            Screen.FocusSession,
            Screen.SessionCompleted,
            Screen.Notifications,
            Screen.Settings -> viewModel.navigateTo(Screen.Main)
            Screen.SignUp,
            Screen.Login -> viewModel.navigateTo(Screen.Onboarding)
            Screen.Onboarding -> viewModel.navigateTo(Screen.Splash)
            else -> viewModel.navigateTo(Screen.Main)
        }
    }

    AnimatedContent(
        targetState = uiState.currentScreen,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "screen_transition"
    ) { screen ->
        when (screen) {
            Screen.Splash -> {
                SplashScreen(
                    onContinue = { viewModel.navigateTo(Screen.Onboarding) }
                )
            }

            Screen.Onboarding -> {
                OnboardingScreen(
                    onComplete = { viewModel.navigateTo(Screen.Login) }
                )
            }

            Screen.Login, Screen.SignUp -> {
                AuthScreen(
                    onAuthSuccess = { viewModel.navigateTo(Screen.Main) }
                )
            }

            Screen.SubjectDetail -> {
                val subject = uiState.selectedSubject ?: uiState.subjects.first()
                SubjectDetailScreen(
                    subject = subject,
                    uiState = uiState,
                    viewModel = viewModel,
                    onBack = { viewModel.navigateTo(Screen.Main) }
                )
            }

            Screen.AddTask, Screen.EditTask -> {
                AddEditTaskScreen(
                    editingTask = if (screen == Screen.EditTask) uiState.editingTask else null,
                    subjects = uiState.subjects,
                    viewModel = viewModel,
                    onBack = { viewModel.navigateTo(Screen.Main) }
                )
            }

            Screen.FocusSession -> {
                StudySessionScreen(
                    uiState = uiState,
                    viewModel = viewModel,
                    onBack = { viewModel.navigateTo(Screen.Main) }
                )
            }

            Screen.SessionCompleted -> {
                StudySessionCompletedScreen(
                    sessionData = uiState.completedSession,
                    viewModel = viewModel,
                    onContinueHome = { viewModel.navigateTo(Screen.Main) }
                )
            }

            Screen.Notifications -> {
                NotificationsScreen(
                    notifications = uiState.notifications,
                    viewModel = viewModel,
                    onBack = { viewModel.navigateTo(Screen.Main) }
                )
            }

            Screen.Settings -> {
                SettingsScreen(
                    uiState = uiState,
                    viewModel = viewModel,
                    onBack = { viewModel.navigateTo(Screen.Main) }
                )
            }

            Screen.Main -> {
                MainAppScaffold(
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun MainAppScaffold(
    viewModel: StudyViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val unreadNotificationsCount = uiState.notifications.count { !it.isRead }

    Scaffold(
        topBar = {
            val (title, subtitle) = when (uiState.currentBottomTab) {
                BottomNavTab.HOME -> Pair("StudyFlow", "Welcome back, Alex")
                BottomNavTab.TASKS -> Pair("Tasks & Goals", "Stay on schedule")
                BottomNavTab.STUDY -> Pair("Focus Zone", "Deep work session")
                BottomNavTab.PROGRESS -> Pair("Analytics & Insights", "Academic progress")
                BottomNavTab.PROFILE -> Pair("Student Profile", "Computer Science • Year 3")
            }
            StudyTopAppBar(
                title = title,
                subtitle = subtitle,
                unreadNotificationsCount = unreadNotificationsCount,
                onNotificationClick = { viewModel.navigateTo(Screen.Notifications) },
                onSettingsClick = { viewModel.navigateTo(Screen.Settings) }
            )
        },
        bottomBar = {
            StudyBottomNavigation(
                selectedTab = uiState.currentBottomTab,
                onTabSelected = { tab ->
                    if (tab == BottomNavTab.STUDY) {
                        viewModel.prepareFocusSession()
                    } else {
                        viewModel.setBottomTab(tab)
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.currentBottomTab) {
                BottomNavTab.HOME -> HomeScreen(uiState = uiState, viewModel = viewModel)
                BottomNavTab.TASKS -> TasksScreen(uiState = uiState, viewModel = viewModel)
                BottomNavTab.STUDY -> StudySessionScreen(
                    uiState = uiState,
                    viewModel = viewModel,
                    onBack = { viewModel.setBottomTab(BottomNavTab.HOME) }
                )
                BottomNavTab.PROGRESS -> ProgressScreen(uiState = uiState)
                BottomNavTab.PROFILE -> ProfileScreen(profile = uiState.profile, viewModel = viewModel)
            }
        }
    }
}
