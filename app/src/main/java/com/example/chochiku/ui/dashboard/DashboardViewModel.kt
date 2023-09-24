package com.example.chochiku.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class DashboardViewModel : ViewModel() {
    private val prefLiveData = MutableLiveData<String>() // Gantilah dengan tipe data yang sesuai

    // Fungsi ini digunakan untuk mengganti nilai pref LiveData
    fun updatePref(newPrefValue: String) {
        prefLiveData.value = newPrefValue
    }

    // Fungsi ini digunakan untuk mengambil LiveData pref
    fun getPrefLiveData(): LiveData<String> {
        return prefLiveData
    }
}
