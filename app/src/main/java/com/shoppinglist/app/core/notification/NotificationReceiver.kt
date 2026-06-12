package com.shoppinglist.app.core.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "NotificationReceiver"

object NotificationCallback {
    var onItemChecked: ((String) -> Unit)? = null
}

@AndroidEntryPoint
class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        Log.d(TAG, "onReceive called")
        Log.d(TAG, "Action: ${intent?.action}")

        if (intent?.action == ShoppingForegroundService.ACTION_CHECK_ITEM) {
            val itemName = intent.getStringExtra(ShoppingForegroundService.EXTRA_ITEM_ID)
            Log.d(TAG, "Item name from intent: $itemName")

            itemName?.let {
                CoroutineScope(Dispatchers.Main).launch {
                    NotificationCallback.onItemChecked?.invoke(it)
                }
                Log.d(TAG, "Callback invoked for item: $it")
            } ?: Log.e(TAG, "Item name is null!")

            ShoppingForegroundService.stopService(context)
        }
    }
}