package com.example.greenbytebazaar.fragments.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.greenbytebazaar.R
import com.example.greenbytebazaar.adapters.BestProductsAdapter
import com.example.greenbytebazaar.adapters.SpecialProductsAdapter
import com.example.greenbytebazaar.databinding.FragmentMainCategoryBinding
import com.example.greenbytebazaar.util.Resource
import com.example.greenbytebazaar.util.showBottomNavigationView
import com.example.greenbytebazaar.viewmodel.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private val TAG = "MainCategoryFragment"
@AndroidEntryPoint
class MainCategoryFragment: Fragment(R.layout.fragment_main_category) {

    private lateinit var binding:FragmentMainCategoryBinding
    private lateinit var specialProductsAdapter: SpecialProductsAdapter
    private lateinit var bestProductsAdapter: BestProductsAdapter
    private val viewModel by viewModels<MainCategoryViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentMainCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpecialProductsRv()
        setupBestProductsRv()

        specialProductsAdapter.onClick = {
            val b = Bundle().apply {
                putParcelable("product",it)
            }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)}
        bestProductsAdapter.onClick = {
            val b = Bundle().apply {
                putParcelable("product",it)
            }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)}

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.specialProducts.collectLatest {

                    when (it){
                        is Resource.Error<*> -> {
                            hideLoading()
                            Log.e(TAG,it.message.toString())
                            Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()

                        }
                        is Resource.Loading<*> -> {
                            showLoading()
                        }
                        is Resource.Success<*> -> {
                            specialProductsAdapter.differ.submitList(it.data)
                            hideLoading()
                        }
                       else -> Unit
                    }

                }
            }

        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.bestProducts.collectLatest {

                    when (it){
                        is Resource.Error<*> -> {
                            hideLoading()
                            Log.e(TAG,it.message.toString())
                            Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()

                        }
                        is Resource.Loading<*> -> {
                            binding.bestProductsProgressBar.visibility = View.VISIBLE
                        }
                        is Resource.Success<*> -> {
                            bestProductsAdapter.differ.submitList(it.data)
                            binding.bestProductsProgressBar.visibility = View.GONE
                        }
                        else -> Unit
                    }

                }
            }
        }
        binding.nestedScrollMainCategory.setOnScrollChangeListener (NestedScrollView.OnScrollChangeListener{
            v,_,scrollY,_,_ ->
            if (v.getChildAt(0).bottom<=scrollY+ v.height){
                viewModel.fetchBestProducts()
            }
        })
    }
    private fun setupBestProductsRv() {
        bestProductsAdapter = BestProductsAdapter()
        binding.rvBestProducts.apply {
            layoutManager = GridLayoutManager(requireContext(),2,
                GridLayoutManager.VERTICAL,false)
            adapter = bestProductsAdapter
        }
    }
    private fun hideLoading() {
        binding.mainCategoryProgressBar.visibility = View.GONE
        binding.tvBestProducts.visibility = View.VISIBLE
        binding.tvSpecialProducts.visibility = View.VISIBLE
    }
    private fun showLoading() {
        binding.tvBestProducts.visibility = View.GONE
        binding.tvSpecialProducts.visibility = View.GONE

    }
    private fun setupSpecialProductsRv() {
        specialProductsAdapter = SpecialProductsAdapter()
        binding.rvSpecialProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL,false)
            adapter = specialProductsAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigationView()

    }
}