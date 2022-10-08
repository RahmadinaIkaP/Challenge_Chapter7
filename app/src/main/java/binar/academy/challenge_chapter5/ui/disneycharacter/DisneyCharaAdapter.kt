package binar.academy.challenge_chapter5.ui.disneycharacter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import binar.academy.challenge_chapter5.data.model.DisneyCharaResponse
import binar.academy.challenge_chapter5.databinding.ItemFilmBinding
import com.bumptech.glide.Glide

class DisneyCharaAdapter(private val onClick : DisneyCharaInterface) : RecyclerView.Adapter<DisneyCharaAdapter.ViewHolder>() {

    private val limit = 30

    private val differCallback = object : DiffUtil.ItemCallback<DisneyCharaResponse>(){
        override fun areItemsTheSame(
            oldItem: DisneyCharaResponse,
            newItem: DisneyCharaResponse
        ): Boolean {
            return oldItem.id == oldItem.id
        }

        override fun areContentsTheSame(
            oldItem: DisneyCharaResponse,
            newItem: DisneyCharaResponse
        ): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    inner class ViewHolder(private val binding : ItemFilmBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(disneyChara: DisneyCharaResponse){
                binding.dataDisneyChara = disneyChara

                Glide.with(itemView)
                    .load(disneyChara.imageUrl)
                    .into(binding.ivDisneyChara)

                itemView.setOnClickListener {
                    onClick.onItemClick(disneyChara)
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemFilmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        if (differ.currentList.size > limit){
            return limit
        }else{
            return differ.currentList.size
        }
    }

    fun setData(data : List<DisneyCharaResponse>){
        differ.submitList(data)
    }

    interface DisneyCharaInterface {
        fun onItemClick(disneyChara: DisneyCharaResponse)
    }
}