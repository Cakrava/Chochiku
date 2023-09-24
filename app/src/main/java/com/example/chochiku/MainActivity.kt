package com.example.chochiku

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.celestial.notifikasi.FcmSoundSender

import com.example.chochiku.databinding.ActivityMainBinding

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.chochiku.util.pref
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var pref: pref // Ganti dengan nama kelas Pref yang sesuai
    private val firebaseMessaging = FirebaseMessaging.getInstance()
    private var ref= FirebaseDatabase.getInstance().reference
    private val SPLASH_DELAY: Long = 1000 // Waktu tampilan splash screen (10 detik)

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.load_scr)

        val pref = pref(this)

        // Handler untuk menunda pembukaan aktivitas selama 1 detik
        Handler(Looper.getMainLooper()).postDelayed({
            // Simulate a loading process for 2 seconds
            // After 2 seconds, check if the user is logged in
            if (!pref.getISlogin()) {
                // If not logged in, redirect to the login page
                startActivity(Intent(this, LoginActivity::class.java))
                finish() // Selesai untuk menghapus layar loading
            } else {
                // If logged in, switch to the main view
                setContentView(R.layout.activity_main) // Menggunakan layout utama
                val navView: BottomNavigationView = findViewById(R.id.nav_view)
                val navController = findNavController(R.id.nav_host_fragment_activity_main)
                val appBarConfiguration = AppBarConfiguration(
                    setOf(
                        R.id.navigation_home, R.id.navigation_dashboard
                    )
                )
                setupActionBarWithNavController(navController, appBarConfiguration)
                navView.setupWithNavController(navController)
            }
        }, 1000) // Menunda selama 1 detik (1000 milidetik)
    }



    //    fungsi notifikasi
     fun pushnotification() {
        val pref = pref(this)

        val user = pref.getUsername()
        var judul = ""
        var isi = ""
        var mytoken = ""

    if (user == "joyokilatmoko") {
             judul = "Lihat tabungan mu!!🥳"
             isi = "Yeay!!, baru saja uang tabunganmu bertambah, Joyo baru aja menabung.👉"
             mytoken  =  "vtoken"
        }else {
             judul = "Lihat tabungan mu!!🥳"
             isi = "Yeay!!, baru saja uang tabunganmu bertambah, Vra baru aja menabung.👉"
             mytoken = "jtoken"
        }

    val saldoRef = ref.child("data")
    saldoRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val token = dataSnapshot.child(mytoken).getValue(String::class.java).toString()

            val notificationsSender =
                FcmNotificationsSender(
                    token,
                    judul,
                    isi,
                    applicationContext,
                    this@MainActivity

                )
            notificationsSender.sendNotifications()


        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Handle error
        }
    })


    }

    fun pushHutang() {
        val pref = pref(this)

        val user = pref.getUsername()
        var judul = ""
        var isi = ""
        var mytoken = ""

        if (user == "joyokilatmoko") {
            judul = "Ada yang mau lunas ni!!🥳"
            isi = "Yeay!!,Joyo baru aja bayar hutang loh, kamu kapan?👉"
            mytoken  =  "vtoken"
        }else {
            judul = "Ada yang mau lunas ni!!🥳"
            isi = "Yeay!!,Joyo baru aja bayar hutang loh, kamu kapan?👉"
            mytoken = "jtoken"
        }

        val saldoRef = ref.child("data")
        saldoRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val token = dataSnapshot.child(mytoken).getValue(String::class.java).toString()

                val notificationsSender =
                    FcmNotificationsSender(
                        token,
                        judul,
                        isi,
                        applicationContext,
                        this@MainActivity

                    )
                notificationsSender.sendNotifications()


            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })


    }

    fun notiflimit() {
        val pref = pref(this)

        val user = pref.getUsername()
         var   judul = "Limit mu berubah!!😲"
           var isi = "Limit tabungan diubah nih, coba cek limit nya."
                val notificationsSender =
                    FcmNotificationsSender(
                        "/topics/all",
                        judul,
                        isi,
                        applicationContext,
                        this@MainActivity

                    )
                notificationsSender.sendNotifications()
            }



    fun notice() {
        val pref = pref(this)

        val user = pref.getUsername()
        var judul = ""
        var isi = ""
        var mytoken = ""

        if (user == "joyokilatmoko") {
            mytoken = "vtoken"
        } else {
            mytoken = "jtoken"
        }
        var soundUrl = ""
        var musik = pref.getMusik()
        if (musik == "musik1") {
            //terompet
            soundUrl =
                "https://firebasestorage.googleapis.com/v0/b/notifikasi-4724b.appspot.com/o/terompet.mp3?alt=media&token=2ee55430-3e8b-4426-a2d4-5b02d6c71b36"

        } else if (musik == "musik2") {
//            cukurukuk
            soundUrl =
           "https://firebasestorage.googleapis.com/v0/b/chochiku.appspot.com/o/cukurukuk.mp3?alt=media&token=581f2ea5-96d0-4567-b169-02a7db45d7dd"  }
        else if (musik == "musik3") {
//            lapor komandan
            soundUrl ="https://firebasestorage.googleapis.com/v0/b/chochiku.appspot.com/o/order_masuk_komandan.mp3?alt=media&token=8c800129-a8d8-473f-916f-3a4b38fcefce"
           } else if (musik == "musik4") {
            soundUrl =
                "https://firebasestorage.googleapis.com/v0/b/chochiku.appspot.com/o/ajojing.mp3?alt=media&token=0b0a772b-40c8-4bd5-b2c4-cfa99afc228d"
        } else if (musik == "musik5") {
            soundUrl =
                "https://firebasestorage.googleapis.com/v0/b/chochiku.appspot.com/o/fring_new.mp3?alt=media&token=4a9e643d-fc24-4833-9f3b-708646727a54"
        }else{
            soundUrl =
                "https://firebasestorage.googleapis.com/v0/b/chochiku.appspot.com/o/nokia_hummingbird.mp3?alt=media&token=34ab3b5c-4f9b-43f2-8f7b-0fdacf2b737c"

        }

        val saldoRef = ref.child("data")
        saldoRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val token = dataSnapshot.child(mytoken).getValue(String::class.java).toString()
                val fcmSender = FcmSoundSender(
                    token,
                    "play_sound",
                    soundUrl,
                    applicationContext,
                    this@MainActivity
                )
                fcmSender.sendCommand()

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })

    }
    }


