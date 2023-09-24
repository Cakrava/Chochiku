package com.example.chochiku.ui.home
import AdapterTabungan

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chochiku.MainActivity
import com.example.chochiku.R

import java.text.SimpleDateFormat
import java.util.*
import com.example.chochiku.databinding.FragmentHomeBinding
import com.example.chochiku.model.ModelTabungan

import com.example.chochiku.util.pref
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import java.util.Calendar
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment() {
    private var klikCount = 0
    private lateinit var pref: pref // Ganti dengan nama kelas Pref yang sesuai
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    var hutang =0
    private val firebaseMessaging = FirebaseMessaging.getInstance()
    private var ref= FirebaseDatabase.getInstance().reference

//    override fun onResume() {
//        super.onResume()
//        binding.btnRefresh.setOnClickListener() {
//            (activity as? MainActivity)?.push()
//        }
//
//
//    }
    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        pref = pref(requireContext()) // Gantilah dengan cara Anda menginisialisasi Pref

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        FirebaseMessaging.getInstance().subscribeToTopic("all");
        generateToken()
//        }

//        konfismasi bayar hutan
        binding.btnBayarhutang.setOnClickListener {
            // Tampilkan dialog konfirmasi
            konfirmasibayarhutang()
        }


        binding.btnRefresh.setOnClickListener {
            // Tampilkan dialog konfirmasi
            ubahjumlah()


        }

        tampilriwayat()
       tampilstamp()
        hutang()
        hari()
        sapa()
        profil()


        return root
    }



    private fun tampilriwayat() {

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("tabungan")
        // Tampilkan data dari database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listTabungan = mutableListOf<ModelTabungan>()
                for (data in snapshot.children) {
                    val tabungan = data.getValue(ModelTabungan::class.java)
                    listTabungan.add(tabungan!!)
                }

                // Sort data berdasarkan tanggal
                listTabungan.sortByDescending { it.urutan}

                // Ambil 3 data terbaru
                val dataTerbaru = listTabungan.take(2)

                // Set layout manager
                binding.tvRiwayat.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                // Set data ke adapter
                binding.tvRiwayat.adapter = AdapterTabungan(dataTerbaru)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }




    //get token

    fun generateToken(): Unit {
        val builder = AlertDialog.Builder(requireContext())
        val ref = FirebaseDatabase.getInstance().reference

        firebaseMessaging.token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d("TokenGenerator", "Token: $token")
                pref.setToken(token)
                var dbtoken=""
                val user = pref.getUsername()
                if (user=="joyokilatmoko"){
                    dbtoken = "jtoken"
                }
                else{
                    dbtoken = "vtoken"
                }

                val saldoRef = ref.child("data")
                saldoRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        saldoRef.child(dbtoken).setValue(token) // Mengupdate nilai limit
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle error
                    }
                })

            } else {
                Log.e("TokenGenerator", "Failed to generate token", task.exception)
            }
        }
    }

    private fun profil() {
        val binding = _binding ?: return

        val username = pref.getUsername()
        val pp = binding.previewAkun
        var imageDrawable = 0
        var pref_photo = pref.getPhoto()


// Berdasarkan username, atur Drawable gambar profil yang sesuai
        if (username == "joyokilatmoko") {

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
    private fun sapa() {
        val sapaTextView = binding.sapa
        val username = pref.getUsername() // Ganti dengan cara Anda menyimpan username

// Mendapatkan waktu saat ini
        val currentTime = Calendar.getInstance()
        val hourOfDay = currentTime.get(Calendar.HOUR_OF_DAY)

// Menentukan pesan selamat malam berdasarkan jam
        val greeting = when (hourOfDay) {
            in 5..9 -> "Selamat Pagi"
            in 10..13 -> "Selamat Siang"
            in 14..17 -> "Selamat Sore"
            else -> "Selamat Malam"
        }
//                penentuan usernya
        val pengguna = when {
            // Cek jika pengguna joyokilatmoko
            username == "joyokilatmoko" -> "Joyo"
            // Cek jika pengguna vrawidiaputri
            username == "vrawidiaputri" -> "Vra"
            // Tambahkan pengecekan untuk pengguna lain jika diperlukan
            else -> "Pengguna Tidak Dikenal"
        }
        val selamatMalam = "$greeting, $pengguna!"
        sapaTextView.text = selamatMalam

//                ini ngatur tanggal
        val tanggalTextView = binding.tanggal
        val currentDate = Date()
        // Membuat format tanggal yang diinginkan (contoh: "dd MMMM yyyy")
        val dateFormat = SimpleDateFormat(
            "dd MMMM yyyy",
            Locale("id", "ID")
        ) // Ubah ke locale yang sesuai
        // Mengonversi tanggal saat ini ke format yang diinginkan
        val formattedDate = dateFormat.format(currentDate)
        tanggalTextView.text = formattedDate



    }
    private fun tampilstamp(){
        var  progressBar =binding.progressBar
        progressBar.progress = 0

        val username = pref.getUsername()
        var stamp = ""
        var tmplhutang = ""
        var keterangan = ""
        if (username == "joyokilatmoko" ){
            stamp ="stampjoyo"
            tmplhutang ="hjoyo"
            keterangan ="kjoyo"
        }
        else{
            stamp="stampvra"
            tmplhutang ="hvra"
            keterangan ="kvra"
        }
        val database = FirebaseDatabase.getInstance().getReference("saldo")

        // Gunakan addValueEventListener() untuk mendengarkan perubahan data
        database.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SuspiciousIndentation")
            override fun onDataChange(snapshot: DataSnapshot) {
                // Tampilkan data yang telah dibaca
                val total = snapshot.child("total").value.toString()
                val limit = snapshot.child("limit").value.toString()
                binding.tvSaldo.text = "Rp."+total
                binding.tvLimit.text = "Rp."+limit
                val stamp = snapshot.child(stamp).value.toString()
                if (stamp == "1"){
                    binding.idStamp.text = "1"
                    progressBar.progress = 25

                }
                else if (stamp == "2"){
                    binding.idStamp.text = "2"
                    progressBar.progress = 50

                }
                else if (stamp == "3"){
                    binding.idStamp.text = "3"
                    progressBar.progress = 75

                }
                else if (stamp == "4"){
                    binding.idStamp.text = "MAX"
                    progressBar.progress = 100

                }
                else if (stamp == "0"){
                    binding.idStamp.text = "0"
                    progressBar.progress = 0

                }
                val tvhutang = snapshot.child(tmplhutang).value.toString()
                binding.tvHutang.text = "Rp."+tvhutang

                val tvmasa = snapshot.child(keterangan).value.toString()
                binding.tvMasa.text = tvmasa
                val penanda = snapshot.child("penanda").value.toString()
                    if (penanda == "1") {
                        binding.gifloader.isInvisible = true
                        binding.gifloader.alpha = 0.0f

                    }else{
                        binding.gifloader.isInvisible = false

                    }





//


            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })




    }
    private fun hutang() {
        // Deklarasi
        val user = pref.getUsername()
        val lm = binding.tvLimit.text.toString()
        val denda = lm.replace("Rp.", "")
        val ref = FirebaseDatabase.getInstance().reference
        val btntabung = binding.btnTabung
        var hutang = ""
        var ket = ""
        var ketDefMasa = "Anda dalam masa menabung"
        var ketDefSudah = "Anda sudah menabung"

        // Logika untuk menentukan nilai hutang dan keterangan
        if (user == "joyokilatmoko") {
            hutang = "hjoyo"
            ket = "kjoyo"
        } else {
            hutang = "hvra"
            ket = "kvra"
        }

        val hutangRef = ref.child("saldo")

        // Logika pengecekan hari Senin
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        if (dayOfWeek == Calendar.MONDAY) {
            // Hari Senin, secara otomatis akan menyimpan ketdetmasa
            hutangRef.child(ket).setValue(ketDefMasa)
        }

        //membuka konfirmasi
        btntabung.setOnClickListener {
            konfirmasitabungan()

        }

        // Logika untuk tombol btnTabung diklik

        hutangRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Logika untuk totalHutangBaru jika tidak ada klik dalam 1 minggu
                val lastUpdated = dataSnapshot.child("lastUpdated").getValue(Long::class.java) ?: 0
                val currentTime = System.currentTimeMillis()
                val oneWeekMillis = TimeUnit.DAYS.toMillis(7)

                if (currentTime - lastUpdated >= oneWeekMillis) {
                    // Jika sudah 1 minggu sejak update terakhir, maka
                    // akan mengganti ketdetmasa di hari Senin
                    if (dayOfWeek == Calendar.MONDAY) {
                        hutangRef.child(ket).setValue(ketDefMasa)
                    }

                    // Menambah totalHutangBaru jika tidak ada klik dalam 1 minggu
                    val totalHutang = dataSnapshot.child(hutang).getValue(Long::class.java) ?: 0
                    val limit_database = dataSnapshot.child("limit").getValue(Long::class.java) ?: 0
                    val totalSaldoBaru = totalHutang + limit_database
                    hutangRef.child(hutang).setValue(totalSaldoBaru)

                    // Menyimpan waktu update terakhir
                    hutangRef.child("lastUpdated").setValue(currentTime)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }
    private fun hari(){
    val tvHari = binding.tvHari
    val calendar = Calendar.getInstance().time

    // Cek tanggal hari ini
    val day = calendar.day
    if (day == 1) {
        tvHari.text = "Senin"

    } else if (day == 2) {

        tvHari.text = "Selasa"
    } else if (day == 3) {

        tvHari.text = "Rabu"
    } else if (day == 4) {

        tvHari.text = "Kamis"
    } else if (day == 5) {

        tvHari.text = "Jumat"
    } else if (day == 6) {

        tvHari.text = "Sabtu"
    } else if (day == 7) {

        tvHari.text = "Minggu"
    }


}

    @SuppressLint("SuspiciousIndentation")
    private fun ubahjumlah() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Ubah Jumlah Tabungan")
        // Membuat EditText
        val edJmlTabung = EditText(requireContext())
        edJmlTabung.hint = "Masukkan jumlah tabungan"
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,

        )
        edJmlTabung.layoutParams = layoutParams

        builder.setView(edJmlTabung)

        builder.setPositiveButton("Ubah") { _, _ ->
            (activity as? MainActivity)?.notiflimit()

            val edJmlTabung = edJmlTabung.text.toString()
            val limit = edJmlTabung.toInt()
            val ref = FirebaseDatabase.getInstance().reference
            // Mengambil nilai limit dari EditText (di sini diasumsikan EditText bernama edLimit)


            // Memperbarui total saldo
                    val saldoRef = ref.child("saldo")
                    saldoRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            saldoRef.child("limit").setValue(limit) // Mengupdate nilai limit
                            Toast.makeText(requireContext(), "Limit tabungan berhasil diubah", Toast.LENGTH_SHORT).show()
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Handle error
                        }
                    })
                }

        builder.setNegativeButton("Batal") { dialog, _ ->
            dialog.cancel()
        }

        val dialog = builder.create()
        dialog.show()
    }




    private fun konfirmasibayarhutang() {
        val edbayarhutang = binding.edBayarhutang.text
        val bayarhutang = edbayarhutang.toString()
        val rp = "Rp. $bayarhutang ?"

        if (edbayarhutang.isNotEmpty()) {

            if (edbayarhutang.length < 4) {
                Toast.makeText(requireContext(), "Utang ngga ada kurang dari Rp.1000!", Toast.LENGTH_SHORT).show()
            } else {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Konfirmasi Tabung")
                builder.setMessage("Anda akan menabung sejumlah uang sebesar $rp")
                builder.setPositiveButton("Ya") { _, _ ->
                    (activity as? MainActivity)?.pushHutang()
                    binding.edBayarhutang.setText("")
                    val hutangangka = bayarhutang.toDouble()
                    val tanggal = binding.tanggal.text.toString()
                    val username = pref.getUsername() // Ganti dengan cara Anda menyimpan username
                    var keterangan = ""
                    var label = ""
                    var hutang = ""
                    var urutan = 0
                    if (username == "vrawidiaputri") {
                        keterangan = "Vra Widia Putri telah membayar hutang"
                        label = "hutang"
                        hutang = "hvra"
                    } else if (username == "joyokilatmoko") {
                        keterangan = "Joyo Kilat Moko telah membayar hutang"
                        label = "hutang"
                        hutang = "hjoyo"
                    } else {
                        keterangan = "Pengguna Tidak Dikenal telah menabung"
                        label = "ngga ada"
                        hutang = "kosong"
                    }

                    val calendar = Calendar.getInstance()
                    val sdf = SimpleDateFormat("HH:mm")
                    val waktuSekarang = sdf.format(calendar.time)

                    val ref = FirebaseDatabase.getInstance().reference
                    val tabunganRef = ref.child("tabungan") // Gunakan tabel tabungan
                    // Mengecek urutan terakhir dari Firebase
                    tabunganRef.orderByChild("urutan").limitToLast(1).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            var urutanTerakhir = 0
                            for (data in dataSnapshot.children) {
                                val tabungan = data.getValue(ModelTabungan::class.java)
                                if (tabungan != null) {
                                    urutanTerakhir = tabungan.urutan
                                }
                            }

                            // Menambah 1 ke urutan terakhir untuk mendapatkan urutan berikutnya
                            urutanTerakhir++
                            urutan = urutanTerakhir

                            // Menambah data ke tabel "tabungan" dengan urutan yang baru
                            val tabunganId = tabunganRef.push().key
                            val tbg = ModelTabungan(tabunganId, label, waktuSekarang, tanggal, urutan, keterangan)
                            if (tabunganId != null) {
                                tabunganRef.child(tabunganId).setValue(tbg).addOnSuccessListener {
                                    Toast.makeText(requireContext(), "Yeay udah bayar hutang nih!", Toast.LENGTH_SHORT).show()

                                    // Memperbarui total saldo
                                    val saldoRef = ref.child("saldo")
                                    saldoRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                                            val totalSaldo = dataSnapshot.child("total").getValue(Long::class.java) ?: 0
                                            val totalhutang = dataSnapshot.child(hutang).getValue(Long::class.java) ?: 0
                                            val pengurangan_hutang = totalhutang - hutangangka
                                            val totalSaldoBaru = totalSaldo + hutangangka
                                            saldoRef.child("total").setValue(totalSaldoBaru)
                                            saldoRef.child(hutang).setValue(pengurangan_hutang)
                                        }

                                        override fun onCancelled(databaseError: DatabaseError) {
                                            // Handle error
                                        }
                                    })

                                }
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Handle error
                        }
                    })
                }

                builder.setNegativeButton("Tidak") { _, _ ->
                    // Tindakan yang akan diambil jika "Tidak" diklik
                }
                val dialog = builder.create()
                dialog.show()
            }
        } else {
            Toast.makeText(requireContext(), "Masukkan nominal!", Toast.LENGTH_SHORT).show()
        }
    }



    private fun konfirmasitabungan() {
        val idstam = binding.idStamp.text
        val default = 0
        val username = pref.getUsername()
        var stamp = ""
        if (username == "joyokilatmoko") {
            stamp = "stampjoyo"
        } else {
            stamp = "stampvra"
        }
        if (idstam == "MAX") {
            val saldoRef = ref.child("saldo")
            saldoRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    saldoRef.child(stamp).setValue(default) // Mengupdate nilai limit
                    Toast.makeText(requireContext(), "Stamp kamu akan telah di reset ke 0, silahkan menabung!", Toast.LENGTH_LONG).show()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                }
            })
        } else {
            val tvLimit = binding.tvLimit
            val limit = tvLimit.text.toString()
            val rp = "Rp. $limit ?"

            val limitAngka = limit.replace("Rp.", "")

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Konfirmasi Tabung")
            builder.setMessage("Anda akan menabung sejumlah uang sebesar $rp")
            builder.setPositiveButton("Ya") { _, _ ->
                val hutangRef = ref.child("saldo")

                (activity as? MainActivity)?.pushnotification()

                val nilaiTabung = limitAngka.toInt()
                val tanggal = binding.tanggal.text.toString()
                val username = pref.getUsername() // Ganti dengan cara Anda menyimpan username
                var keterangan = ""
                var label = ""
//
                var stamp = ""
                var ket = ""
                var ketDefSudah = "Anda sudah menabung minggu ini"
                if (username == "vrawidiaputri") {
                    keterangan = "Vra Widia Putri telah menabung"
                    label = "tabung"
                    stamp = "stampvra"
                    ket = "kvra"
                } else if (username == "joyokilatmoko") {
                    keterangan = "Joyo Kilat Moko telah menabung"
                    label = "tabung"
                    stamp = "stampjoyo"
                    ket = "kjoyo"
                } else {
                    keterangan = "Pengguna Tidak Dikenal telah menabung"
                    label = "ngga ada"
                }

                val calendar = Calendar.getInstance()
                val sdf = SimpleDateFormat("HH:mm")
                val waktuSekarang = sdf.format(calendar.time)

                hutangRef.child(ket).setValue(ketDefSudah)
                var urutan = 0

                // Mengecek urutan terakhir dari Firebase
                val tabunganRef = ref.child("tabungan")
                tabunganRef.orderByChild("urutan").limitToLast(1).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        var urutanTerakhir = 0
                        for (data in dataSnapshot.children) {
                            val tabungan = data.getValue(ModelTabungan::class.java)
                            if (tabungan != null) {
                                urutanTerakhir = tabungan.urutan
                            }
                        }

                        // Menambah 1 ke urutan terakhir untuk mendapatkan urutan berikutnya
                        urutanTerakhir++
                        urutan = urutanTerakhir

                        // Menambah data ke tabel "tabungan" dengan urutan yang baru
                        val tabunganId = tabunganRef.push().key
                        val tbg = ModelTabungan(tabunganId, label, waktuSekarang, tanggal, urutan, keterangan)
                        if (tabunganId != null) {
                            tabunganRef.child(tabunganId).setValue(tbg).addOnSuccessListener {
                                Toast.makeText(requireContext(), "Yeay udah nabung nih!", Toast.LENGTH_SHORT).show()

                                // Memperbarui total saldo
                                val saldoRef = ref.child("saldo")
                                saldoRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        val totalSaldo = dataSnapshot.child("total").getValue(Long::class.java) ?: 0
                                        val totalSaldoBaru = totalSaldo + nilaiTabung
                                        saldoRef.child("total").setValue(totalSaldoBaru)

                                        // Mengelola stamp
                                        val stamp_database = dataSnapshot.child(stamp).getValue(Long::class.java) ?: 0
                                        var totalstamp = stamp_database + 1
                                        if (totalstamp >= 5) {
                                            totalstamp = 0
                                        }

                                        saldoRef.child(stamp).setValue(totalstamp)
                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {
                                        // Handle error
                                    }
                                })
                            }
                        }
                    }
//ssssssss
                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle error
                    }
                })
            }

            builder.setNegativeButton("Tidak") { _, _ ->
                // Tindakan yang akan diambil jika "Tidak" diklik
            }
            val dialog = builder.create()
            dialog.show()
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}


