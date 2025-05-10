package com.example.greenbytebazaar.fragments.Shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.greenbytebazaar.R
import com.example.greenbytebazaar.adapters.HomeViewpagerAdapter
import com.example.greenbytebazaar.databinding.FragmentHomeBinding
import com.example.greenbytebazaar.fragments.categories.AccessoriesFragment
import com.example.greenbytebazaar.fragments.categories.ComputerComponentsFragment
import com.example.greenbytebazaar.fragments.categories.ComputersFragment
import com.example.greenbytebazaar.fragments.categories.LaptopsFragment
import com.example.greenbytebazaar.fragments.categories.MainCategoryFragment
import com.example.greenbytebazaar.fragments.categories.NetworksFragment
import com.example.greenbytebazaar.fragments.categories.OtherFragment
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment:Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categoriesFragment = arrayListOf<Fragment>(
            MainCategoryFragment(),
            ComputersFragment(),
            LaptopsFragment(),
            NetworksFragment(),
            AccessoriesFragment(),
            ComputerComponentsFragment(),
            OtherFragment()
        )
        val viewPager2Adapter =
            HomeViewpagerAdapter(categoriesFragment, childFragmentManager, lifecycle)
        binding.viewPagerHome.adapter=viewPager2Adapter
        TabLayoutMediator(binding.tabLayout,binding.viewPagerHome){tab,position->
            when(position){
                0->tab.text="Main"
                1->tab.text="Computers"
                2->tab.text="Laptops"
                3->tab.text="Networks"
                4->tab.text="Accessories"
                5->tab.text="Computer Components"
                6->tab.text="Other"
            }

        }.attach()

    }

}