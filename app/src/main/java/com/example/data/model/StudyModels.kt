// Copyright (c) 2026 Sabareesh M (saba1207B). All Rights Reserved.
// All rights reserved. No public license is granted.

package com.example.data.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WarningAmber

enum class TaskPriority(val label: String, val color: Color) {
    HIGH("High", ErrorRed),
    MEDIUM("Medium", WarningAmber),
    LOW("Low", SuccessGreen)
}

data class Task(
    val id: String,
    val title: String,
    val description: String = "",
    val subjectId: String,
    val subjectName: String,
    val dueDate: String,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val estimatedMinutes: Int = 45,
    val isCompleted: Boolean = false
)

data class Subject(
    val id: String,
    val name: String,
    val code: String,
    val iconName: String,
    val progressPercent: Int, // e.g. 68
    val taskCount: Int,
    val colorHex: Long,
    val nextActivity: String = "",
    val studyHours: Float = 0f
)

data class StudySession(
    val id: String,
    val subjectId: String,
    val subjectName: String,
    val taskTitle: String,
    val durationMinutes: Int,
    val date: String,
    val focusScore: Int = 100
)

enum class NotificationType {
    TASK,
    STUDY_GOAL,
    WEEKLY_PROGRESS,
    STUDY_REMINDER
}

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timeAgo: String,
    val isRead: Boolean = false,
    val type: NotificationType = NotificationType.TASK
)

data class StudyStats(
    val dailyMinutes: Int = 135, // 2h 15m
    val weeklyMinutes: Int = 920, // ~15h 20m
    val weeklyGoalMinutes: Int = 1200, // 20h
    val completedTasksCount: Int = 14,
    val studyStreakDays: Int = 5,
    val weeklyDayValues: List<Pair<String, Float>> = listOf(
        "Mon" to 2.5f,
        "Tue" to 3.0f,
        "Wed" to 1.8f,
        "Thu" to 2.25f,
        "Fri" to 3.5f,
        "Sat" to 1.5f,
        "Sun" to 0.8f
    )
) {
    val streakDays: Int get() = studyStreakDays
}

data class UserProfile(
    val name: String = "Alex Morgan",
    val email: String = "alex.morgan@university.edu",
    val major: String = "Computer Science & Engineering",
    val semester: String = "Year 3 • Semester 5",
    val academicYear: String = "Year 3",
    val studentId: String = "STU-2024-8842",
    val studyStreakDays: Int = 5,
    val completedTasks: Int = 28,
    val totalHoursStudied: Float = 64.5f,
    val gpa: String = "3.85"
) {
    val totalStudyHours: Float get() = totalHoursStudied
    val streakDays: Int get() = studyStreakDays
}
