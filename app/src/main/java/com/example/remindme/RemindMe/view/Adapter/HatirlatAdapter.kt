package com.example.remindme.RemindMe.view.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.remindme.RemindMe.view.Model.HatirlaticiModel
import com.example.remindme.RemindMe.view.View.HatirlaticiListeFragmentDirections
import com.example.remindme.databinding.RecyclerRowBinding

class HatirlatAdapter : RecyclerView.Adapter<HatirlatAdapter.HatirlaticiViewHolder>() {

    private var hatirlaticilar: List<HatirlaticiModel> = listOf()

    // ViewHolder sınıfı
    class HatirlaticiViewHolder(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root)

    // onCreateViewHolder: Her bir öğe için view hazırlığı
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HatirlaticiViewHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HatirlaticiViewHolder(binding)
    }

    // getItemCount: Listedeki öğe sayısı
    override fun getItemCount(): Int {
        return hatirlaticilar.size
    }

    // onBindViewHolder: Her öğeyi bağlamak
    override fun onBindViewHolder(holder: HatirlaticiViewHolder, position: Int) {
        // Veriyi öğeye bağla
        holder.binding.textView.text = hatirlaticilar[position].Konu

        // Tıklama işlemi
        holder.itemView.setOnClickListener {
            // Eski hatırlatıcıya yönlendirmek için tıklama işlevi
            val action = HatirlaticiListeFragmentDirections.actionHatirlaticiListeFragmentToHatirlaticiEkleFragment("eski", hatirlaticilar[position].id)
            Navigation.findNavController(it).navigate(action)
        }
    }

    // Adapter'e verileri göndermek için bir fonksiyon
    fun submitList(newList: List<HatirlaticiModel>) {
        hatirlaticilar = newList
        notifyDataSetChanged()  // Veriler değiştiğinde RecyclerView'u güncelle
    }
}
