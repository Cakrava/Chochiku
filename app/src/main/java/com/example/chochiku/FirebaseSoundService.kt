package com.example.chochiku

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseSoundService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Mengecek apakah pesan FCM merupakan pesan data
        if (remoteMessage.data.isNotEmpty()) {
            val data = remoteMessage.data

            // Mengecek apakah pesan FCM berisi perintah "play_sound"
            if (data.containsKey("command") && data["command"] == "play_sound") {
                val soundUrl = data["sound_url"]
                playSound(applicationContext, soundUrl)
            }
        }
    }

    private fun playSound(context: Context, soundUrl: String?) {
        if (soundUrl != null) {
            val mediaPlayer = MediaPlayer()
            try {
                mediaPlayer.setDataSource(soundUrl)
                mediaPlayer.prepare()

                val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

                // Meningkatkan volume aplikasi
                val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0)

                mediaPlayer.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            // Handle ketika soundUrl adalah null
            // Misalnya, Anda dapat memutarkan suara default atau menampilkan pesan kesalahan.
        }
    }

}
