package com.ad.fd_ml1.entity_extraction.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EntityExtractionRepository @Inject constructor(
   private val mlkit: EntityExtractionMlkit
) {

   fun closeModel() = mlkit.closeExtractor()

   fun prepareExtractor() = mlkit.prepareExtractor()

   suspend fun doExtraction(input: String) =
      withContext(Dispatchers.IO) {
         mlkit.doExtraction(input)
      }
}