package com.example.geoquiz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var previousButton: Button
    private lateinit var cheatButton: Button
    private lateinit var resetButton: Button
    private lateinit var questionTextView: TextView
    private lateinit var questionStatus: TextView

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        val isCheater = savedInstanceState?.getBoolean(KEY_IS_CHEATER, false) ?: false

        Log.i("TEST", savedInstanceState?.getBoolean(KEY_IS_CHEATER).toString())

        quizViewModel.isCheater = isCheater
        quizViewModel.currentIndex = currentIndex

        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        previousButton = findViewById(R.id.previous_button)
        cheatButton = findViewById(R.id.cheat_button)
        resetButton = findViewById(R.id.reset_button)
        questionTextView = findViewById(R.id.question_text_view)
        questionStatus = findViewById(R.id.question_status)

        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
        }

        falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
        }

        nextButton.setOnClickListener { view: View ->
            quizViewModel.moveToNext()
            updateQuestion()
            toggleAnswerButtons()
        }

        previousButton.setOnClickListener { view: View ->
            quizViewModel.moveToPrevious()
            updateQuestion()
            toggleAnswerButtons()
        }

        cheatButton.setOnClickListener {
            //val intent = Intent(this, CheatActivity::class.java)
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }

        resetButton.setOnClickListener {
            quizViewModel.resetScore()
            quizViewModel.resetAnswerState()
            quizViewModel.resetIsCheater()
            quizViewModel.resetCurrentIndex()
            updateQuestion()
            changeButtonState()

            Toast.makeText(
                this,
                "Quiz successfully reset",
                Toast.LENGTH_SHORT
            ).show()
        }

        updateQuestion()
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    /**
     * Dispatch onPause() to fragments.
     */
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are *not* resumed.
     */
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            quizViewModel.isCheater = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)

        questionStatus.text = "Question " + (quizViewModel.currentIndex + 1).toString() + " of " + quizViewModel.questionBankSize.toString()
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer

        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }

        Toast.makeText(
            this,
            messageResId,
            Toast.LENGTH_SHORT
        ).show()

        if (userAnswer == correctAnswer) {
            quizViewModel.incrementScore()
        }

        quizViewModel.updateAnswerState()
        toggleAnswerButtons()
        displayScoreToast()
    }

    private fun toggleAnswerButtons() {
        if (quizViewModel.currentQuestionAnswered) {
            trueButton.isEnabled = false
            falseButton.isEnabled = false
        } else {
            trueButton.isEnabled = true
            falseButton.isEnabled = true
        }
    }

    private fun displayScoreToast() {
        if (quizViewModel.allQuestionsAnswered()) {
            val scorePercentage = (quizViewModel.score.toDouble() / 6.toDouble() * 100).toInt()
            val scoreMessage = "Quiz completed! You scored $scorePercentage%"

            Toast.makeText(
                this,
                scoreMessage,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}