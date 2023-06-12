package com.example.geoquiz

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

private const val EXTRA_TOTAL_QUESTIONS_ANSWERED = "com.bignerdranch.android.geoquiz.total_questions_answered"
private const val EXTRA_TOTAL_SCORE = "com.bignerdranch.android.geoquiz.total_score"
private const val EXTRA_TOTAL_CHEAT_ATTEMPTS = "com.bignerdranch.android.geoquiz.total_cheat_attempts"

class ResultActivity : AppCompatActivity() {
    private lateinit var totalQuestionsAnsweredText: TextView
    private lateinit var totalScoreText: TextView
    private lateinit var totalCheatAttemptsText: TextView

    private val resultViewModel: ResultViewModel by lazy {
        ViewModelProvider(this).get(ResultViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        totalQuestionsAnsweredText = findViewById(R.id.total_questions_answered)
        totalScoreText = findViewById(R.id.total_score)
        totalCheatAttemptsText = findViewById(R.id.total_cheat_attempts)

        totalQuestionsAnsweredText.text = "Total Questions Answered : " + intent.getIntExtra(EXTRA_TOTAL_QUESTIONS_ANSWERED, 0).toString()
        totalScoreText.text = "Total Score : " + intent.getIntExtra(EXTRA_TOTAL_SCORE, 0).toString() + "%"
        totalCheatAttemptsText.text = "Total Cheat Attempts : " + intent.getIntExtra(EXTRA_TOTAL_CHEAT_ATTEMPTS, 0).toString()
    }

    companion object {
        fun newIntent(packageContext: Context, totalQuestionsAnswered: Int, totalScore: Int, cheatAttempts: Int): Intent {
            return Intent(packageContext, ResultActivity::class.java).apply {
                putExtra(EXTRA_TOTAL_QUESTIONS_ANSWERED, totalQuestionsAnswered)
                putExtra(EXTRA_TOTAL_SCORE, totalScore)
                putExtra(EXTRA_TOTAL_CHEAT_ATTEMPTS, cheatAttempts)
            }
        }
    }
}