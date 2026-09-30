package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.StudyRepository
import com.example.data.quotes.MotivationalQuotes
import com.example.data.quotes.Quote
import com.example.util.DateUtils
import com.example.util.DayItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class StreakUiState(
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val totalDaysStudied: Int = 0,
    val isStudiedToday: Boolean = false,
    val last7Days: List<DayItem> = emptyList(),
    val motivationalQuote: Quote = MotivationalQuotes.getRandomQuote(),
    val showCelebrationAnimation: Boolean = false,
    val infoMessage: String? = null
)

class StreakViewModel(
    private val repository: StudyRepository
) : ViewModel() {

    private val _currentQuote = MutableStateFlow(MotivationalQuotes.getRandomQuote())
    private val _celebrationTrigger = MutableStateFlow(false)
    private val _infoMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<StreakUiState> = combine(
        repository.allStudyDays,
        _currentQuote,
        _celebrationTrigger,
        _infoMessage
    ) { studyDays, quote, celebration, infoMsg ->
        val dateSet = studyDays.map { it.dateString }.toSet()
        val currentStreak = DateUtils.calculateCurrentStreak(dateSet)
        val bestStreak = DateUtils.calculateBestStreak(dateSet)
        val todayStr = DateUtils.getTodayDateString()
        val isTodayStudied = dateSet.contains(todayStr)
        val last7Days = DateUtils.getLast7Days(dateSet)

        StreakUiState(
            currentStreak = currentStreak,
            bestStreak = maxOf(bestStreak, currentStreak),
            totalDaysStudied = dateSet.size,
            isStudiedToday = isTodayStudied,
            last7Days = last7Days,
            motivationalQuote = quote,
            showCelebrationAnimation = celebration,
            infoMessage = infoMsg
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = StreakUiState()
    )

    /**
     * Acción principal: Hoy sí estudié.
     * Guarda la fecha de hoy en el teléfono, actualiza la racha y renueva el mensaje motivador.
     */
    fun markTodayAsStudied() {
        val today = DateUtils.getTodayDateString()
        viewModelScope.launch {
            repository.markDayStudied(today)
            _celebrationTrigger.value = true
            nextMotivationalQuote()
            _infoMessage.value = "¡Excelente! Día registrado y sumado a tu racha."
        }
    }

    /**
     * Alterna el estado del día de hoy (permite desmarcar si fue un error).
     */
    fun toggleToday() {
        val today = DateUtils.getTodayDateString()
        viewModelScope.launch {
            if (uiState.value.isStudiedToday) {
                repository.unmarkDay(today)
                _celebrationTrigger.value = false
                _infoMessage.value = "Registro de hoy cancelado."
            } else {
                markTodayAsStudied()
            }
        }
    }

    /**
     * Permite alternar un día específico de los últimos 7 días.
     */
    fun toggleDay(dateString: String) {
        viewModelScope.launch {
            val dateSet = repository.allStudyDays
            if (repository.isDayStudied(dateString)) {
                repository.unmarkDay(dateString)
                _infoMessage.value = "Día $dateString desmarcado."
            } else {
                repository.markDayStudied(dateString)
                _infoMessage.value = "Día $dateString marcado como estudiado."
            }
        }
    }

    /**
     * Cambia a un mensaje motivador distinto.
     */
    fun nextMotivationalQuote() {
        _currentQuote.value = MotivationalQuotes.getRandomQuote()
    }

    fun dismissInfoMessage() {
        _infoMessage.value = null
    }

    fun dismissCelebration() {
        _celebrationTrigger.value = false
    }
}

class StreakViewModelFactory(private val repository: StudyRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StreakViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StreakViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
