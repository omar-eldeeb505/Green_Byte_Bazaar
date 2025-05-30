package com.example.greenbytebazaar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenbytebazaar.data.Product
import com.example.greenbytebazaar.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel@Inject constructor(
    private val firestore: FirebaseFirestore
): ViewModel() {
    private val _specialProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val specialProducts: StateFlow<Resource<List<Product>>> = _specialProducts

    private val _bestProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestProducts: StateFlow<Resource<List<Product>>> = _bestProducts

    private val pagingInfo = PagingInfo()

    init {
        fetchSpecialProducts()
        fetchBestProducts()
    }

    fun fetchSpecialProducts() {
        viewModelScope.launch {
            _specialProducts.emit(Resource.Loading())
        }
        firestore.collection("Products").whereEqualTo("category", "Accessories").get()
            .addOnSuccessListener { result ->
                val specialProductList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _specialProducts.emit(Resource.Success(specialProductList))
                }

            }.addOnFailureListener {
            viewModelScope.launch {
                _specialProducts.emit(Resource.Error(it.message.toString()))
            }
        }
    }

    fun fetchBestProducts() {
        if (!pagingInfo.isPagingEnd) {
        viewModelScope.launch {
            _bestProducts.emit(Resource.Loading())
        }
        firestore.collection("Products").limit(pagingInfo.bestProductsPage * 10).get()
            .addOnSuccessListener { result ->
                val bestProducts = result.toObjects(Product::class.java)
                pagingInfo.isPagingEnd = bestProducts == pagingInfo.oldBestProducts
                pagingInfo.oldBestProducts = bestProducts
                viewModelScope.launch {
                    _bestProducts.emit(Resource.Success(bestProducts))
                }
                pagingInfo.bestProductsPage++

            }.addOnFailureListener {
                viewModelScope.launch {
                    _bestProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }
}
}

internal data class PagingInfo(

    var bestProductsPage: Long= 1,
    var oldBestProducts: List<Product> = emptyList(),
    var isPagingEnd: Boolean = false

)