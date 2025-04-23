package com.example.sedonortdd.ui.lokasi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.sedonortdd.R
import com.example.sedonortdd.data.models.Location

class LokasiAdapter(list: List<Location>?, listener: OnItemClickListener, glideRequestManager: RequestManager) :
    RecyclerView.Adapter<LokasiAdapter.MyViewHolder>() {
    private var list: List<Location>? = list
    private val mListener = listener
    private val glideRequestManager: RequestManager = glideRequestManager

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_lokasi_donor, parent, false)
        return MyViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val location: Location = list!![position]
        holder.bind(location, glideRequestManager)
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    fun updateData(locations: List<Location>) {
        list = locations
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View, listener: OnItemClickListener?) : RecyclerView.ViewHolder(itemView) {
        private val nama: TextView = itemView.findViewById<TextView>(R.id.tvNamaDonor)
        private val lokasi: TextView = itemView.findViewById<TextView>(R.id.tvLokasiDonor)
        private val gambar: ImageView = itemView.findViewById<ImageView>(R.id.ivGambar)

        init {
            itemView.setOnClickListener { _: View? ->
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(position)
                }
            }
        }

        fun bind(location: Location, glide: RequestManager) {
            nama.text = location.name
            lokasi.text = location.location

            glide.load(location.photo)
                .placeholder(R.drawable.loading)
                .into(gambar)
        }
    }
}