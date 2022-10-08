package binar.academy.challenge_chapter5.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import binar.academy.challenge_chapter5.R
import binar.academy.challenge_chapter5.data.model.DisneyCharaResponse
import binar.academy.challenge_chapter5.databinding.FragmentHomeBinding
import binar.academy.challenge_chapter5.ui.disneycharacter.DisneyCharaAdapter
import binar.academy.challenge_chapter5.ui.disneycharacter.viewmodel.DisneyCharaViewModel
import binar.academy.challenge_chapter5.utils.Constant.Companion.USER
import binar.academy.challenge_chapter5.utils.Constant.Companion.USERNAME
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment(), DisneyCharaAdapter.DisneyCharaInterface {
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val disneyViewModel : DisneyCharaViewModel by viewModels()
    private lateinit var adapter : DisneyCharaAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences(USER, Context.MODE_PRIVATE)
        setUsernameText()

        setRvData()

        binding.btnProfile.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }
    }

    private fun setRvData() {
        adapter = DisneyCharaAdapter(this)

        binding.apply{
            disneyViewModel.showDisneyCharaList(5)
            disneyViewModel.getLiveDataDisneyChara().observe(viewLifecycleOwner){
                if ( it != null){
                    progressBar.visibility = View.GONE
                    adapter.setData(it.data)
                }else{
                    progressBar.visibility = View.GONE
                    Snackbar.make(binding.root, getString(R.string.gagal_load_data), Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.light_gray))
                        .setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_blue))
                        .show()
                }
            }

            rvDisneyChara.layoutManager = GridLayoutManager(context, 2)
            rvDisneyChara.adapter = adapter
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUsernameText() {
        val username = sharedPreferences.getString(USERNAME, null)
        binding.tvGreetUsername.text = getString(R.string.selamat_datang) + " $username!"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(disneyChara: DisneyCharaResponse) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFilmFragment(disneyChara.id)
        findNavController().navigate(action)
    }
}