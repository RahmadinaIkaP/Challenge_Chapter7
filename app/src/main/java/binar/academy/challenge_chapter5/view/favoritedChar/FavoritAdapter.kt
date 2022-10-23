package binar.academy.challenge_chapter5.view.favoritedChar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import binar.academy.challenge_chapter5.databinding.ItemFilmBinding
import binar.academy.challenge_chapter5.model.DisneyCharaRoom
import binar.academy.challenge_chapter5.model.UserAndFavorite
import com.bumptech.glide.Glide

class FavoritAdapter : RecyclerView.Adapter<FavoritAdapter.ViewHolder>() {

    private val differCallback = object  : DiffUtil.ItemCallback<DisneyCharaRoom>(){
        override fun areItemsTheSame(oldItem: DisneyCharaRoom, newItem: DisneyCharaRoom): Boolean {
            return oldItem.id == oldItem.id
        }

        override fun areContentsTheSame(oldItem: DisneyCharaRoom, newItem: DisneyCharaRoom): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    inner class ViewHolder(private val binding : ItemFilmBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DisneyCharaRoom){
            binding.tvCharaName.text = data.name

            Glide.with(itemView)
                .load(data.imageUrl)
                .into(binding.ivDisneyChara)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemFilmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun setData(data : List<DisneyCharaRoom>){
        differ.submitList(data)
    }
}