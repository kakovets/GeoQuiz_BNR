package com.kakovets.geoquiz

import android.app.Activity
import android.app.ActivityOptions
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProviders

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val KEY_OPENED = "index"
private const val REQUEST_CODE_CHEAT = 0
private const val MAX_OPENED_QUESTIONS = 3



class MainActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }
    override fun onResume() {
        super.onResume()
        if (quizViewModel.openedQuestions >= MAX_OPENED_QUESTIONS) {
            cheatButton.isEnabled = false
        }
        Log.d(TAG, "onResume() called")
    }
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }
    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }
    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart() called")
    }

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var cheatButton: Button
    private lateinit var questionTextView: TextView

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

//    private lateinit var listTextView: TextView
//    private lateinit var resultTextView: TextView
//    private lateinit var prevButton: Button

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState")
        outState.putInt(KEY_INDEX, quizViewModel.currentIndex)
        outState.putInt(KEY_OPENED, quizViewModel.openedQuestions)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate(Bundle?) called")

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX) ?: 0
        quizViewModel.currentIndex = currentIndex

        trueButton = findViewById(R.id.button_true)
        falseButton = findViewById(R.id.button_false)
        nextButton = findViewById(R.id.button_next)
        cheatButton = findViewById(R.id.button_cheat)
        questionTextView = findViewById(R.id.textView)

        updateQuestion()

        trueButton.setOnClickListener {
            checkAnswer(true)
        }
        falseButton.setOnClickListener {
            checkAnswer(false)
        }
        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }
        cheatButton.setOnClickListener { view ->

//            if (quizViewModel.openedQuestions < MAX_OPENED_QUESTIONS) {
                val answerIsTrue = quizViewModel.currentQuestionAnswer
                val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val options =
                        ActivityOptions.makeClipRevealAnimation(view, 0, 0, view.width, view.height)
                    startActivityForResult(intent, REQUEST_CODE_CHEAT, options.toBundle())
                } else {
                    startActivityForResult(intent, REQUEST_CODE_CHEAT)
                }
                quizViewModel.openedQuestions++
//            } else {
//                cheatButton.isEnabled = false
//            }





            }
    }
    @Deprecated("")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return
        if (requestCode == REQUEST_CODE_CHEAT) {
            quizViewModel.currentQuestionCheated = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
//            quizViewModel.isCheater = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }


    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val messageResId = when {
//            quizViewModel.isCheater -> R.string.toast_judgment
            quizViewModel.currentQuestionCheated -> R.string.toast_judgment
            userAnswer == correctAnswer -> R.string.toast_correct
            else -> R.string.toast_incorrect
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }
}