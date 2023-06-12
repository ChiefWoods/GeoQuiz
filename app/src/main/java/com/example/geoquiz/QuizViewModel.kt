package com.example.geoquiz

import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {
    private val questionBank = listOf(
        Question(R.string.question_australia, true, false),
        Question(R.string.question_oceans, true, false),
        Question(R.string.question_mideast, false, false),
        Question(R.string.question_africa, false, false),
        Question(R.string.question_americas, true, false),
        Question(R.string.question_asia, true, false),
    )

    var currentIndex = 0
    var isCheater = false
    var score = 0

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer
    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId
    val currentQuestionAnswered: Boolean
        get() = questionBank[currentIndex].isQuestionAnswered
    val questionBankSize: Int
        get() = questionBank.size

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrevious() {
        currentIndex = (currentIndex - 1) % questionBank.size
        if (currentIndex == -1) {
            currentIndex = 5
        }
    }

    fun updateAnswerState() {
        questionBank[currentIndex].isQuestionAnswered = true
    }

    fun allQuestionsAnswered(): Boolean {
        return questionBank.all { it.isQuestionAnswered }
    }

    fun countQuestionsAnswered(): Int {
        return questionBank.count { it.isQuestionAnswered }
    }

    fun incrementScore() {
        score++
    }

    fun resetScore() {
        score = 0
    }

    fun resetAnswerState() {
        for (Question in questionBank) {
            Question.isQuestionAnswered = false
        }
    }

    fun resetCurrentIndex() {
        currentIndex = 0
    }

    fun resetIsCheater() {
        isCheater = false
    }
}