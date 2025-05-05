package com.example.greenbytebazaar.viewmodel

import androidx.lifecycle.ViewModel
import com.example.greenbytebazaar.data.User
import com.example.greenbytebazaar.util.Constants.USER_COLLECTION
import com.example.greenbytebazaar.util.RegisterFieldState
import com.example.greenbytebazaar.util.RegisterValidation
import com.example.greenbytebazaar.util.Resource
import com.example.greenbytebazaar.util.validateEmail
import com.example.greenbytebazaar.util.validatePassword
import com.example.greenbytebazaar.util.validateconfirmPassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth:FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {
    private val _register = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val register: Flow<Resource<User>> = _register

    private val _validation = Channel<RegisterFieldState>()
    val validation = _validation.receiveAsFlow()

    fun createAccountWithEmailAndPassword(user: User , Password:String , confirmPassword: String) {
        if (checkValidation(user, Password, confirmPassword)){
            runBlocking {
                _register.emit(Resource.Loading())

            }
        firebaseAuth.createUserWithEmailAndPassword(user.email, Password)

            .addOnSuccessListener { it ->
                it.user?.let {
                saveUserInfo(it.uid,user)
                }

            }.addOnFailureListener {
                _register.value = Resource.Error(it.message.toString())

            }
        } else{
            val registerFieldState = RegisterFieldState(
                validateEmail(user.email),
                validatePassword(Password, confirmPassword),
                validateconfirmPassword(Password, confirmPassword)
            )
            runBlocking {
                _validation.send(registerFieldState)
            }

        }

    }

    private fun saveUserInfo(userUid: String , user: User) {
        db.collection(USER_COLLECTION)
            .document(userUid)
            .set(user)
            .addOnSuccessListener {
                _register.value = Resource.Success(user)
            }
            .addOnFailureListener {
                _register.value = Resource.Error(it.message.toString())

            }

    }

    private fun checkValidation(user: User, Password: String , confirmPassword: String) :Boolean{
        val emailValidation = validateEmail(user.email)
        val passwordValidation = validatePassword(Password, confirmPassword)
        val confirmPasswordValidation = validateconfirmPassword(Password, confirmPassword)
        val shouldRegister = emailValidation is RegisterValidation.Success &&
                passwordValidation is RegisterValidation.Success && confirmPasswordValidation is RegisterValidation.Success

        return shouldRegister
    }
}