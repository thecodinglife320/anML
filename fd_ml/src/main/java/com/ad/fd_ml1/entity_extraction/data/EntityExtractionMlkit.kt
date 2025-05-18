package com.ad.fd_ml1.entity_extraction.data

import com.google.mlkit.nl.entityextraction.Entity
import com.google.mlkit.nl.entityextraction.EntityAnnotation
import com.google.mlkit.nl.entityextraction.EntityExtraction
import com.google.mlkit.nl.entityextraction.EntityExtractionParams
import com.google.mlkit.nl.entityextraction.EntityExtractorOptions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class EntityExtractionMlkit @Inject constructor() {

   private val entityExtractor = EntityExtraction
      .getClient(
         EntityExtractorOptions.Builder(EntityExtractorOptions.ENGLISH)
            .build()
      )

   private var extractorAvailable: Boolean = false

   fun prepareExtractor() {
      entityExtractor.downloadModelIfNeeded().addOnSuccessListener {
         extractorAvailable = true
      }
         .addOnFailureListener {
            extractorAvailable = false
         }
   }

   @OptIn(ExperimentalCoroutinesApi::class)
   suspend fun doExtraction(input: String) =
      suspendCancellableCoroutine {
         if (extractorAvailable) {
            val params = EntityExtractionParams
               .Builder(input)
               .build()
            var outputString = ""
            entityExtractor.annotate(params)
               .addOnSuccessListener { result: List<EntityAnnotation> ->
                  for (entityAnnotation in result) {
                     outputString += entityAnnotation.annotatedText
                     for (entity in entityAnnotation.entities) {
                        outputString += ": " + getStringFor(entity)
                     }
                     outputString += "\n\n"
                  }
                  it.resume(outputString)
               }
               .addOnFailureListener {
               }
         }
         it.invokeOnCancellation {
            entityExtractor.close()
         }
      }

   fun closeExtractor() = entityExtractor.close()

   private fun getStringFor(entity: Entity): String {
      var returnVal = "Type - "
      returnVal += when (entity.type) {
         Entity.TYPE_ADDRESS -> "Address"
         Entity.TYPE_DATE_TIME -> "DateTime"
         Entity.TYPE_EMAIL -> "Email Address"
         Entity.TYPE_FLIGHT_NUMBER -> "Flight Number"
         Entity.TYPE_IBAN -> "IBAN"
         Entity.TYPE_ISBN -> "ISBN"
         Entity.TYPE_MONEY -> "Money"
         Entity.TYPE_PAYMENT_CARD -> "Credit/Debit Card"
         Entity.TYPE_PHONE -> "Phone Number"
         Entity.TYPE_TRACKING_NUMBER -> "Tracking Number"
         Entity.TYPE_URL -> "URL"
         else -> "Address"
      }
      return returnVal
   }
}