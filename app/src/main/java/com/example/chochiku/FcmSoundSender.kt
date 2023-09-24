package com.celestial.notifikasi
import android.app.Activity
import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class FcmSoundSender(
    private val userFcmToken: String,
    private val command: String,
    private val soundUrl: String,
    private val mContext: Context,
    private val sActivity: Activity
) {

    private lateinit var requestQueue: RequestQueue
    private val postUrl = "https://fcm.googleapis.com/fcm/send"
    private val fcmServerKey = "AAAA5OdoTpk:APA91bG5-zk_gkdUfA9UezHASqPzRir1VmKdG-k8XX1ZWGNHoOP82mOON1jNFkGfNDxjewCJRfJIhjYPGUHqh0XQVmBjiPDmLIcYWxB1nLRiKGk3CmgPNpAuqkfjjBfQIa66bYFsMiji"

    fun sendCommand() {
        requestQueue = Volley.newRequestQueue(sActivity)
        val mainObj = JSONObject()
        try {
            mainObj.put("to", userFcmToken)
            val dataObject = JSONObject()
            dataObject.put("command", command)
            dataObject.put("sound_url", soundUrl)
            mainObj.put("data", dataObject)

            val request = object : JsonObjectRequest(Method.POST, postUrl, mainObj,
                Response.Listener { response ->
                    // Tambahkan logika jika berhasil mengirim perintah
                },
                Response.ErrorListener { error ->
                    // Tambahkan penanganan kesalahan di sini
                }) {
                override fun getHeaders(): HashMap<String, String> {
                    val header = HashMap<String, String>()
                    header["content-type"] = "application/json"
                    header["authorization"] = "key=$fcmServerKey"
                    return header
                }
            }
            requestQueue.add(request)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}
