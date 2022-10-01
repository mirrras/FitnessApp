package com.example.fitnessapp.utils

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitnessapp.adapters.ExerciseModel

class MainViewModel : ViewModel(){
    val mutableListExercise = MutableLiveData<ArrayList<ExerciseModel>>()
    var pref: SharedPreferences? = null
    var cuurentDay = 0

    fun savePref(key: String, value: Int){
        pref?.edit()?.putInt(key, value)?.apply()
    }

    fun getExerciseCount():Int{
        return pref?.getInt(cuurentDay.toString(), 0) ?:0
    }
}