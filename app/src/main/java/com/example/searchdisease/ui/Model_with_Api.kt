package com.example.searchdisease.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.searchdisease.APIservice
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.InstanceCreator
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type


//Here I have implemented the Api call and the UI for the symptom selection screen

class SymptomsData {
    val symptomsList = listOf(
        "itching", "skin_rash", "nodal_skin_eruptions", "continuous_sneezing", "shivering", "chills", "joint_pain", "stomach_pain", "acidity", "ulcers_on_tongue", "muscle_wasting", "vomiting", "burning_micturition", "spotting_ urination", "fatigue", "weight_gain", "anxiety", "cold_hands_and_feets", "mood_swings", "weight_loss", "restlessness", "lethargy", "patches_in_throat", "irregular_sugar_level", "cough", "high_fever", "sunken_eyes", "breathlessness", "sweating", "dehydration", "indigestion", "headache", "yellowish_skin", "dark_urine", "nausea", "loss_of_appetite", "pain_behind_the_eyes", "back_pain", "constipation", "abdominal_pain", "diarrhoea", "mild_fever", "yellow_urine", "yellowing_of_eyes", "acute_liver_failure", "fluid_overload", "swelling_of_stomach", "swelled_lymph_nodes", "malaise", "blurred_and_distorted_vision", "phlegm", "throat_irritation", "redness_of_eyes", "sinus_pressure", "runny_nose", "congestion", "chest_pain", "weakness_in_limbs", "fast_heart_rate", "pain_during_bowel_movements", "pain_in_anal_region", "bloody_stool", "irritation_in_anus", "neck_pain", "dizziness", "cramps", "bruising", "obesity", "swollen_legs", "swollen_blood_vessels", "puffy_face_and_eyes", "enlarged_thyroid", "brittle_nails", "swollen_extremeties", "excessive_hunger", "extra_marital_contacts", "drying_and_tingling_lips", "slurred_speech", "knee_pain", "hip_joint_pain", "muscle_weakness", "stiff_neck", "swelling_joints", "movement_stiffness", "spinning_movements", "loss_of_balance", "unsteadiness", "weakness_of_one_body_side", "loss_of_smell", "bladder_discomfort", "foul_smell_of urine", "continuous_feel_of_urine", "passage_of_gases", "internal_itching", "toxic_look_(typhos)", "depression", "irritability", "muscle_pain", "altered_sensorium", "red_spots_over_body", "belly_pain", "abnormal_menstruation", "dischromic _patches", "watering_from_eyes", "increased_appetite", "polyuria", "family_history", "mucoid_sputum", "rusty_sputum", "lack_of_concentration", "visual_disturbances", "receiving_blood_transfusion", "receiving_unsterile_injections", "coma", "stomach_bleeding", "distention_of_abdomen", "history_of_alcohol_consumption", "fluid_overload.1", "blood_in_sputum", "prominent_veins_on_calf", "palpitations", "painful_walking", "pus_filled_pimples", "blackheads", "scurring", "skin_peeling", "silver_like_dusting", "small_dents_in_nails", "inflammatory_nails", "blister", "red_sore_around_nose", "yellow_crust_ooze"
    )
}

//This is the data class for the symptoms
data class SelectedSymptomsRequest(
    @SerializedName("symptoms")
    val symptoms: List<String>
)
//This is the data class for the response from Modal
data class DiseasePredictionResponse(val disease: String)


@Composable
fun MainScreen() {
    val symptomsData = SymptomsData()
    val selectedSymptoms = remember { mutableStateListOf<String>() }
    val coroutineScope = rememberCoroutineScope()
    val prediction = remember { mutableStateOf("") }
    val isButtonClicked = remember { mutableStateOf(false) }
    val predictedDisease = remember { mutableStateOf<String?>(null) }


    Box{
        SymptomSelectionScreen(symptomsData, selectedSymptoms) { symptom ->
            selectedSymptoms.add(symptom)
        }
        for (symptom in selectedSymptoms) {
            println(symptom)
        }
        Button(
            onClick = {//Here I have implemented the Api call and the UI for the symptom selection screen
                //Here the crash occurs
                println("Button Clicked")
                coroutineScope.launch {
                    isButtonClicked.value = true

                    val result = postDataUsingRetrofit(selectedSymptoms, prediction)
                    if(result != null){
                        prediction.value = result
                    }
                    else{
                        prediction.value = "Error"
                    }
                    Log.d("Predict", "Result: ${prediction.value}")

                }

            },
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Text("Predict")
            if (isButtonClicked.value) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(16.dp)
                        .padding(start = 4.dp),
                    color = Color.White
                )
                predictedDisease.value?.let { Text(text = it, modifier = Modifier.fillMaxWidth()) }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SymptomSelectionScreen(
    symptomsData: SymptomsData,
    selectedSymptoms: SnapshotStateList<String>,
    onSymptomSelected: (String) -> Unit
) {
    val searchText = remember { mutableStateOf("") }
    val filteredSymptoms = remember(searchText.value) {
        symptomsData.symptomsList.filter { it.contains(searchText.value, ignoreCase = true) }
    }

    Column(Modifier.padding(16.dp)) {
        TextField(
            value = searchText.value,
            onValueChange = { searchText.value = it },
            label = { Text("Search Symptoms") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredSymptoms) { symptom ->
                SymptomItem(
                    symptom = symptom,
                    onSelected = { selectedSymptoms.add(it) }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column {
            Text("Selected Symptoms:")
            for (symptom in selectedSymptoms) {
                Text(symptom)
            }
        }
    }
}

@Composable
fun SymptomItem(
    symptom: String,
    onSelected: (String) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = false,
            onCheckedChange = null // TODO: Handle checkbox state
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = symptom)
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = { onSelected(symptom) }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add"
            )
        }
    }
}



class CallTypeInstanceCreator<T> : InstanceCreator<Call<T>> {
    override fun createInstance(type: Type?): Call<T> {
        return object : Call<T> {
            override fun enqueue(callback: Callback<T>) {}
            override fun isExecuted(): Boolean = false
            override fun clone(): Call<T> = this
            override fun isCanceled(): Boolean = false
            override fun cancel() {}
            override fun execute(): Response<T> = throw UnsupportedOperationException("Call execute() not supported")
            override fun request(): Request = throw UnsupportedOperationException("Call request() not supported")
            override fun timeout(): Timeout {
                TODO("Not yet implemented")
            }
        }
    }
}

private fun createApiService(): APIservice {

    val BASE_URL = "https://disease.swoyam.engineer"
    val loggingInterceptor = HttpLoggingInterceptor { message ->
        Log.d("ApiService", message)
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        // Add any other interceptors or configurations as needed
        .build()

    val gson = GsonBuilder()
        .registerTypeAdapter(Call::class.java, CallTypeInstanceCreator<Any>())
        .create()

    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(APIservice::class.java)
}


private suspend fun postDataUsingRetrofit(
    selectedSymptoms: SnapshotStateList<String>,
    result: MutableState<String>
):String {
    // below the line is to create an instance for our retrofit api class.
    val retrofitAPI = createApiService()
    // passing data from our text fields to our model class.
    val dataModel = SelectedSymptomsRequest(selectedSymptoms)
    // calling a method to create an update and passing our model class.
    val call: Call<DiseasePredictionResponse>? = retrofitAPI.sendSelectedSymptoms(dataModel)
    // on below line we are executing our method.
    try {
        val response = call?.enqueue(object : Callback<DiseasePredictionResponse> {
            override fun onResponse(
                call: Call<DiseasePredictionResponse>,
                response: Response<DiseasePredictionResponse>
            ) {
                if (response.isSuccessful) {
                    result.value  = response.body()?.disease ?: "No disease found"
                    Log.d("trial","Response: ${response.body()?.disease}")
                    Log.d("Response", "Response: ${response.body()?.disease}")
                } else{
                    result.value = "Error found: ${response.errorBody()}"
                    println("Response: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<DiseasePredictionResponse>, t: Throwable) {
                println("Response: ${t.message}")
                result.value = "Error found: ${t.message}"
            }
        })
    } catch (e: Exception) {
        result.value = "Error found: in postdatausingretrofit"
        Log.e("YourTag", "Exception occurred: ${e.message}", e)
    }
    return result.value
}

@Preview
@Composable
fun PreviewMainScreen() {
    MainScreen()
}