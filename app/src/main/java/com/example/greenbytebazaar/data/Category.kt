package com.example.greenbytebazaar.data

sealed class Category(val category: String) {

    object Laptops : Category("Laptops")
    object PCs : Category("PCs")
    object Networks : Category("Networks")
    object ComputerComponents : Category("ComputerComponents")
    object Accessories : Category("Accessories")
}