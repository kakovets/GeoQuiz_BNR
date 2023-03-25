package com.kakovets.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

private const val EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true"
const val EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown"


class CheatActivity : AppCompatActivity() {

    private lateinit var showAnswerButton: Button
    private lateinit var answerTextView: TextView
    private lateinit var textViewSDK: TextView
    private var answerIsTrue: Boolean = false
    private var isCheater: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        isCheater = savedInstanceState?.getBoolean("bool") ?: false
        if (isCheater) setAnswerShownResult(true)
        Log.d("TAG", "cre: $isCheater")


        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)

        showAnswerButton = findViewById(R.id.button_show_answer)
        answerTextView = findViewById(R.id.textView_answer)
        textViewSDK = findViewById(R.id.textView_SDK)

        showAnswerButton.setOnClickListener {
            val answerText = when {
                answerIsTrue -> R.string.button_true
                else -> R.string.button_false
            }
            answerTextView.setText(answerText)

            isCheater = true
            setAnswerShownResult(true)
            Log.d("TAG", "but: $isCheater")

            val sdk = Build.VERSION.SDK_INT
            textViewSDK.text = "API level is $sdk"

        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("TAG", "sav: $isCheater")
        outState.putBoolean("bool", isCheater)
    }


    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
        Log.d("TAG", "set: $isCheater")
        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }
}