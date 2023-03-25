package com.kakovets.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel: ViewModel() {

    var currentIndex = 0
    var isCheater = false
    private val questionBank = listOf(
        Question(R.string.question_australia, answer = true, cheated = false),
        Question(R.string.question_oceans, answer = true, cheated = false),
        Question(R.string.question_mideast, answer = false, cheated = false),
        Question(R.string.question_africa, answer = false, cheated = false),
        Question(R.string.question_americas, answer = true, cheated = false),
        Question(R.string.question_asia, answer = true, cheated = false)
    )

    val currentQuestionText: Int get() = questionBank[currentIndex].textResId
    val currentQuestionAnswer: Boolean get() = questionBank[currentIndex].answer
    var currentQuestionCheated: Boolean
        get() = questionBank[currentIndex].cheated
        set(value) {questionBank[currentIndex].cheated = value}
    var openedQuestions = 0
    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }



    init {
        Log.d(TAG, "ViewModel instance created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel instance about to be destroyed")
    }
}