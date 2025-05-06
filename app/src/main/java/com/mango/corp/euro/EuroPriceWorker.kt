package com.mango.corp.euro

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import org.json.JSONObject
import java.net.URL

class EuroPriceWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        return try {
            val response = URL("https://api.frankfurter.app/latest?from=EUR&to=BRL").readText()
            val json = JSONObject(response)
            val rate = json.getJSONObject("rates").getDouble("BRL")
            val message = "€ %.2f".format(rate)
            showNotification(message)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    private fun showNotification(rateMessage: String) {
        val channelId = "euro_price_channel"
        val manager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Euro Price Notification",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setShowBadge(true)
            }
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Euro Tracker")
            .setContentText("Current Euro rate: $rateMessage")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(longArrayOf(0, 1000, 500, 1000))
            .setAutoCancel(true)
            .build()

        manager.notify(1, notification)
    }
}
