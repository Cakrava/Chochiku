package com.example.chochiku.ui.dashboard

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Handler
import android.os.Looper
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.fragment.app.Fragment
import com.example.chochiku.MainActivity

import com.example.chochiku.PhotoPicker
import com.example.chochiku.R

import com.example.chochiku.databinding.FragmentDashboardBinding

import com.example.chochiku.util.pref
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import androidx.core.view.children
class DashboardFragment : Fragment() {

    private var klikCount = 0
    private lateinit var pref: pref // Ganti dengan nama kelas Pref yang sesuai
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    var hutang =0
    private var ref= FirebaseDatabase.getInstance().reference
    private val handler = Handler(Looper.getMainLooper())
    private val updateIntervalMillis = 500 // Interval pemantauan dalam milidetik (contoh: 10 detik)
    private var isCountingDown = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        pref = pref(requireContext()) // Gantilah dengan cara Anda menginisialisasi Pref

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        profil()
        startMonitoring()
      openedit()
        photo_clien()
        binding.btnNotice.setOnClickListener(){
            (activity as? MainActivity)?.notice()
            if (!isCountingDown) {
                startCountDown()
            }
            Toast.makeText(requireContext(), "Sudah tersenggol nih,,asekk!!", Toast.LENGTH_SHORT).show()

            val musicOptions = arrayOf("Cukurukuk", "Terompet", "Order", "Ajojing", "Fring", "Nokia")
            // Menangani klik pada tombol "Tukar"
            binding.btnTukar.setOnClickListener {
                // Membangun dialog pop-up dengan daftar pilihan musik
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Pilih Musik")
                    .setSingleChoiceItems(musicOptions, -1) { dialog: DialogInterface?, which: Int ->
                        // Tangani pemilihan musik di sini, contohnya:
                        val selectedMusic = musicOptions[which]
                        when (selectedMusic) {
                            "Cukurukuk" -> pref.setMusik("musik2")
                            "Terompet" -> pref.setMusik("musik1")
                            "Order" -> pref.setMusik("musik3")
                            "Ajojing" -> pref.setMusik("musik4")
                            "Fring" -> pref.setMusik("musik5")
                            "Nokia" -> pref.setMusik("musik6")
                        }
                    }
                    .setPositiveButton("Simpan") { dialog: DialogInterface?, which: Int ->
                        // Tombol Simpan ditekan, Anda dapat menambahkan logika tambahan di sini jika diperlukan
                        dialog?.dismiss()
                    }
                    .setNegativeButton("Batal") { dialog: DialogInterface?, which: Int ->
                        // Tombol Batal ditekan, tidak ada yang dilakukan di sini, jadi dialog akan ditutup
                        dialog?.dismiss()
                    }

                val dialog = builder.create()
                dialog.show()
            }
        }
        return root
    }


    private fun startCountDown() {
        // Menyembunyikan tombol
        val tv_user_client=binding.tvUserClient
        val btn_notice=binding.btnNotice

        // Menampilkan teks hitung mundur
        tv_user_client.text = "Tunggu 15 detik untuk senggol lagi"

        // Menonaktifkan tombol selama hitung mundur berlangsung
        btn_notice.isEnabled = false

        // Memulai hitung mundur selama 10 detik
        object : CountDownTimer(15000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                tv_user_client.text = "Tunggu $secondsRemaining detik untuk senggol lagi"
            }

            override fun onFinish() {
                // Menampilkan teks asli
                tv_user_client.text = "Klik untuk senggol"

                // Mengaktifkan kembali tombol
                btn_notice.isEnabled = true
                isCountingDown = false
            }
        }.start()

        isCountingDown = true
    }

    private fun photo_clien() {
        var username = pref.getUsername()
        var dataphoto =""
        var photofield = ""

        if (username == "joyokilatmoko") {

            photofield = "vphoto"

        } else {
            photofield = "jphoto"
        }
        val pp_client = binding.ppClient

        val ref = ref.child("data")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val photo = dataSnapshot.child(photofield).getValue(String::class.java)
                if (photo != null) {
                    // Membentuk ID gambar dengan menggabungkan "R.drawable." dan nilai "photo"
                    val imageId = resources.getIdentifier("com.example.chochiku:drawable/$photo", null, null)

                    // Set gambar ke ImageView yang sesuai
                    if (imageId != 0) {
                        pp_client.setImageResource(imageId)
                    } else {
                        // Gambar tidak ditemukan, lakukan penanganan kesalahan atau tindakan lain
                    }
                } else {
                    // "photo" tidak ditemukan dalam data Firebase, lakukan penanganan kesalahan atau tindakan lain
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })

    }


    //-------------------------------------------------
    private fun openedit() {
        binding.btnEdit.setOnClickListener {
            val intent = Intent(requireContext(), PhotoPicker::class.java)
            startActivity(intent)
        }
    }


    private fun profil() {
        val binding = _binding ?: return

        val username = pref.getUsername()
        val pp = binding.photoprofil
        var imageDrawable = 0
        var pref_photo = pref.getPhoto()


// Berdasarkan username, atur Drawable gambar profil yang sesuai
        if (username == "joyokilatmoko") {
            binding.tvTmplnama.setText("Joyo Kilat Moko")
            binding.tvUsername.setText("@joyokilatmoko24")
            if (pref_photo == "j1") {
                imageDrawable = R.drawable.j1
            }else if(pref_photo == "j2") {
                imageDrawable = R.drawable.j1
            }else if(pref_photo == "j3") {
                imageDrawable = R.drawable.j3
            }else if(pref_photo == "j4") {
                imageDrawable = R.drawable.j4
            }else if(pref_photo == "j5") {
                imageDrawable = R.drawable.j5
            }else if(pref_photo == "j6") {
                imageDrawable = R.drawable.j6
            }else{
                imageDrawable = R.drawable.default_photo
            }
        }else{
            binding.tvTmplnama.setText("Vra Widia Putri")
            binding.tvUsername.setText("@widia06")
            if (pref_photo == "v1") {
                imageDrawable = R.drawable.v1
            }else if(pref_photo == "v2") {
                imageDrawable = R.drawable.v2
            }else if(pref_photo == "v3") {
                imageDrawable = R.drawable.v3
            }else if(pref_photo == "v4") {
                imageDrawable = R.drawable.v4
            }else if(pref_photo == "v5") {
                imageDrawable = R.drawable.v5
            }else if(pref_photo == "v6") {
                imageDrawable = R.drawable.v6
            }else{
                imageDrawable = R.drawable.default_photo
            }
        }

        pp.setImageResource(imageDrawable)

    }



    private val monitorRunnable = object : Runnable {
        override fun run() {
            // Panggil fungsi profil untuk memeriksa perubahan
            profil()

            // Setelah selesai memeriksa, jadwalkan pemantauan berikutnya
            handler.postDelayed(this, updateIntervalMillis.toLong())
        }
    }
    private fun startMonitoring() {
        // Mulai pemantauan terus-menerus dengan interval tertentu
        handler.postDelayed(monitorRunnable, updateIntervalMillis.toLong())
    }

    private fun stopMonitoring() {
        // Hentikan pemantauan terus-menerus
        handler.removeCallbacks(monitorRunnable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //    radiobutton
    fun radio(){


    }
}

