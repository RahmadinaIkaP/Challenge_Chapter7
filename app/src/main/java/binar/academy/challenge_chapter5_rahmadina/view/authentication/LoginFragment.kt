package binar.academy.challenge_chapter5_rahmadina.view.authentication

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import binar.academy.challenge_chapter5_rahmadina.R
import binar.academy.challenge_chapter5_rahmadina.data.datastore.UserPref
import binar.academy.challenge_chapter5_rahmadina.model.UserResponse
import binar.academy.challenge_chapter5_rahmadina.databinding.FragmentLoginBinding
import binar.academy.challenge_chapter5_rahmadina.viewmodel.AuthenticationViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val authenticationViewModel : AuthenticationViewModel by viewModels()
    private lateinit var userPref: UserPref
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mGoogleSignInClient : GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPref = UserPref(requireContext())

        firebaseAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

//        throw RuntimeException("Test Crash") // Force a crash

        binding.apply {

            btnLogin.setOnClickListener {
                val email = etEmailLogin.text.toString()
                val password = etPassLogin.text.toString()
                login(email, password)
            }

            btnToRegister.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }

            btnLoginWithGoogle.setOnClickListener {
                loginWithGoogleAcc()
            }
        }
    }

    private fun loginWithGoogleAcc() {
        val intent = mGoogleSignInClient.signInIntent
        resultLauncher.launch(intent)
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK){
            val task : Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            handleResult(task)
        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            if (account != null){
                updateUI(account)
            }
        }catch(e : ApiException){
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful){
                saveData(account.displayName.toString(), true)
                Toast.makeText(requireContext(), "Login Berhasil!!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }else{
                Toast.makeText(requireContext(), "Login Gagal!!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveData(username: String, status : Boolean) {
        GlobalScope.launch {
            userPref.saveLoginData(username, status)
        }
    }

    private fun login(email : String, password : String) {
        val listUser : MutableList<UserResponse> = mutableListOf()

        if (email.isEmpty() || password.isEmpty()){
            Snackbar.make(requireView(), getString(R.string.tidak_boleh_kosong), Snackbar.LENGTH_SHORT)
                .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.light_gray))
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_blue))
                .show()
        }else{
            authenticationViewModel.login()
            authenticationViewModel.loginObserver().observe(viewLifecycleOwner){
                if (it != null){
                    it.forEach { users ->
                        listUser.addAll(mutableListOf(users))
                    }

                    val currentUser = listUser.filter { cu -> cu.email == email && cu.password == password }
                    if (currentUser.isNotEmpty()){
                        inputUsernameSharePreferences(currentUser[0].id, currentUser[0].username,true)

                        Snackbar.make(requireView(), getString(R.string.login_berhasil), Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.light_gray))
                            .setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_blue))
                            .show()

                        Log.d("User", currentUser.toString())

                        listUser.clear()
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    }else{
                        Snackbar.make(binding.root, getString(R.string.email_password_salah), Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.light_gray))
                            .setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_blue))
                            .show()
                    }
                }else{
                    Snackbar.make(binding.root, getString(R.string.login_gagal), Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.light_gray))
                        .setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_blue))
                        .show()
                }
            }
        }
    }

    private fun inputUsernameSharePreferences( id : String, username : String, isLogin : Boolean) {
        GlobalScope.launch {
            userPref.saveData(id, username, isLogin)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}