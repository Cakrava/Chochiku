package com.example.chochiku

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.RemoteMessage
class FirebaseMessagingService : com.google.firebase.messaging.FirebaseMessagingService() {

    private lateinit var mNotificationManager: NotificationManager

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Mengecek apakah pesan FCM berisi perintah "play_sound"
        if (remoteMessage.data.isNotEmpty() && remoteMessage.data["command"] == "play_sound") {
            val soundUrl = remoteMessage.data["sound_url"]
            playSound(applicationContext, soundUrl)
            return // Keluar dari metode jika perintah adalah "play_sound"
        }

        // Kode selanjutnya untuk menangani tindakan lain, seperti notifikasi
        // ...

        // Memainkan suara dan getaran ketika pengguna se reques
        val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val r: Ringtone = RingtoneManager.getRingtone(applicationContext, notification)
        r.play()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            r.isLooping = false
        }

        // Getaran
        val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val pattern = longArrayOf(100, 300, 300, 300)
        v.vibrate(pattern, -1)

        val resourceImage = resources.getIdentifier(
            remoteMessage.notification?.icon, "drawable", packageName
        )

        val builder = NotificationCompat.Builder(this, "CHANNEL_ID")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(resourceImage)
        } else {
            builder.setSmallIcon(resourceImage)
        }

        val resultIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        builder.setContentTitle(remoteMessage.notification?.title)
        builder.setContentText(remoteMessage.notification?.body)
        builder.setContentIntent(pendingIntent)
        builder.setStyle(NotificationCompat.BigTextStyle().bigText(remoteMessage.notification?.body))
        builder.setAutoCancel(true)
        builder.setPriority(Notification.PRIORITY_MAX)

        mNotificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "Your_channel_id"
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_HIGH
            )
            mNotificationManager.createNotificationChannel(channel)
            builder.setChannelId(channelId)
        }

        // notificationId adalah int unik untuk setiap notifikasi yang harus Anda tentukan
        mNotificationManager.notify(100, builder.build())
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
