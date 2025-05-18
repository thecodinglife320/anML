package com.ad.fd_ml1.handwriting.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ad.fd_ml1.handwriting.data.InkRecognitionRepository
import com.google.mlkit.vision.digitalink.Ink
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InkRecognitionViewModel @Inject constructor(
   private val repository: InkRecognitionRepository
) : ViewModel() {

   private val _results = mutableStateOf("")
   val results: State<String> = _results

   private var ink: Ink? = null

   fun recognize() {
      val stringBuilder = StringBuilder("")
      viewModelScope.launch {
         ink?.let {
            repository.recognize(it).forEach {
               stringBuilder.appendLine(it.text)
            }
            _results.value = stringBuilder.toString()
         }
      }
   }

   fun onTouchUp(ink: Ink) {
      this.ink = ink
      println(ink)
   }

   override fun onCleared() {
      repository.closeModel()
   }
}