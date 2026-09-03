package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.data.model.Task
import com.example.ui.components.FilterChipGroup
import com.example.ui.components.StudySearchBar
import com.example.ui.components.TaskCard
import com.example.ui.theme.IndigoPrimary
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.StudyUiState
import com.example.ui.viewmodel.StudyViewModel

@Composable
fun TasksScreen(
    uiState: StudyUiState,
    viewModel: StudyViewModel,
    modifier: Modifier = Modifier
) {
    val filterOptions = listOf("All", "Today", "Upcoming", "Completed")

    // Filter and search logic
    val filteredTasks = uiState.tasks.filter { task ->
        val matchesFilter = when (uiState.taskFilter) {
            "Today" -> task.dueDate.contains("Today", ignoreCase = true) && !task.isCompleted
            "Upcoming" -> !task.dueDate.contains("Today", ignoreCase = true) && !task.isCompleted
            "Completed" -> task.isCompleted
            else -> true
        }
        val matchesQuery = if (uiState.taskSearchQuery.isBlank()) {
            true
        } else {
            task.title.contains(uiState.taskSearchQuery, ignoreCase = true) ||
                    task.subjectName.contains(uiState.taskSearchQuery, ignoreCase = true) ||
                    task.description.contains(uiState.taskSearchQuery, ignoreCase = true)
        }
        matchesFilter && matchesQuery
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.navigateTo(Screen.AddTask) },
                containerColor = IndigoPrimary,
                contentColor = Color.White,
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.testTag("tasks_fab_add")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Task"
                )
            }
        },
        modifier = modifier.testTag("tasks_screen")
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            // Search Bar
            item {
                Spacer(modifier = Modifier.height(8.dp))
                StudySearchBar(
                    query = uiState.taskSearchQuery,
                    onQueryChange = { viewModel.setTaskSearchQuery(it) }
                )
            }

            // Filter Chips
            item {
                Spacer(modifier = Modifier.height(14.dp))
                FilterChipGroup(
                    options = filterOptions,
                    selectedOption = uiState.taskFilter,
                    onOptionSelected = { viewModel.setTaskFilter(it) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Task list count summary
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${uiState.taskFilter} Tasks",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "${filteredTasks.size} items",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Empty state or Task list
            if (filteredTasks.isEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(40.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            modifier = Modifier.padding(12.dp),
                            shape = RoundedCornerShape(20.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = if (uiState.taskSearchQuery.isNotEmpty()) "No tasks matching \"${uiState.taskSearchQuery}\"" else "No tasks in this category",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Tap the + button to create a new task with due date and priority.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            } else {
                items(filteredTasks, key = { it.id }) { task ->
                    Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)) {
                        TaskCard(
                            task = task,
                            onToggleComplete = { viewModel.toggleTaskCompleted(task.id) },
                            onClick = { viewModel.startEditTask(task) },
                            onDelete = { viewModel.deleteTask(task.id) }
                        )
                    }
                }
            }
        }
    }
}
