// Injected for Glide image loading

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.sedonortdd.R
import com.example.sedonortdd.models.Article

class MyAdapter(list: List<Article>?, listener: OnItemClickListener, glideRequestManager: RequestManager) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    private val list: List<Article>? = list
    private val mListener = listener
    private val glideRequestManager: RequestManager = glideRequestManager

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_artikel, parent, false)
        return MyViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val artikel: Article = list!![position]
        holder.bind(artikel, glideRequestManager)
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    class MyViewHolder(itemView: View, listener: OnItemClickListener?) : RecyclerView.ViewHolder(itemView) {
        private val judul: TextView = itemView.findViewById<TextView>(R.id.tvJudulArtikel)
        private val konten: TextView = itemView.findViewById<TextView>(R.id.tvKonten)
        private val gambar: ImageView = itemView.findViewById<ImageView>(R.id.ivGambar)

        init {
            // Set listener on itemView for clicks
            itemView.setOnClickListener { v: View? ->
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(position)
                }
            }
        }

        fun bind(artikel: Article, glide: RequestManager) {
            judul.setText(artikel.title)
            konten.setText(artikel.content)

            glide.load(artikel.imageUrl)
                .placeholder(R.drawable.loading)
                .into(gambar)
        }
    }
}