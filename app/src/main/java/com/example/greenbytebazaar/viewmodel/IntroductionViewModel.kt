package com.example.greenbytebazaar.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenbytebazaar.R
import com.example.greenbytebazaar.util.Constants.INTRODUCTION_KEY
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.core.content.edit
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class IntroductionViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val firebaseAuth: FirebaseAuth

) : ViewModel() {
    private val _navigate = MutableStateFlow(0)
    val navigate: StateFlow<Int> = _navigate

    companion object{
        const val SHOPPING_ACTIVITY=23
        val ACCOUNT_OPTIONS_FRAGMENT= R.id.action_introductionFragment_to_accountOptionsFragment
    }

    init {

        val isButtonClicked = sharedPreferences.getBoolean(INTRODUCTION_KEY, false)
        val user = firebaseAuth.currentUser

        if (user != null) {
            viewModelScope.launch {
                _navigate.emit(SHOPPING_ACTIVITY)
            }

        } else if (isButtonClicked) {
            viewModelScope.launch {
                _navigate.emit(ACCOUNT_OPTIONS_FRAGMENT)
            }
        }else {
            Unit
        }
    }
    fun startButtonClick() {
        sharedPreferences.edit().putBoolean(INTRODUCTION_KEY, true).apply()

    }
}