package binar.academy.challenge_chapter5.view.splashscreen

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import binar.academy.challenge_chapter5.R
import binar.academy.challenge_chapter5.data.datastore.UserPref
import binar.academy.challenge_chapter5.databinding.FragmentSplashScreenBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreenFragment : Fragment() {
    private var _binding : FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var userPref : UserPref

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPref = UserPref(requireContext())
        splashScreen()
    }

    private fun splashScreen() {
        val time : Long = 3000

        userPref.statusLogin.asLiveData().observe(viewLifecycleOwner){
            Handler(Looper.getMainLooper()).postDelayed({
                if (!it){
                    findNavController().navigate(R.id.action_splashScreenFragment_to_loginFragment)
                }
                else
                    findNavController().navigate(R.id.action_splashScreenFragment_to_homeFragment)
            }, time)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}