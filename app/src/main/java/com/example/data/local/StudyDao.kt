package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyDao {
    @Query("SELECT * FROM study_days ORDER BY dateString DESC")
    fun getAllStudyDays(): Flow<List<StudyDay>>

    @Query("SELECT * FROM study_days WHERE dateString = :dateString LIMIT 1")
    suspend fun getStudyDayByDate(dateString: String): StudyDay?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudyDay(studyDay: StudyDay)

    @Query("DELETE FROM study_days WHERE dateString = :dateString")
    suspend fun deleteStudyDayByDate(dateString: String)

    @Query("SELECT COUNT(*) FROM study_days")
    fun getTotalCount(): Flow<Int>

    @Query("DELETE FROM study_days")
    suspend fun clearAll()
}
