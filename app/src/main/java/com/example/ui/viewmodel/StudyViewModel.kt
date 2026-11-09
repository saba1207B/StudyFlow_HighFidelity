// Copyright (c) 2026 Sabareesh M (saba1207B). All Rights Reserved.
// All rights reserved. No public license is granted.

package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.NotificationItem
import com.example.data.model.StudySession
import com.example.data.model.StudyStats
import com.example.data.model.Subject
import com.example.data.model.Task
import com.example.data.model.TaskPriority
import com.example.data.model.UserProfile
import com.example.data.repository.StudyRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object SignUp : Screen("sign_up")
    object Main : Screen("main")
    object SubjectDetail : Screen("subject_detail")
    object AddTask : Screen("add_task")
    object EditTask : Screen("edit_task")
    object FocusSession : Screen("focus_session")
    object SessionCompleted : Screen("session_completed")
    object Notifications : Screen("notifications")
    object Settings : Screen("settings")
}

enum class BottomNavTab(val title: String) {
    HOME("Home"),
    TASKS("Tasks"),
    STUDY("Study"),
    PROGRESS("Progress"),
    PROFILE("Profile")
}

data class CompletedSessionData(
    val subjectName: String,
    val taskTitle: String,
    val durationMinutes: Int,
    val xpGained: Int = 50,
    val focusScore: Int = 100
)

data class StudyUiState(
    val currentScreen: Screen = Screen.Splash,
    val currentBottomTab: BottomNavTab = BottomNavTab.HOME,
    val tasks: List<Task> = emptyList(),
    val subjects: List<Subject> = emptyList(),
    val sessions: List<StudySession> = emptyList(),
    val notifications: List<NotificationItem> = emptyList(),
    val stats: StudyStats = StudyStats(),
    val profile: UserProfile = UserProfile(),
    val selectedSubject: Subject? = null,
    val editingTask: Task? = null,
    // Focus Session State
    val focusSubject: Subject? = null,
    val focusTask: Task? = null,
    val focusDurationMinutes: Int = 25,
    val remainingSeconds: Int = 25 * 60,
    val isTimerRunning: Boolean = false,
    val completedSession: CompletedSessionData? = null,
    // Settings & Preferences
    val isDarkMode: Boolean = false,
    val studyRemindersEnabled: Boolean = true,
    val soundEnabled: Boolean = true,
    val dailyGoalHours: Float = 3.0f,
    // Task Filter & Search
    val taskFilter: String = "All",
    val taskSearchQuery: String = ""
)

class StudyViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        StudyUiState(
            tasks = StudyRepository.getDefaultTasks(),
            subjects = StudyRepository.getDefaultSubjects(),
            sessions = StudyRepository.getDefaultSessions(),
            notifications = StudyRepository.getDefaultNotifications(),
            stats = StudyRepository.getDefaultStats(),
            profile = StudyRepository.getDefaultProfile(),
            selectedSubject = StudyRepository.getDefaultSubjects().firstOrNull(),
            focusSubject = StudyRepository.getDefaultSubjects().firstOrNull()
        )
    )
    val uiState: StateFlow<StudyUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    // Navigation
    fun navigateTo(screen: Screen) {
        _uiState.update { it.copy(currentScreen = screen) }
    }

    fun setBottomTab(tab: BottomNavTab) {
        _uiState.update {
            it.copy(
                currentBottomTab = tab,
                currentScreen = Screen.Main
            )
        }
    }

    // Task Actions
    fun addTask(
        title: String,
        description: String,
        subjectId: String,
        dueDate: String,
        priority: TaskPriority,
        estimatedMinutes: Int
    ) {
        val subject = _uiState.value.subjects.find { it.id == subjectId }
        val subjectName = subject?.name ?: "General"
        val newTask = Task(
            id = "task_${UUID.randomUUID().toString().take(8)}",
            title = title.trim(),
            description = description.trim(),
            subjectId = subjectId,
            subjectName = subjectName,
            dueDate = dueDate.ifBlank { "Tomorrow" },
            priority = priority,
            estimatedMinutes = estimatedMinutes,
            isCompleted = false
        )
        _uiState.update { state ->
            val updatedTasks = listOf(newTask) + state.tasks
            state.copy(
                tasks = updatedTasks,
                currentScreen = Screen.Main
            )
        }
    }

    fun updateTask(
        id: String,
        title: String,
        description: String,
        subjectId: String,
        dueDate: String,
        priority: TaskPriority,
        estimatedMinutes: Int
    ) {
        val subject = _uiState.value.subjects.find { it.id == subjectId }
        val subjectName = subject?.name ?: "General"

        _uiState.update { state ->
            val updated = state.tasks.map { task ->
                if (task.id == id) {
                    task.copy(
                        title = title.trim(),
                        description = description.trim(),
                        subjectId = subjectId,
                        subjectName = subjectName,
                        dueDate = dueDate,
                        priority = priority,
                        estimatedMinutes = estimatedMinutes
                    )
                } else task
            }
            state.copy(
                tasks = updated,
                editingTask = null,
                currentScreen = Screen.Main
            )
        }
    }

    fun toggleTaskCompleted(taskId: String) {
        _uiState.update { state ->
            var completedCountChange = 0
            val updated = state.tasks.map { task ->
                if (task.id == taskId) {
                    val nextState = !task.isCompleted
                    completedCountChange = if (nextState) 1 else -1
                    task.copy(isCompleted = nextState)
                } else task
            }
            val newStats = state.stats.copy(
                completedTasksCount = (state.stats.completedTasksCount + completedCountChange).coerceAtLeast(0)
            )
            state.copy(tasks = updated, stats = newStats)
        }
    }

    fun deleteTask(taskId: String) {
        _uiState.update { state ->
            val updated = state.tasks.filterNot { it.id == taskId }
            state.copy(
                tasks = updated,
                editingTask = null,
                currentScreen = if (state.currentScreen == Screen.EditTask) Screen.Main else state.currentScreen
            )
        }
    }

    fun startEditTask(task: Task) {
        _uiState.update {
            it.copy(
                editingTask = task,
                currentScreen = Screen.EditTask
            )
        }
    }

    fun setTaskFilter(filter: String) {
        _uiState.update { it.copy(taskFilter = filter) }
    }

    fun setTaskSearchQuery(query: String) {
        _uiState.update { it.copy(taskSearchQuery = query) }
    }

    // Subject Actions
    fun selectSubject(subject: Subject) {
        _uiState.update {
            it.copy(
                selectedSubject = subject,
                currentScreen = Screen.SubjectDetail
            )
        }
    }

    // Study Focus Session
    fun prepareFocusSession(subject: Subject? = null, task: Task? = null, durationMinutes: Int = 25) {
        _uiState.update { state ->
            val sub = subject ?: state.subjects.firstOrNull()
            state.copy(
                focusSubject = sub,
                focusTask = task,
                focusDurationMinutes = durationMinutes,
                remainingSeconds = durationMinutes * 60,
                isTimerRunning = false,
                currentScreen = Screen.FocusSession
            )
        }
    }

    fun setFocusDuration(minutes: Int) {
        if (!_uiState.value.isTimerRunning) {
            _uiState.update {
                it.copy(
                    focusDurationMinutes = minutes,
                    remainingSeconds = minutes * 60
                )
            }
        }
    }

    fun toggleTimer() {
        val isCurrentlyRunning = _uiState.value.isTimerRunning
        if (isCurrentlyRunning) {
            pauseTimer()
        } else {
            startTimer()
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        _uiState.update { it.copy(isTimerRunning = true) }
        timerJob = viewModelScope.launch {
            while (_uiState.value.isTimerRunning && _uiState.value.remainingSeconds > 0) {
                delay(1000L)
                _uiState.update { state ->
                    val nextRemaining = state.remainingSeconds - 1
                    if (nextRemaining <= 0) {
                        completeFocusSession()
                        state.copy(remainingSeconds = 0, isTimerRunning = false)
                    } else {
                        state.copy(remainingSeconds = nextRemaining)
                    }
                }
            }
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()
        _uiState.update { it.copy(isTimerRunning = false) }
    }

    fun resetTimer() {
        timerJob?.cancel()
        _uiState.update {
            it.copy(
                isTimerRunning = false,
                remainingSeconds = it.focusDurationMinutes * 60
            )
        }
    }

    fun completeFocusSession() {
        timerJob?.cancel()
        val state = _uiState.value
        val duration = state.focusDurationMinutes
        val subjectName = state.focusSubject?.name ?: "General Study"
        val taskTitle = state.focusTask?.title ?: "Independent Focus Session"

        val completed = CompletedSessionData(
            subjectName = subjectName,
            taskTitle = taskTitle,
            durationMinutes = duration,
            xpGained = duration * 2,
            focusScore = 98
        )

        // Mark associated task as complete if applicable
        state.focusTask?.let { t ->
            toggleTaskCompleted(t.id)
        }

        // Add to sessions
        val newSession = StudySession(
            id = "sess_${UUID.randomUUID().toString().take(6)}",
            subjectId = state.focusSubject?.id ?: "gen",
            subjectName = subjectName,
            taskTitle = taskTitle,
            durationMinutes = duration,
            date = "Just now",
            focusScore = 98
        )

        val updatedStats = state.stats.copy(
            dailyMinutes = state.stats.dailyMinutes + duration,
            weeklyMinutes = state.stats.weeklyMinutes + duration
        )

        _uiState.update {
            it.copy(
                isTimerRunning = false,
                completedSession = completed,
                sessions = listOf(newSession) + it.sessions,
                stats = updatedStats,
                currentScreen = Screen.SessionCompleted
            )
        }
    }

    // Settings
    fun toggleDarkMode(enabled: Boolean) {
        _uiState.update { it.copy(isDarkMode = enabled) }
    }

    fun toggleStudyReminders(enabled: Boolean) {
        _uiState.update { it.copy(studyRemindersEnabled = enabled) }
    }

    fun toggleSound(enabled: Boolean) {
        _uiState.update { it.copy(soundEnabled = enabled) }
    }

    fun setDailyGoal(hours: Float) {
        _uiState.update { it.copy(dailyGoalHours = hours) }
    }

    // Notifications
    fun markAllNotificationsAsRead() {
        _uiState.update { state ->
            val updated = state.notifications.map { it.copy(isRead = true) }
            state.copy(notifications = updated)
        }
    }

    fun toggleNotificationRead(id: String) {
        _uiState.update { state ->
            val updated = state.notifications.map {
                if (it.id == id) it.copy(isRead = !it.isRead) else it
            }
            state.copy(notifications = updated)
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
