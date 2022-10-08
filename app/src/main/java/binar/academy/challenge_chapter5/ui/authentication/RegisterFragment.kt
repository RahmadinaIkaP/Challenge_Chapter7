package binar.academy.challenge_chapter5.ui.authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import binar.academy.challenge_chapter5.R
import binar.academy.challenge_chapter5.data.model.User
import binar.academy.challenge_chapter5.databinding.FragmentRegisterBinding
import binar.academy.challenge_chapter5.ui.authentication.viewmodel.AuthenticationViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

class RegisterFragment : Fragment() {
    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val authenticationViewModel : AuthenticationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivCalendar.setOnClickListener {

            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.pilih_tanggal))
                .setSelection(MaterialDatePicker.thisMonthInUtcMilliseconds())
                .build()

            datePicker.show(parentFragmentManager, "datePicker")
            datePicker.addOnPositiveButtonClickListener {
                val dateFormat = SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault())
                binding.etBornDateRegister.setText(dateFormat.format(Date(it).time))
            }
        }

        binding.btnRegister.setOnClickListener {
            register()
        }
    }

    private fun register() {
        binding.apply {
            val username = etUsernameRegis.text.toString()
            val email = etEmailRegister.text.toString()
            val fullname = etFullnameRegister.text.toString()
            val bornDate = etBornDateRegister.text.toString()
            val address = etAddressRegister.text.toString()
            val password = etPassRegister.text.toString()
            val konfirPass = etKonfirPass.text.toString()

            if (etUsernameRegis.text!!.isEmpty() || etEmailRegister.text!!.isEmpty() || etFullnameRegister.text!!.isEmpty() || etBornDateRegister.text!!.isEmpty()
                || etAddressRegister.text!!.isEmpty() || etPassRegister.text!!.isEmpty() || etKonfirPass.text!!.isEmpty()){

                Toast.makeText(context, getString(R.string.tidak_boleh_kosong), Toast.LENGTH_SHORT).show()
            }else{
                if (konfirPass == password){
                    authenticationViewModel.register(username, email, password, fullname, bornDate, address)
                    authenticationViewModel.registerObserver().observe(viewLifecycleOwner){
                        if (it != null){
                            Snackbar.make(binding.root, getString(R.string.register_berhasil), Snackbar.LENGTH_SHORT)
                                .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.light_gray))
                                .setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_blue))
                                .show()
                            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                        }else{
                            Snackbar.make(binding.root, getString(R.string.register_gagal), Snackbar.LENGTH_SHORT)
                                .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.light_gray))
                                .setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_blue))
                                .show()
                        }
                    }
                }else{
                    Snackbar.make(binding.root, getString(R.string.password_tidak_sama), Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.light_gray))
                        .setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_blue))
                        .show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}