package binar.academy.challenge_chapter5.ui.profile

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import binar.academy.challenge_chapter5.R
import binar.academy.challenge_chapter5.databinding.FragmentEditProfileBinding
import binar.academy.challenge_chapter5.ui.authentication.viewmodel.AuthenticationViewModel
import binar.academy.challenge_chapter5.utils.Constant
import binar.academy.challenge_chapter5.utils.Constant.Companion.USERNAME
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class EditProfileFragment : Fragment() {
    private var _binding : FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences
    private val userViewModel : AuthenticationViewModel by viewModels()
    private val args : EditProfileFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences(Constant.USER, Context.MODE_PRIVATE)

        binding.ivCalendarEdit.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.pilih_tanggal))
                .setSelection(MaterialDatePicker.thisMonthInUtcMilliseconds())
                .build()

            datePicker.show(parentFragmentManager, "datePicker")
            datePicker.addOnPositiveButtonClickListener {
                val dateFormat = SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault())
                binding.etBornDate.setText(dateFormat.format(Date(it).time))
            }
        }

        editProfile()

    }

    private fun editProfile() {
        val user = args.userProfile

        binding.apply {
            dataUserUpdate = user

            btnBackToProfile.setOnClickListener {
                findNavController().navigateUp()
            }

            btnEdit.setOnClickListener {
                val id = sharedPreferences.getString(Constant.ID_USER, null)
                val username = etUsername.text.toString()
                val fullname = etFullName.text.toString()
                val bornDate = etBornDate.text.toString()
                val address = etAddress.text.toString()

                if (etUsername.text!!.isEmpty() || etFullName.text!!.isEmpty() || etBornDate.text!!.isEmpty() || etAddress.text!!.isEmpty()){
                    Toast.makeText(context, getString(R.string.tidak_boleh_kosong), Toast.LENGTH_SHORT).show()
                }else{
                    Log.d("UserId", id.toString())
                    updateDataUser(id.toString(), username, fullname, bornDate, address)
                    editSharePrefUsername(username)
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun updateDataUser(id : String, username : String, fullname : String, bornDate : String, address : String){
        userViewModel.editUserProfile(id, username, fullname, bornDate, address)
        userViewModel.editUserObserver().observe(viewLifecycleOwner){
            if (it != null){
                Log.d("editUser", it.toString())
            }else{
                Log.d("editFailed", it.toString())
            }
        }
    }

    private fun editSharePrefUsername(username : String){
        val setNewUsername = sharedPreferences.edit()
        setNewUsername.putString(USERNAME, username)
        setNewUsername.apply()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}