package com.example.greenbytebazaar.util

import android.util.Patterns

fun validateEmail(email:String):RegisterValidation{
    if(email.isEmpty())
        return RegisterValidation.Failed("Email can't be empty")
    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        return RegisterValidation.Failed("Wrong email format")

    return RegisterValidation.Success
}
fun validatePassword(password:String , confirmPassword:String):RegisterValidation{
    if(password.isEmpty())
        return RegisterValidation.Failed("Password can't be empty")
    if (password.length<6)
        return RegisterValidation.Failed("Password should contain at least 6 char")
    if (password != confirmPassword)
        return RegisterValidation.Failed("Password don't match")

    return RegisterValidation.Success
}
fun validateconfirmPassword(password:String , confirmPassword:String):RegisterValidation{
    if(confirmPassword.isEmpty())
        return RegisterValidation.Failed("Confirm Password can't be empty")
    if (confirmPassword.length<6)
        return RegisterValidation.Failed("Confirm Password should contain at least 6 char")
    if (password != confirmPassword)
        return RegisterValidation.Failed("Confirm Password don't match")

    return RegisterValidation.Success
}

