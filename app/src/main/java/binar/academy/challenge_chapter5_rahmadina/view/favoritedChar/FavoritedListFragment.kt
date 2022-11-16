package binar.academy.challenge_chapter5_rahmadina.view.favoritedChar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import binar.academy.challenge_chapter5_rahmadina.data.datastore.UserPref
import binar.academy.challenge_chapter5_rahmadina.databinding.FragmentFavoritedListBinding
import binar.academy.challenge_chapter5_rahmadina.viewmodel.DisneyCharaViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritedListFragment : Fragment(){
    private var _binding : FragmentFavoritedListBinding? = null
    private val binding get() = _binding!!

    private val disneyCharViewModel : DisneyCharaViewModel by viewModels()
    private lateinit var adapter : FavoritAdapter
    private lateinit var userPref: UserPref

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritedListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPref = UserPref(requireContext())

        binding.btnBackFavToHome.setOnClickListener {
            findNavController().navigateUp()
        }

        showDataFavorit()
    }

    private fun showDataFavorit() {
        adapter = FavoritAdapter()

        binding.apply {
            disneyCharViewModel.getAllFavDisney()
            disneyCharViewModel.getAllFavDisneyObserver().observe(viewLifecycleOwner){
                adapter.setData(it)
            }

            rvFavorite.layoutManager = GridLayoutManager(context, 2)
            rvFavorite.adapter = adapter
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}