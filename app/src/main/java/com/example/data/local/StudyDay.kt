package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representa un día de estudio registrado.
 * dateString tiene formato "yyyy-MM-dd" para unicidad por día.
 */
@Entity(tableName = "study_days")
data class StudyDay(
    @PrimaryKey
    val dateString: String,
    val timestamp: Long = System.currentTimeMillis(),
    val notes: String = ""
)
