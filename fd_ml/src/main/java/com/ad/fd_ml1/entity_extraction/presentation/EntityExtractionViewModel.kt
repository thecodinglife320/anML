package com.ad.fd_ml1.entity_extraction.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ad.fd_ml1.entity_extraction.data.EntityExtractionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EntityExtractionViewModel @Inject constructor(
   private val repository: EntityExtractionRepository
) : ViewModel() {

   private val _input = mutableStateOf("")

   private val _result = mutableStateOf("")

   val input: State<String> = _input

   val result: State<String> = _result

   fun onInputChange(newInput: String) {
      _input.value = newInput
   }

   fun extraction() {
      viewModelScope.launch {
         repository.prepareExtractor()
         _result.value = repository.doExtraction(_input.value)
      }
   }

   override fun onCleared() {
      repository.closeModel()
   }

}