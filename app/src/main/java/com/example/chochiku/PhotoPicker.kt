package com.example.chochiku

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import com.example.chochiku.util.pref
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PhotoPicker : AppCompatActivity() {
    private var ref= FirebaseDatabase.getInstance().reference

    private lateinit var pref: pref // Pastikan pref diimpor dengan benar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_picker)

        // Inisialisasi pref dengan benar
        pref = pref(this)

        // Panggil metode preview() setelah pref diinisialisasi
        preview()
        profil()
    }

    private fun preview() {
        val btnsimpan = findViewById<Button>(R.id.btn_simpan)

        val view = findViewById<ImageView>(R.id.img_view)
        val btnv1 =findViewById<ImageButton>(R.id.btn_v1)
        val btnv2 = findViewById<ImageButton>(R.id.btn_v2)
        val btnv3 = findViewById<ImageButton>(R.id.btn_v3)
        val btnv4 = findViewById<ImageButton>(R.id.btn_v4)
        val btnv5 = findViewById<ImageButton>(R.id.btn_v5)
        val btnv6 = findViewById<ImageButton>(R.id.btn_v6)

        val p1 = findViewById<ImageView>(R.id.view_pref1)
        val p2 = findViewById<ImageView>(R.id.view_pref2)
        val p3 = findViewById<ImageView>(R.id.view_pref3)
        val p4 = findViewById<ImageView>(R.id.view_pref4)
        val p5 = findViewById<ImageView>(R.id.view_pref5)
        val p6 = findViewById<ImageView>(R.id.view_pref6)

        var lastClickedButton: ImageButton? = null
        val username = pref.getUsername()
        var alamat1 = 0
        var alamat2 = 0
        var alamat3 = 0
        var alamat4 = 0
        var alamat5 = 0
        var alamat6 = 0



        var ppaktid = pref.getPhoto()
        if (username == "joyokilatmoko") {
            alamat1 = R.drawable.j1
            alamat2 = R.drawable.j2
            alamat3 = R.drawable.j3
            alamat4 = R.drawable.j4
            alamat5 = R.drawable.j5
            alamat6 = R.drawable.j6
        } else {
            alamat1 = R.drawable.v1
            alamat2 = R.drawable.v2
            alamat3 = R.drawable.v3
            alamat4 = R.drawable.v4
            alamat5 = R.drawable.v5
            alamat6 = R.drawable.v6
        }

        p1.setImageResource(alamat1)
        p2.setImageResource(alamat2)
        p3.setImageResource(alamat3)
        p4.setImageResource(alamat4)
        p5.setImageResource(alamat5)
        p6.setImageResource(alamat6)
        btnv1.setOnClickListener {
            view.setImageResource(alamat1)
//            Toast.makeText(this, "Ngecek aja", Toast.LENGTH_SHORT).show()
            lastClickedButton = btnv1
        }
        btnv2.setOnClickListener {
            view.setImageResource(alamat2)
            lastClickedButton = btnv2
        }
        btnv3.setOnClickListener {
            view.setImageResource(alamat3)
            lastClickedButton = btnv3
        }
        btnv4.setOnClickListener {
            view.setImageResource(alamat4)
            lastClickedButton = btnv4
        }
        btnv5.setOnClickListener {
            view.setImageResource(alamat5)
            lastClickedButton = btnv5
        }
        btnv6.setOnClickListener {
            view.setImageResource(alamat6)
            lastClickedButton = btnv6
        }


        var dataphoto =""
        var photo = ""
        if (username == "joyokilatmoko") {
            photo = "jphoto"
        } else {
            photo = "vphoto"
        }
        btnsimpan.setOnClickListener() {
            val ref = ref.child("data")
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                val pp = ref.child(photo).toString()
                    ref.child(photo).setValue(dataphoto) // Mengupdate nilai limit

                }
                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                }
            })

            when (lastClickedButton) {
                btnv1 -> {
                    if (username == "vrawidiaputri") {
                        pref.setPhoto("v1")
                        dataphoto = "v1"
                        navigateToDashboardFragment()

                    }
                    else{
                        dataphoto = "j2"
                        pref.setPhoto("j1")
                        navigateToDashboardFragment()
                    }
                }
                btnv2 -> {
                    if (username == "vrawidiaputri") {
                        pref.setPhoto("v2")
                        dataphoto = "v2"
                        navigateToDashboardFragment()
                    }
                    else{
                        dataphoto = "j2"
                        pref.setPhoto("j2")
                        navigateToDashboardFragment()
                    }
                }
                btnv3 -> {
                    if (username == "vrawidiaputri") {
                        pref.setPhoto("v3")
                        dataphoto = "v3"
                        navigateToDashboardFragment()
                    }
                    else{
                        pref.setPhoto("j3")
                        dataphoto = "j3"
                        navigateToDashboardFragment()
                    }  }
                btnv4 -> {
                    if (username == "vrawidiaputri") {
                        pref.setPhoto("v4")
                        dataphoto = "v4"
                        navigateToDashboardFragment()
                    }
                    else{
                        pref.setPhoto("j4")
                        dataphoto = "j4"
                        navigateToDashboardFragment()
                    } }
                btnv5 -> {
                    if (username == "vrawidiaputri") {
                        pref.setPhoto("v5")
                        dataphoto = "v5"
                        navigateToDashboardFragment()
                    }
                    else{
                        pref.setPhoto("j5")
                        dataphoto = "j5"
                        navigateToDashboardFragment()
                    }}
                else -> {
                    if (username == "vrawidiaputri") {
                        pref.setPhoto("v6")
                        dataphoto = "v6"
                        navigateToDashboardFragment()
                    }
                    else{
                        pref.setPhoto("j6")
                        dataphoto = "j6"
                        navigateToDashboardFragment()
                    } }
            }
        }






    }
    private fun profil() {

        val username = pref.getUsername()
        val pp = findViewById<ImageView>(R.id.img_view)
        var imageDrawable = 0
        val pref_photo = pref.getPhoto()


// Berdasarkan username, atur Drawable gambar profil yang sesuai
        if (username == "joyokilatmoko") {
            if (pref_photo == "j1") {
                imageDrawable = R.drawable.j1
            }else if(pref_photo == "j2") {
                imageDrawable = R.drawable.j2
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


    private fun navigateToDashboardFragment() {
            finish()

    }
}
