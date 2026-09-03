package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Subject
import com.example.data.model.Task
import com.example.ui.components.ProductivityHeroCard
import com.example.ui.components.SectionHeader
import com.example.ui.components.SubjectCard
import com.example.ui.components.TaskCard
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.VioletSecondary
import com.example.ui.viewmodel.BottomNavTab
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.StudyUiState
import com.example.ui.viewmodel.StudyViewModel

import com.example.ui.components.SleekCompactSubjectCard
import com.example.ui.components.SleekPrimaryButton
import com.example.ui.theme.Indigo700
import com.example.ui.theme.Indigo900
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900

@Composable
fun QuickActionButton(
    title: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag("quick_action_${title.lowercase().replace(" ", "_")}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate100)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp
                ),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
        }
    }
}

@Composable
fun HomeScreen(
    uiState: StudyUiState,
    viewModel: StudyViewModel,
    modifier: Modifier = Modifier
) {
    val pendingTasks = uiState.tasks.filterNot { it.isCompleted }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("home_screen"),
        contentPadding = PaddingValues(bottom = 28.dp)
    ) {
        // Sleek Interface Top Header with Gradient and Frosted Progress Ring
        item {
            ProductivityHeroCard(
                greeting = "Good morning, Alex!",
                dateString = "Wednesday, Oct 25",
                todayMinutes = uiState.stats.dailyMinutes,
                weeklyGoalPercent = 72,
                remainingTasksCount = pendingTasks.size,
                onStartFocus = {
                    viewModel.prepareFocusSession()
                }
            )
        }

        // Active Subjects Section (Sleek Interface layout)
        item {
            Spacer(modifier = Modifier.height(20.dp))
            SectionHeader(
                title = "Active Subjects",
                actionText = "See All",
                onActionClick = {
                    uiState.subjects.firstOrNull()?.let { viewModel.selectSubject(it) }
                }
            )
        }

        // Horizontal Sleek Compact Subject Cards
        item {
            Spacer(modifier = Modifier.height(4.dp))
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.subjects) { subject ->
                    SleekCompactSubjectCard(
                        subject = subject,
                        onClick = { viewModel.selectSubject(subject) },
                        modifier = Modifier.width(165.dp)
                    )
                }
            }
        }

        // Today's Tasks Section (Sleek Interface layout)
        item {
            Spacer(modifier = Modifier.height(20.dp))
            SectionHeader(
                title = "Today's Tasks",
                actionText = "Add Task",
                onActionClick = { viewModel.navigateTo(Screen.AddTask) }
            )
        }

        // Today's Task Items
        val todayPreviewTasks = uiState.tasks.take(3)
        if (todayPreviewTasks.isEmpty()) {
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Slate100)
                ) {
                    Text(
                        text = "All tasks completed! Great work today! 🎉",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Slate500,
                        modifier = Modifier.padding(20.dp)
                    )
                }
            }
        } else {
            items(todayPreviewTasks) { task ->
                Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)) {
                    TaskCard(
                        task = task,
                        onToggleComplete = { viewModel.toggleTaskCompleted(task.id) },
                        onClick = { viewModel.startEditTask(task) },
                        onDelete = { viewModel.deleteTask(task.id) }
                    )
                }
            }
        }

        // Sleek "Start Focus Session" Action Button
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                SleekPrimaryButton(
                    text = "Start Focus Session",
                    icon = Icons.Default.PlayArrow,
                    onClick = { viewModel.prepareFocusSession() }
                )
            }
        }

        // Quick Actions Navigation Row
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionButton(
                    title = "All Tasks",
                    icon = Icons.Default.Add,
                    color = IndigoPrimary,
                    onClick = { viewModel.setBottomTab(BottomNavTab.TASKS) },
                    modifier = Modifier.weight(1f)
                )
                QuickActionButton(
                    title = "Focus Timer",
                    icon = Icons.Default.PlayArrow,
                    color = VioletSecondary,
                    onClick = { viewModel.prepareFocusSession() },
                    modifier = Modifier.weight(1f)
                )
                QuickActionButton(
                    title = "Analytics",
                    icon = Icons.Default.Assessment,
                    color = AccentCyan,
                    onClick = { viewModel.setBottomTab(BottomNavTab.PROGRESS) },
                    modifier = Modifier.weight(1f)
                )
                QuickActionButton(
                    title = "Subjects",
                    icon = Icons.Default.MenuBook,
                    color = SuccessGreen,
                    onClick = {
                        uiState.subjects.firstOrNull()?.let { viewModel.selectSubject(it) }
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
