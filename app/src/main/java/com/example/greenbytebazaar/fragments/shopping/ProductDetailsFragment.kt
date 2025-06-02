package com.example.greenbytebazaar.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.greenbytebazaar.R
import com.example.greenbytebazaar.activities.ShoppingActivity
import com.example.greenbytebazaar.adapters.ViewPager2Images
import com.example.greenbytebazaar.databinding.FragmentProductDetailsBinding
import com.example.greenbytebazaar.util.hideBottomNavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProductDetailsFragment: Fragment() {
    private val args by navArgs<ProductDetailsFragmentArgs>()
    private lateinit var binding: FragmentProductDetailsBinding
    private val viewPagerAdapter by lazy { ViewPager2Images() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        hideBottomNavigationView()
        binding = FragmentProductDetailsBinding.inflate(inflater)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val product = args.product
        setupViewpager()
        binding.imageClose.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.apply {
            tvProductName.text = product.name
            tvProductPrice.text = "${product.price} EGP"
            tvProductDescription.text = product.description
        }
        viewPagerAdapter.differ.submitList(product.images)
    }

    private fun setupViewpager() {
        binding.apply {
            viewPagerProductImages.adapter = viewPagerAdapter
        }
    }
}