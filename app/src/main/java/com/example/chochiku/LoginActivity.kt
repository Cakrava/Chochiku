package com.example.chochiku

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.chochiku.util.pref

class LoginActivity : AppCompatActivity() {

    private lateinit var pref: pref // Ganti dengan nama kelas Pref yang sesuai

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        pref = pref(this)

        val loginButton = findViewById<Button>(R.id.btn_login)
        val usernameEditText = findViewById<EditText>(R.id.username)
        val pinEditText = findViewById<EditText>(R.id.pin)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val pin = pinEditText.text.toString()

            if (username == "joyokilatmoko" && pin == "240301") {
                // Login berhasil untuk pengguna joyo
                pref.setISlogin(true)
                pref.setPhoto("j1")
                pref.setUsername("joyokilatmoko") // Simpan username
                // Arahkan pengguna ke MainActivity dengan fragment Home
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("fragment", "home")
                startActivity(intent)
                finish()
            } else if (username == "vrawidiaputri" && pin == "060501") {
                // Login berhasil untuk pengguna vra
                pref.setISlogin(true)
                pref.setPhoto("v1")
                pref.setMusik("musik1")
                pref.setUsername("vrawidiaputri") // Simpan username
                // Arahkan pengguna ke MainActivity dengan fragment Home
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("fragment", "home")
                startActivity(intent)
                finish()
            } else {
                // Login gagal, tampilkan pesan kesalahan
                Toast.makeText(this, "Login Gagal. Cek username dan PIN.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
