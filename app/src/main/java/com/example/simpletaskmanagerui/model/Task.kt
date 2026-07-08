package com.example.simpletaskmanagerui.model

data class Task(
    val id: Int?,
    val title: String,
    val description: String,
    val completed: Boolean = false,
    val createdAt: String?,
    val completedAt: String?
)
