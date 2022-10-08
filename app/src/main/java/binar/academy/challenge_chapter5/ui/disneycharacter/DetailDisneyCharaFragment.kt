package binar.academy.challenge_chapter5.ui.disneycharacter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import binar.academy.challenge_chapter5.R
import binar.academy.challenge_chapter5.databinding.FragmentDetailDisneyCharaBinding
import binar.academy.challenge_chapter5.ui.disneycharacter.viewmodel.DisneyCharaViewModel
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar

class DetailDisneyCharaFragment : Fragment() {
    private var _binding : FragmentDetailDisneyCharaBinding? = null
    private val binding get() = _binding!!

    private val disneyViewModel : DisneyCharaViewModel by viewModels()
    private val args : DetailDisneyCharaFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailDisneyCharaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getDetailDisneyChara()
    }

    private fun getDetailDisneyChara() {
        val id = args.idDiscneyChara
        disneyViewModel.showDetailDisneyChara(id)
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
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}