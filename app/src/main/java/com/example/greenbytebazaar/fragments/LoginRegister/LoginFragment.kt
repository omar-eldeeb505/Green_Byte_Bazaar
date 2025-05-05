package com.example.greenbytebazaar.fragments.LoginRegister

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.greenbytebazaar.R
import com.example.greenbytebazaar.activities.ShoppingActivity
import com.example.greenbytebazaar.databinding.FragmentLoginBinding
import com.example.greenbytebazaar.util.Resource
import com.example.greenbytebazaar.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment: Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.register.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment2)
        }

        binding.apply {
            btnLogin.setOnClickListener {
                val email = emailEdit.text.toString().trim()
                val password = passwordEdit.text.toString().trim()
                viewModel.login(email, password)
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.login.collect {
                    when (it) {
                        is Resource.Loading<*> -> {
                            binding.progressCircular1.visibility = View.VISIBLE
                            binding.btnLogin.visibility = View.GONE
                            binding.notHaveAcc.visibility=View.GONE
                            binding.register.visibility=View.GONE


                        }
                        is Resource.Error<*> -> {
                            binding.progressCircular1.visibility = View.GONE
                            binding.btnLogin.visibility = View.VISIBLE
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()


                        }
                        is Resource.Success<*> -> {
                            binding.progressCircular1.visibility = View.GONE
                            binding.btnLogin.visibility = View.VISIBLE
                            Intent(requireActivity(), ShoppingActivity::class.java).also {
                                    intent ->
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                            }
                        }
                        else -> Unit
                    }
                }
            }
        }
    }
}