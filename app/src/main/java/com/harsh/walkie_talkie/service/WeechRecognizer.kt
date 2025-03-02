package com.harsh.walkie_talkie.service

import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import java.util.Locale

@Composable
fun WeechRecognizer(
    audioManager: AudioManager,
    speechRecognizer: SpeechRecognizer,
    result: (ArrayList<String>) -> Unit
) {
    val recognizerIntent = remember {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        }
    }

    val recognitionListener = object : RecognitionListener {
        override fun onResults(results: Bundle?) {
            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            result(matches!!)
            restartListening(audioManager, speechRecognizer, recognizerIntent)
        }

        override fun onPartialResults(partialResults: Bundle?) {
        }

        override fun onError(error: Int) {
            restartListening(audioManager, speechRecognizer, recognizerIntent)
        }

        override fun onReadyForSpeech(params: Bundle?) {}
        override fun onBeginningOfSpeech() {}
        override fun onRmsChanged(rmsdB: Float) {}
        override fun onBufferReceived(buffer: ByteArray?) {}
        override fun onEndOfSpeech() {
            restartListening(audioManager, speechRecognizer, recognizerIntent)
        }

        override fun onEvent(eventType: Int, params: Bundle?) {}
    }

    LaunchedEffect(Unit) {
        speechRecognizer.setRecognitionListener(recognitionListener)
        restartListening(audioManager, speechRecognizer, recognizerIntent)
    }

    DisposableEffect(Unit) {
        onDispose {
            speechRecognizer.destroy()
        }
    }
}

private fun restartListening(
    audioManager: AudioManager,
    speechRecognizer: SpeechRecognizer,
    intent: Intent
) {
    audioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_MUTE, 0)
    Handler(Looper.getMainLooper()).postDelayed({
        speechRecognizer.startListening(intent)
    }, 500)
}

fun stopListening(audioManager: AudioManager, speechRecognizer: SpeechRecognizer) {
    Handler(Looper.getMainLooper()).postDelayed({
        speechRecognizer.stopListening()
        Handler(Looper.getMainLooper()).postDelayed({
            audioManager.adjustStreamVolume(
                AudioManager.STREAM_NOTIFICATION,
                AudioManager.ADJUST_UNMUTE,
                0
            )
        }, 1000)
    }, 500)
}