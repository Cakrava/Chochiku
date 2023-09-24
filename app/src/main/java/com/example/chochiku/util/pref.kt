package com.example.chochiku.util

import android.content.Context
import android.content.SharedPreferences
import android.provider.ContactsContract.Contacts.Photo

class pref(activity: Context) {


    private var sp: SharedPreferences? = null
    private val login = "Login"
    private val usernameKey = "username" // Menambahkan kunci untuk username
    private val tanggalMulaiKey = "tanggal_mulai" // Menambahkan kunci untuk tanggal mulai
    private val tokenKey = "token" // Menambahkan kunci untuk username
    private val photoKey = "photo" // Menambahkan kunci untuk username
    private val musikKey = "musik" // Menambahkan kunci untuk username

    init {
        sp = activity.getSharedPreferences("Mypref", Context.MODE_PRIVATE)
    }

    fun getPhoto(): String? {
        return sp!!.getString(photoKey, null)
    }


    fun setPhoto(photo: String) {
        sp!!.edit().putString(photoKey, photo).apply()
    }
    fun setISlogin(value: Boolean) {
        sp!!.edit().putBoolean(login, value).apply()
    }

    fun getISlogin(): Boolean {
        return sp!!.getBoolean(login, false)
    }
    fun setUsername(username: String) {
        sp!!.edit().putString(usernameKey, username).apply()
    }

    fun setToken(username: String) {
        sp!!.edit().putString(tokenKey, username).apply()
    }

    fun getToken(): String? {
        return sp!!.getString(tokenKey, null)
    }

    fun setMusik(musik: String) {
        sp!!.edit().putString(musikKey, musik).apply()
    }

    fun getMusik(): String? {
        return sp!!.getString(musikKey, null)
    }

    // Menambahkan fungsi untuk mendapatkan username
    fun getUsername(): String? {
        return sp!!.getString(usernameKey, null)
    }
    fun getTanggalMulai(currentTime: Long): Long {
        return sp!!.getLong(tanggalMulaiKey, 0)
    }


    fun setTanggal(tanggalMulai: Long) {
        sp!!.edit().putLong(tanggalMulaiKey, tanggalMulai).apply()
    }

    fun getTanggal(): Long {
        return sp!!.getLong(tanggalMulaiKey, 0)
    }



}
