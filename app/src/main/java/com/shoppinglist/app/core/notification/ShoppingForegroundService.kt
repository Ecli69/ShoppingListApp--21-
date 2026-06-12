package com.shoppinglist.app.core.notification

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*
import java.util.Locale

private const val TAG = "ShoppingForegroundService"

class ShoppingForegroundService : Service() {

    companion object {
        private const val CHANNEL_ID = "shopping_channel_001"
        private const val NOTIFICATION_ID = 1001
        const val ACTION_CHECK_ITEM = "ACTION_CHECK_ITEM"
        const val EXTRA_ITEM_ID = "EXTRA_ITEM_ID"

        fun startService(context: Context, itemName: String) {
            Log.d(TAG, "startService called for item: $itemName")
            val intent = Intent(context, ShoppingForegroundService::class.java)
            intent.putExtra("item_name", itemName)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stopService(context: Context) {
            Log.d(TAG, "stopService called")
            val intent = Intent(context, ShoppingForegroundService::class.java)
            context.stopService(intent)
        }
    }

    private var serviceJob: Job? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val itemName = intent?.getStringExtra("item_name") ?: "Buy groceries"
        Log.d(TAG, "onStartCommand, itemName: $itemName")

        val isRussian = Locale.getDefault().language == "ru"

        val title = if (isRussian) "🛒 Режим покупок" else "🛒 Shopping Mode"
        val text = if (isRussian) "Сейчас нужно купить: $itemName" else "Current item: $itemName"
        val actionText = if (isRussian) "✓ Вычеркнуть" else "✓ Check off"

        createNotificationChannel()

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(android.R.drawable.ic_menu_edit)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)
            .addAction(
                android.R.drawable.ic_menu_delete,
                actionText,
                getCheckIntent(itemName)
            )
            .build()

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> {
                startForeground(
                    NOTIFICATION_ID,
                    notification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
                )
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                startForeground(
                    NOTIFICATION_ID,
                    notification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
                )
            }
            else -> {
                startForeground(NOTIFICATION_ID, notification)
            }
        }

        serviceJob = CoroutineScope(Dispatchers.IO).launch {
            delay(120000)
            Log.d(TAG, "Auto-stopping service after 2 minutes")
            stopSelf()
        }

        return START_NOT_STICKY
    }

    private fun getCheckIntent(itemName: String): PendingIntent {
        Log.d(TAG, "getCheckIntent for item: $itemName")
        val intent = Intent(this, NotificationReceiver::class.java)
        intent.action = ACTION_CHECK_ITEM
        intent.putExtra(EXTRA_ITEM_ID, itemName)

        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        return PendingIntent.getBroadcast(this, 0, intent, flags)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val isRussian = Locale.getDefault().language == "ru"
            val channelName = if (isRussian) "Сервис покупок" else "Shopping Service"
            val channelDescription = if (isRussian) "Показывает текущий товар для покупки" else "Shows current shopping item"

            val channel = NotificationChannel(
                CHANNEL_ID,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = channelDescription
                enableVibration(true)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
        serviceJob?.cancel()
    }
}