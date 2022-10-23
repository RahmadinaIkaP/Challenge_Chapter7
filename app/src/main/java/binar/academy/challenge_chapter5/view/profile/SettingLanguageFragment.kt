package binar.academy.challenge_chapter5.view.profile

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import binar.academy.challenge_chapter5.R
import binar.academy.challenge_chapter5.databinding.FragmentSettingLanguageBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class SettingLanguageFragment : BottomSheetDialogFragment() {
    private var _binding : FragmentSettingLanguageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingLanguageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            rbUbahKeIndonesia.setOnCheckedChangeListener { _, isClick : Boolean ->
                if (isClick){
                    setLocale("id")
                    dismiss()
                }
            }

            rbUbahKeInggris.setOnCheckedChangeListener { _, isClick : Boolean ->
                if (isClick){
                    setLocale("en")
                    dismiss()
                }
            }
        }

    }

    private fun setLocale(lang : String) {
        val myLocale = Locale(lang)
        val res = resources
        val conf = res.configuration
        conf.locale = myLocale
        res.updateConfiguration(conf, res.displayMetrics)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        findNavController().navigate(R.id.profileFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}