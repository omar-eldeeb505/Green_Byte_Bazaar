package com.example.greenbytebazaar.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.greenbytebazaar.R
import com.example.greenbytebazaar.adapters.BestProductsAdapter
import com.example.greenbytebazaar.databinding.FragmentBaseCategoryBinding
import com.example.greenbytebazaar.databinding.FragmentMainCategoryBinding

open class BaseCategoryFragment: Fragment(R.layout.fragment_base_category) {
    private lateinit var binding: FragmentBaseCategoryBinding
    protected val bestProductsAdapter by lazy { BestProductsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBestProductsRv()

        binding.nestedScrollBaseCategory.setOnScrollChangeListener (NestedScrollView.OnScrollChangeListener{
                v,_,scrollY,_,_ ->
            if (v.getChildAt(0).bottom<=scrollY+ v.height){
                onBestProductsPagingRequest()
            }
        })

    }
    fun showBestProductsLoading(){
        binding.bestProductsProgressBar.visibility = View.VISIBLE

    }
    fun hideBestProductsLoading(){
        binding.bestProductsProgressBar.visibility = View.GONE
    }

    open fun onBestProductsPagingRequest(){

    }

    private fun setupBestProductsRv() {

        binding.rvBase.apply {
            layoutManager = GridLayoutManager(requireContext(),2,
                GridLayoutManager.VERTICAL,false)
            adapter = bestProductsAdapter
        }    }
}
