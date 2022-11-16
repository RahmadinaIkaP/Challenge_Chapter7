package binar.academy.challenge_chapter5_rahmadina.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import binar.academy.challenge_chapter5_rahmadina.R
import binar.academy.challenge_chapter5_rahmadina.data.datastore.UserPref
import binar.academy.challenge_chapter5_rahmadina.model.DisneyCharaResponse
import binar.academy.challenge_chapter5_rahmadina.databinding.FragmentHomeBinding
import binar.academy.challenge_chapter5_rahmadina.view.disneycharacter.DisneyCharaAdapter
import binar.academy.challenge_chapter5_rahmadina.viewmodel.DisneyCharaViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), DisneyCharaAdapter.DisneyCharaInterface {
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val disneyViewModel : DisneyCharaViewModel by viewModels()
    private lateinit var adapter : DisneyCharaAdapter
    private lateinit var userPref: UserPref

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPref = UserPref(requireContext())
        setUsernameText()

        setRvData()

        binding.btnProfile.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }

        binding.btnFavorite.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_favoritedListFragment)
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
        userPref.username.asLiveData().observe(viewLifecycleOwner){
            binding.tvGreetUsername.text = getString(R.string.selamat_datang) + it + "!"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(disneyChara: DisneyCharaResponse) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFilmFragment(disneyChara)
        findNavController().navigate(action)
    }
}