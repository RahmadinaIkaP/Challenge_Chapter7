package binar.academy.challenge_chapter5.view.profile

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import binar.academy.challenge_chapter5.R
import binar.academy.challenge_chapter5.data.datastore.UserPref
import binar.academy.challenge_chapter5.databinding.FragmentProfileBinding
import binar.academy.challenge_chapter5.viewmodel.AuthenticationViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var userPref: UserPref
    private val userViewModel : AuthenticationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPref = UserPref(requireContext())

        // get recently user info from api
        getUserData()

        binding.apply {
            btnBackToHome.setOnClickListener {
                findNavController().navigateUp()
            }

            btnLogout.setOnClickListener {
                logout()
                findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
            }

            layoutUbahBahasa.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_settingLanguageFragment)
            }
        }
    }

    private fun getUserData() {
        userPref.idUser.asLiveData().observe(viewLifecycleOwner){
            userViewModel.getCurrentUser(it.toString())
            userViewModel.currentUserObserver().observe(viewLifecycleOwner){ data ->
                binding.apply {
                    val user = data
                    dataUser = user

                    btnToEdit.setOnClickListener {
                        val action = ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment(user)
                        findNavController().navigate(action)
                    }
                }

            }
        }

    }

    private fun logout() {
        GlobalScope.launch {
            userPref.logoutUser()
            Snackbar.make(binding.root, getString(R.string.logout_berhasil), Snackbar.LENGTH_SHORT)
                .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.light_gray))
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_blue))
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}