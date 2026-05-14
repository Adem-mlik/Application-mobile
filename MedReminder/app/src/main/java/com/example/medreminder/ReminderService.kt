package com.example.medreminder

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.medreminder.MainActivity
import com.medreminder.R
import kotlinx.coroutines.*

class ReminderService : Service() {

    private val CHANNEL_ID = "med_reminder_channel"
    private val NOTIFICATION_ID = 1

    private lateinit var db: FirebaseFirestore
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        db = FirebaseFirestore.getInstance()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, createNotification())

        // Démarrer la vérification des rappels
        startReminderCheck()

        return START_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Rappels de médicaments",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications pour les rappels de prise de médicaments"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500)
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("MedReminder")
            .setContentText("Système de rappel actif")
            .setSmallIcon(android.R.drawable.ic_menu_edit)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
    }

    private fun startReminderCheck() {
        serviceScope.launch {
            while (isActive) {
                checkMedicationTimes()
                delay(60000) // Vérifier chaque minute
            }
        }
    }

    private suspend fun checkMedicationTimes() {
        val currentTime = java.text.SimpleDateFormat("HH:mm", java.util.Locale.FRANCE)
            .format(java.util.Date())

        try {
            val snapshot = db.collection("medications")
                .whereEqualTo("scheduledTime", currentTime)
                .get()
                .await()

            if (!snapshot.isEmpty) {
                sendReminderNotification(snapshot.documents.size)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun sendReminderNotification(medCount: Int) {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("🔔 Heure de prise")
            .setContentText("$medCount médicament(s) à prendre")
            .setSmallIcon(android.R.drawable.ic_menu_edit)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(longArrayOf(0, 500, 200, 500))
            .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID + 1, notification)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}