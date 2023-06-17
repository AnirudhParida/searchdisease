package com.example.searchdisease
import com.example.searchdisease.ui.DiseasePredictionResponse
import com.example.searchdisease.ui.SelectedSymptomsRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface APIservice {
    @POST("/predict")
    suspend fun sendSelectedSymptoms(@Body request: SelectedSymptomsRequest): DiseasePredictionResponse
}