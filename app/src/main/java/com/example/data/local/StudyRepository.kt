package com.example.data.local

import kotlinx.coroutines.flow.Flow

class StudyRepository(private val studyDao: StudyDao) {
    val allStudyDays: Flow<List<StudyDay>> = studyDao.getAllStudyDays()
    val totalCount: Flow<Int> = studyDao.getTotalCount()

    suspend fun markDayStudied(dateString: String) {
        val studyDay = StudyDay(dateString = dateString, timestamp = System.currentTimeMillis())
        studyDao.insertStudyDay(studyDay)
    }

    suspend fun unmarkDay(dateString: String) {
        studyDao.deleteStudyDayByDate(dateString)
    }

    suspend fun isDayStudied(dateString: String): Boolean {
        return studyDao.getStudyDayByDate(dateString) != null
    }

    suspend fun clearAll() {
        studyDao.clearAll()
    }
}
