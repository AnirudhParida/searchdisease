package com.example.searchdisease.ui

import android.hardware.biometrics.BiometricManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update



@OptIn(FlowPreview::class)
class MainViewModel :ViewModel() {
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _symptoms = MutableStateFlow(allsymptoms)


    val symptoms = searchText.debounce(1000).onEach { _isSearching.update { true } }
        .combine(_symptoms) { text, symptoms ->
            if (text.isBlank()) {
                symptoms
            } else {
                delay(2000L)
                symptoms.filter {
                    it.doesmatchquery(text)
                }
            }
        }
        .onEach { _isSearching.update { false } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _symptoms.value
        )

    fun onSearchTextChange(text:String) {
        _searchText.value = text
    }
}

data class Symptoms(val symptoms: String, var isSelected:Boolean = false) {
    fun doesmatchquery(query:String): Boolean {
        val matchingcombinations = listOf(
            symptoms
        )
        return matchingcombinations.any{
            it.contains(query,ignoreCase = true)
        }
    }
}

private val allsymptoms = listOf<Symptoms>(
    Symptoms("itching"),
    Symptoms("skin_rash"),
    Symptoms("nodal_skin_eruptions"),
    Symptoms("continuous_sneezing"),
    Symptoms("shivering"),
    Symptoms("chills"),
    Symptoms("joint_pain"),
    Symptoms("stomach_pain"),
    Symptoms("acidity"),
    Symptoms("ulcers_on_tongue"),
    Symptoms("muscle_wasting"),
    Symptoms("vomiting"),
    Symptoms("burning_micturition"),
    Symptoms("spotting_ urination"),
    Symptoms("fatigue"),
    Symptoms("weight_gain"),
    Symptoms("anxiety"),
    Symptoms("cold_hands_and_feets"),
    Symptoms("mood_swings"),
    Symptoms("weight_loss"),
    Symptoms("restlessness"),
    Symptoms("lethargy"),
    Symptoms("patches_in_throat"),
    Symptoms("irregular_sugar_level"),
    Symptoms("cough"),
    Symptoms("high_fever"),
    Symptoms("sunken_eyes"),
    Symptoms("breathlessness"),
    Symptoms("sweating"),
    Symptoms("dehydration"),
    Symptoms("indigestion"),
    Symptoms("headache"),
    Symptoms("yellowish_skin"),
    Symptoms("dark_urine"),
    Symptoms("nausea"),
    Symptoms("loss_of_appetite"),
    Symptoms("pain_behind_the_eyes"),
    Symptoms("back_pain"),
    Symptoms("constipation"),
    Symptoms("abdominal_pain"),
    Symptoms("diarrhoea"),
    Symptoms("mild_fever"),
    Symptoms("yellow_urine"),
    Symptoms("yellowing_of_eyes"),
    Symptoms("acute_liver_failure"),
    Symptoms("fluid_overload"),
    Symptoms("swelling_of_stomach"),
    Symptoms("swelled_lymph_nodes"),
    Symptoms("malaise"),
    Symptoms("blurred_and_distorted_vision"),
    Symptoms("phlegm"),
    Symptoms("throat_irritation"),
    Symptoms("redness_of_eyes"),
    Symptoms("sinus_pressure"),
    Symptoms("runny_nose"),
    Symptoms("congestion"),
    Symptoms("chest_pain"),
    Symptoms("weakness_in_limbs"),
    Symptoms("fast_heart_rate"),
    Symptoms("pain_during_bowel_movements"),
    Symptoms("pain_in_anal_region"),
    Symptoms("bloody_stool"),
    Symptoms("irritation_in_anus"),
    Symptoms("neck_pain"),
    Symptoms("dizziness"),
    Symptoms("cramps"),
    Symptoms("bruising"),
    Symptoms("obesity"),
    Symptoms("swollen_legs"),
    Symptoms("swollen_blood_vessels"),
    Symptoms("puffy_face_and_eyes"),
    Symptoms("enlarged_thyroid"),
    Symptoms("brittle_nails"),
    Symptoms("swollen_extremeties"),
    Symptoms("excessive_hunger"),
    Symptoms("extra_marital_contacts"),
    Symptoms("drying_and_tingling_lips"),
    Symptoms("slurred_speech"),
    Symptoms("knee_pain"),
    Symptoms("hip_joint_pain"),
    Symptoms("muscle_weakness"),
    Symptoms("stiff_neck"),
    Symptoms("swelling_joints"),
    Symptoms("movement_stiffness"),
    Symptoms("spinning_movements"),
    Symptoms("loss_of_balance"),
    Symptoms("unsteadiness"),
    Symptoms("weakness_of_one_body_side"),
    Symptoms("loss_of_smell"),
    Symptoms("bladder_discomfort"),
    Symptoms("foul_smell_of urine"),
    Symptoms("continuous_feel_of_urine"),
    Symptoms("passage_of_gases"),
    Symptoms("internal_itching"),
    Symptoms("toxic_look_(typhos)"),
    Symptoms("depression"),
    Symptoms("irritability"),
    Symptoms("muscle_pain"),
    Symptoms("altered_sensorium"),
    Symptoms("red_spots_over_body"),
    Symptoms("belly_pain"),
    Symptoms("abnormal_menstruation"),
    Symptoms("dischromic _patches"),
    Symptoms("watering_from_eyes"),
    Symptoms("increased_appetite"),
    Symptoms("polyuria"),
    Symptoms("family_history"),
    Symptoms("mucoid_sputum"),
    Symptoms("rusty_sputum"),
    Symptoms("lack_of_concentration"),
    Symptoms("visual_disturbances"),
    Symptoms("receiving_blood_transfusion"),
    Symptoms("receiving_unsterile_injections"),
    Symptoms("coma"),
    Symptoms("stomach_bleeding"),
    Symptoms("distention_of_abdomen"),
    Symptoms("history_of_alcohol_consumption"),
    Symptoms("fluid_overload.1"),
    Symptoms("blood_in_sputum"),
    Symptoms("prominent_veins_on_calf"),
    Symptoms("palpitations"),
    Symptoms("painful_walking"),
    Symptoms("pus_filled_pimples"),
    Symptoms("blackheads"),
    Symptoms("scurring"),
    Symptoms("skin_peeling"),
    Symptoms("silver_like_dusting"),
    Symptoms("small_dents_in_nails"),
    Symptoms("inflammatory_nails"),
    Symptoms("blister"),
    Symptoms("red_sore_around_nose"),
    Symptoms("yellow_crust_ooze")
)


