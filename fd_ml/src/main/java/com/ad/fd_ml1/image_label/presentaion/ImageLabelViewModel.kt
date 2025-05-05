package com.ad.fd_ml1.image_label.presentaion

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ad.fd_ml1.assetsToBitmap
import com.ad.fd_ml1.image_label.domain.ImageLabelUseCase
import com.google.mlkit.vision.label.ImageLabel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageLabelViewModel @Inject constructor(
   private val useCase: ImageLabelUseCase,
   @ApplicationContext private val context: Context
) :ViewModel(){

   private val bitmaps =
      listOf(
         "figure4-1.jpg",
         "osamu-tezuka2.jpg",
         "traicay.jpg",
         "osamu-tezuka.jpg",
         "deo-khau-trang.jpg"
      ).map {
         context.assetsToBitmap(it)
      }

   private val _index = mutableIntStateOf(0)

   private val _bitmap = mutableStateOf(
      bitmaps[_index.intValue]
   )

   private val _result = mutableStateOf("")

   val index: State<Int> get() = _index

   val bitmap: State<Bitmap?> get() = _bitmap

   val lastIndex = bitmaps.size-1

   val result:State<String> get()=_result

   fun labelImage()=
      run {
         viewModelScope.launch {
            try {
               _result.value = populateText(useCase(_bitmap.value!!))
            }catch (e:Exception){
               Log.e("vm",e.message.toString())
            }
         }
      }

   private fun populateText(imageLabels: List<ImageLabel>)=
      run {
         val stringBuilder = StringBuilder()
         imageLabels.forEach {
            stringBuilder.appendLine("${it.text} ${it.confidence}")
         }
         stringBuilder.toString()
      }

   fun goNextBitMap(){
      _index.intValue +=1
      _bitmap.value=bitmaps[_index.intValue]
   }

   fun goPreviousBitMap(){
      _index.intValue-=1
      _bitmap.value=bitmaps[_index.intValue]
   }

   override fun onCleared() {
      super.onCleared()
      useCase.closeLabeler()
   }
}
