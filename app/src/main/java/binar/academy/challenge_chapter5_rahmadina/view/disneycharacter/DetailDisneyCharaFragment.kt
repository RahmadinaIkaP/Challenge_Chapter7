package binar.academy.challenge_chapter5_rahmadina.view.disneycharacter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import binar.academy.challenge_chapter5_rahmadina.R
import binar.academy.challenge_chapter5_rahmadina.data.datastore.UserPref
import binar.academy.challenge_chapter5_rahmadina.databinding.FragmentDetailDisneyCharaBinding
import binar.academy.challenge_chapter5_rahmadina.model.DisneyCharaRoom
import binar.academy.challenge_chapter5_rahmadina.viewmodel.DisneyCharaViewModel
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class DetailDisneyCharaFragment : Fragment() {
    private var _binding : FragmentDetailDisneyCharaBinding? = null
    private val binding get() = _binding!!

    private val disneyViewModel : DisneyCharaViewModel by viewModels()
    private val args : DetailDisneyCharaFragmentArgs by navArgs()
    private lateinit var userPref: UserPref
    private var isClicked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailDisneyCharaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPref = UserPref(requireContext())

        binding.btnBackDetailToHome.setOnClickListener {
            findNavController().navigateUp()
        }

        getDetailDisneyChara()
    }

    private fun getDetailDisneyChara() {
        val data = args.idDiscneyChara
        disneyViewModel.showDetailDisneyChara(data.id)
        disneyViewModel.getLiveDataDisneyCharaDetail().observe(viewLifecycleOwner){
            if (it != null){
                binding.apply {
                    Glide.with(requireContext())
                        .load(it.imageUrl)
                        .into(ivDisneyCharaDetail)
                    tvCharaName.text = it.name

                    for(films in it.films){
                        tvFilmAppear.text = films
                    }
                    for (shortFilms in it.shortFilms){
                        tvShortFilmAppear.text = shortFilms
                    }
                    for (tvShows in it.tvShows){
                        tvTvShowsAppear.text = tvShows
                    }
                    for (games in it.videoGames){
                        tvGamesAppear.text = games
                    }

                }
            }else{
                Snackbar.make(binding.root, getString(R.string.gagal_load_data), Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.light_gray))
                    .setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_blue))
                    .show()
            }

            GlobalScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main){
                    val count = disneyViewModel.checkFavDisney(data.id)
                    isClicked = if (count > 0){
                        binding.btnFavorite.setImageResource(R.drawable.ic_favorite)
                        true
                    }else{
                        binding.btnFavorite.setImageResource(R.drawable.ic_unfav)
                        false
                    }
                }
            }

            binding.btnFavorite.setOnClickListener {
                isClicked = !isClicked
                userPref.username.asLiveData().observe(viewLifecycleOwner){ username ->
                    if (username != null){
                        checkButtonClicked(isClicked, DisneyCharaRoom(data.id, data.name, data.imageUrl, username))
                    }
                }
            }

        }
    }

    private fun checkButtonClicked(isClicked : Boolean, data: DisneyCharaRoom) {
        if (isClicked){
            userPref.username.asLiveData().observe(viewLifecycleOwner){
                disneyViewModel.insertFavDisney(DisneyCharaRoom(data.id, data.name, data.imageUrl, it))
            }
            binding.btnFavorite.setImageResource(R.drawable.ic_favorite)
            Snackbar.make(binding.root, getString(R.string.berhasil_favorit), Snackbar.LENGTH_SHORT)
                .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.light_gray))
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_blue))
                .show()
        }else{
            disneyViewModel.deleteFavDisney(data)
            binding.btnFavorite.setImageResource(R.drawable.ic_unfav)
            Snackbar.make(binding.root, getString(R.string.berhasil_unfavorit), Snackbar.LENGTH_SHORT)
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