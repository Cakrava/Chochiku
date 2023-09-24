import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chochiku.R
import com.example.chochiku.model.*

class AdapterTabungan(private val listTabungan: List<ModelTabungan>) :
    RecyclerView.Adapter<AdapterTabungan.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_riwayat, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val tabungan = listTabungan[position]
        holder.tvWaktu.text = tabungan.waktu
        holder.tvKeterangan.text = tabungan.keterangan
        holder.tvTanggal.text = tabungan.tanggal
        holder.tvLabel.text = tabungan.label
    }

    override fun getItemCount(): Int = listTabungan.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvWaktu: TextView = itemView.findViewById(R.id.tv_waktu)
        val tvKeterangan: TextView = itemView.findViewById(R.id.tv_keterangan)
        val tvTanggal: TextView = itemView.findViewById(R.id.tv_tanggal)
        val tvLabel: TextView = itemView.findViewById(R.id.tv_label)
    }
}
