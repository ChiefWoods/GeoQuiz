package com.example.geoquiz

import androidx.lifecycle.ViewModel

private const val TAG = "ResultViewModel"

class ResultViewModel : ViewModel() {
    var totalQuestionsAnswered = 0
    var totalScore = 0
    var totalCheatAttempts = 0
}