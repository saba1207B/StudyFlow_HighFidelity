package com.example.data.repository

import com.example.data.model.NotificationItem
import com.example.data.model.NotificationType
import com.example.data.model.StudySession
import com.example.data.model.StudyStats
import com.example.data.model.Subject
import com.example.data.model.Task
import com.example.data.model.TaskPriority
import com.example.data.model.UserProfile

object StudyRepository {

    fun getDefaultSubjects(): List<Subject> = listOf(
        Subject(
            id = "sub_cs",
            name = "Computer Science",
            code = "CS-302",
            iconName = "computer",
            progressPercent = 91,
            taskCount = 4,
            colorHex = 0xFF059669, // Emerald
            nextActivity = "Algorithm Complexity Quiz",
            studyHours = 24.5f
        ),
        Subject(
            id = "sub_physics",
            name = "Physics",
            code = "PHY-201",
            iconName = "bolt",
            progressPercent = 82,
            taskCount = 3,
            colorHex = 0xFF0284C7, // Cyan/Sky
            nextActivity = "Thermodynamics Lab Report",
            studyHours = 18.0f
        ),
        Subject(
            id = "sub_math",
            name = "Mathematics",
            code = "MTH-240",
            iconName = "calculate",
            progressPercent = 68,
            taskCount = 5,
            colorHex = 0xFF4F46E5, // Indigo
            nextActivity = "Linear Algebra Problem Set 4",
            studyHours = 21.0f
        ),
        Subject(
            id = "sub_english",
            name = "English Literature",
            code = "ENG-110",
            iconName = "menu_book",
            progressPercent = 75,
            taskCount = 2,
            colorHex = 0xFFD97706, // Amber
            nextActivity = "Research Essay Draft",
            studyHours = 12.5f
        )
    )

    fun getDefaultTasks(): List<Task> = listOf(
        Task(
            id = "task_1",
            title = "Complete Calculus Problem Set #4",
            description = "Solve exercises 12 to 24 in Chapter 6: Integration by Parts.",
            subjectId = "sub_math",
            subjectName = "Mathematics",
            dueDate = "Today, 5:00 PM",
            priority = TaskPriority.HIGH,
            estimatedMinutes = 60,
            isCompleted = false
        ),
        Task(
            id = "task_2",
            title = "Review CS Data Structures Notes",
            description = "Prepare quick revision cards on AVL Trees and Red-Black Trees.",
            subjectId = "sub_cs",
            subjectName = "Computer Science",
            dueDate = "Today, 8:00 PM",
            priority = TaskPriority.MEDIUM,
            estimatedMinutes = 45,
            isCompleted = false
        ),
        Task(
            id = "task_3",
            title = "Physics Lab Prep: Thermodynamics",
            description = "Read experiment guide for adiabatic expansion measurements.",
            subjectId = "sub_physics",
            subjectName = "Physics",
            dueDate = "Tomorrow, 10:00 AM",
            priority = TaskPriority.HIGH,
            estimatedMinutes = 30,
            isCompleted = false
        ),
        Task(
            id = "task_4",
            title = "Draft English Comparative Essay",
            description = "Write 800-word introduction and first analytical argument.",
            subjectId = "sub_english",
            subjectName = "English Literature",
            dueDate = "Friday, 11:59 PM",
            priority = TaskPriority.LOW,
            estimatedMinutes = 90,
            isCompleted = false
        ),
        Task(
            id = "task_5",
            title = "CS Algorithm Complexity Quiz Prep",
            description = "Review asymptotic notations Big-O, Theta, and Omega examples.",
            subjectId = "sub_cs",
            subjectName = "Computer Science",
            dueDate = "Yesterday",
            priority = TaskPriority.HIGH,
            estimatedMinutes = 50,
            isCompleted = true
        ),
        Task(
            id = "task_6",
            title = "Read Physics Chapter 5 Summary",
            description = "Re-read key formulas on kinetic theory of gases.",
            subjectId = "sub_physics",
            subjectName = "Physics",
            dueDate = "2 days ago",
            priority = TaskPriority.LOW,
            estimatedMinutes = 30,
            isCompleted = true
        )
    )

    fun getDefaultSessions(): List<StudySession> = listOf(
        StudySession(
            id = "sess_1",
            subjectId = "sub_cs",
            subjectName = "Computer Science",
            taskTitle = "Tree Traversals & Graph Search",
            durationMinutes = 45,
            date = "Today • 10:30 AM",
            focusScore = 98
        ),
        StudySession(
            id = "sess_2",
            subjectId = "sub_math",
            subjectName = "Mathematics",
            taskTitle = "Matrix Eigenvalues & Eigenvectors",
            durationMinutes = 60,
            date = "Today • 2:15 PM",
            focusScore = 94
        ),
        StudySession(
            id = "sess_3",
            subjectId = "sub_physics",
            subjectName = "Physics",
            taskTitle = "Carnot Cycle Calculations",
            durationMinutes = 30,
            date = "Yesterday • 4:00 PM",
            focusScore = 90
        )
    )

    fun getDefaultNotifications(): List<NotificationItem> = listOf(
        NotificationItem(
            id = "notif_1",
            title = "Upcoming Task Due Soon",
            message = "Calculus Problem Set #4 is due today at 5:00 PM. Keep up the pace!",
            timeAgo = "15m ago",
            isRead = false,
            type = NotificationType.TASK
        ),
        NotificationItem(
            id = "notif_2",
            title = "Daily Study Goal Achieved! 🎉",
            message = "You have completed 2 hours and 15 minutes of focused study today!",
            timeAgo = "2h ago",
            isRead = false,
            type = NotificationType.STUDY_GOAL
        ),
        NotificationItem(
            id = "notif_3",
            title = "Weekly Progress Report",
            message = "You reached 72% of your 20-hour weekly study goal. Outstanding consistency!",
            timeAgo = "1d ago",
            isRead = true,
            type = NotificationType.WEEKLY_PROGRESS
        ),
        NotificationItem(
            id = "notif_4",
            title = "Focus Mode Reminder",
            message = "Ready for an afternoon deep work block? 25 minutes of CS study planned.",
            timeAgo = "2d ago",
            isRead = true,
            type = NotificationType.STUDY_REMINDER
        )
    )

    fun getDefaultStats(): StudyStats = StudyStats()

    fun getDefaultProfile(): UserProfile = UserProfile()
}
