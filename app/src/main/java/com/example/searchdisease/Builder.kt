package com.example.searchdisease

import com.example.searchdisease.ui.SelectedSymptomsRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Create a Retrofit instance
/*val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl("disease.swoyam.engineer/") // Replace with your API base URL
    .addConverterFactory(GsonConverterFactory.create())
    .build()

// Create an instance of the ApiService using the Retrofit instance
val apiService: APIservice = retrofit.create(APIservice::class.java)*/

suspend fun sendSelectedSymptoms(apiService: APIservice, selectedSymptoms: List<String>) {
    try {
        val request = SelectedSymptomsRequest(selectedSymptoms)
        apiService.sendSelectedSymptoms(request)
    }catch (e: Exception){
        e.stackTrace
    }

}