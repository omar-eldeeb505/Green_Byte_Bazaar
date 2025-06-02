package com.example.greenbytebazaar.fragments.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.greenbytebazaar.R
import com.example.greenbytebazaar.data.Category
import com.example.greenbytebazaar.util.Resource
import com.example.greenbytebazaar.util.showBottomNavigationView
import com.example.greenbytebazaar.viewmodel.CategoryViewModel
import com.example.greenbytebazaar.viewmodel.Factory.BaseCategoryViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ComputerComponentsFragment: BaseCategoryFragment()  {
    @Inject
    lateinit var firestore: FirebaseFirestore

    val viewModel by viewModels<CategoryViewModel>{
        BaseCategoryViewModelFactory(firestore, Category.ComputerComponents)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.bestProducts.collectLatest {
                    when (it){
                        is Resource.Error<*> -> {
                            Snackbar.make(requireView(),it.message.toString(), Snackbar.LENGTH_LONG).show()
                            hideBestProductsLoading()

                        }
                        is Resource.Loading<*> -> {
                            showBestProductsLoading()

                        }
                        is Resource.Success<*> -> {
                            bestProductsAdapter.differ.submitList(it.data)
                            hideBestProductsLoading()

                        }
                        else -> Unit

                    }
                }
            }
        }
    }

}