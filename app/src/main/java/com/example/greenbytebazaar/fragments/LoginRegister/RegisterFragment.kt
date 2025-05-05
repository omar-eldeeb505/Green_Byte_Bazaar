package com.example.greenbytebazaar.fragments.LoginRegister

import android.nfc.Tag
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.greenbytebazaar.R
import com.example.greenbytebazaar.data.User
import com.example.greenbytebazaar.databinding.FragmentRegisterBinding
import com.example.greenbytebazaar.util.RegisterValidation
import com.example.greenbytebazaar.util.Resource
import com.example.greenbytebazaar.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.zip.Inflater
private const val TAG="RegisterFragment"
@AndroidEntryPoint
class RegisterFragment: Fragment() {

    private lateinit var binding:FragmentRegisterBinding
    private  val viewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.login.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment2_to_loginFragment)
        }
        binding.apply {
            registerBtn.setOnClickListener {
                val user = User(
                    fullName.text.toString().trim(),
                    userName.text.toString().trim(),
                    email.text.toString().trim()

                )
                val password = password.text.toString()
                val confirmPassword = confirmPassword.text.toString()
                viewModel.createAccountWithEmailAndPassword(user, password, confirmPassword)
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
            viewModel.register.collect {
                when (it) {
                    is Resource.Loading<*> -> {
                        binding.progressCircular2.visibility = View.VISIBLE
                        binding.registerBtn.visibility = View.GONE
                        binding.haveAccount.visibility=View.GONE
                        binding.login.visibility=View.GONE


                    }
                    is Resource.Error<*> -> {
                        binding.progressCircular2.visibility = View.GONE
                        binding.registerBtn.visibility = View.VISIBLE
                        Log.e(TAG,it.message.toString())
                    }
                    is Resource.Success<*> -> {
                        binding.progressCircular2.visibility = View.GONE
                        binding.registerBtn.visibility = View.VISIBLE
                        Log.d("test",it.data.toString())
                        findNavController().navigate(R.id.action_registerFragment2_to_loginFragment)
                    }
                    else -> Unit
                }
            }
        }
    }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.validation.collect { validation ->
                    if (validation.email is RegisterValidation.Failed) {
                        withContext(Dispatchers.Main) {
                            binding.email.apply{
                                requestFocus()
                                error = validation.email.message
                            }
                        }
                    }
                    if (validation.password is RegisterValidation.Failed) {
                        withContext(Dispatchers.Main) {
                            binding.password.apply{
                                requestFocus()
                                error = validation.password.message
                            }
                        }
                    }
                    if (validation.confirmpassword is RegisterValidation.Failed) {
                        withContext(Dispatchers.Main) {
                            binding.confirmPassword.apply{
                                requestFocus()
                                error = validation.confirmpassword.message
                            }
                        }
                    }
                }
            }
        }
    }
}