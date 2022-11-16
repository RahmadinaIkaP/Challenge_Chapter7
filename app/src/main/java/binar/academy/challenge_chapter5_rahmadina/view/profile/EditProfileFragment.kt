package binar.academy.challenge_chapter5_rahmadina.view.profile

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import binar.academy.challenge_chapter5_rahmadina.R
import binar.academy.challenge_chapter5_rahmadina.data.datastore.UserPref
import binar.academy.challenge_chapter5_rahmadina.databinding.FragmentEditProfileBinding
import binar.academy.challenge_chapter5_rahmadina.viewmodel.AuthenticationViewModel
import binar.academy.challenge_chapter5_rahmadina.viewmodel.BlurViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class EditProfileFragment : Fragment() {
    private var _binding : FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var userPref: UserPref
    private val userViewModel : AuthenticationViewModel by viewModels()
    private val blurViewModel: BlurViewModel by viewModels { BlurViewModel.BlurViewModelFactory(requireActivity().application) }
    private val args : EditProfileFragmentArgs by navArgs()
    private var profilePictUri : Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPref = UserPref(requireContext())

        binding.ivCalendarEdit.setOnClickListener {
            editDateFromDatePicker()
        }

        binding.btnEditPhotoProfile.setOnClickListener {
            checkingPermissions()
        }

        setPictureBlur()

        editProfile()

    }

    private fun editDateFromDatePicker() {
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

    private fun checkingPermissions() {
        if (isGranted(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                97)
        ){
            openGallery()
        }
    }

    private fun isGranted(
        activity: Activity,
        permission: String,
        permissions: Array<String>,
        request: Int,
    ): Boolean {
        val permissionCheck = ActivityCompat.checkSelfPermission(activity, permission)
        return if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                showPermissionDeniedDialog()
            } else {
                ActivityCompat.requestPermissions(activity, permissions, request)
            }
            false
        } else {
            true
        }
    }

    private fun showPermissionDeniedDialog(){
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.permission_denied))
            .setMessage(getString(R.string.permission_denied_msg))
            .setPositiveButton(
                getString(R.string.settings)
            ){ _, _ ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", requireActivity().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton(getString(R.string.cancel)){ dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    private fun openGallery() {
        requireActivity().intent.type = "image/*"
        galleryResult.launch("image/*")
    }

    //    Gallery
    private val galleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            if (result != null){
                profilePictUri = result
                blurViewModel.setImgUri(result)
                binding.ivProfile.setImageURI(result)
            }
        }

    private fun setPictureBlur() {
        val image = BitmapFactory.decodeFile(requireActivity().applicationContext.filesDir.path + File.separator + "photoProfile" + File.separator + "profile-photo.png")
        if (image != null){
            binding.ivProfile.setImageBitmap(image)
        }
    }

    private fun saveProfilePicture(imgUri: Uri){
        val resolver = requireActivity().applicationContext.contentResolver
        val picture = BitmapFactory.decodeStream(resolver.openInputStream(Uri.parse(imgUri.toString())))
        blurViewModel.applyBlur()
        saveProfile(requireActivity(), picture)
    }

    private fun saveProfile(appContext : Context, bitmap: Bitmap) : Uri {
        val name = "profile-photo.png"
        val outputDir = File(appContext.filesDir, "photoProfile")
        if (!outputDir.exists()){
            outputDir.mkdirs()
        }
        val outputFile = File(outputDir, name)
        var out : FileOutputStream? = null
        try {
            out = FileOutputStream(outputFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, out)
        }finally {
            out?.let {
                try {
                    it.close()
                }catch (ignore : IOException){

                }
            }
        }
        return Uri.fromFile(outputFile)
    }

    private fun editProfile() {
        val user = args.userProfile

        binding.apply {
            dataUserUpdate = user

            btnBackToProfile.setOnClickListener {
                findNavController().navigateUp()
            }

            btnEdit.setOnClickListener {
                userPref.idUser.asLiveData().observe(viewLifecycleOwner){ id ->
                    val username = etUsername.text.toString()
                    val fullname = etFullName.text.toString()
                    val bornDate = etBornDate.text.toString()
                    val address = etAddress.text.toString()

                    if (etUsername.text!!.isEmpty() || etFullName.text!!.isEmpty() || etBornDate.text!!.isEmpty() || etAddress.text!!.isEmpty()){
                        Toast.makeText(context, getString(R.string.tidak_boleh_kosong), Toast.LENGTH_SHORT).show()
                    }else{
                        Log.d("UserId", id.toString())
                        profilePictUri?.let { saveProfilePicture(it) }
                        updateDataUser(id.toString(), username, fullname, bornDate, address)
                        editSharePrefUsername(username)
                        findNavController().navigateUp()
                    }
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
        GlobalScope.launch {
            userPref.saveUpdatedUsername(username)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}