package com.example.greenbytebazaar.data

data class User(
    val fullName:String,
    val userName:String,
    val email:String,
    val imageUrl:String?=null
){
    constructor():this("","","","")
}
