package it.elsyco.usb_manager

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Build
import io.flutter.Log

class PermissionManager(context: Context, private val manager: UsbManager):
    BroadcastReceiver() {
    private val intent = PendingIntent.getBroadcast(
        context,
        0,
        Intent(USB_PERMISSION),
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else 0
    )
    private val filter = IntentFilter(USB_PERMISSION)
    private val requestMap = HashMap<UsbDevice, (result: Boolean) -> Unit>()

    init {
        context.registerReceiver(this, filter)
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(UsbManagerPlugin.LOG_TAG, "Received intent: $intent")
        if (intent.action == USB_PERMISSION) {
            synchronized(this) {
                val device: UsbDevice? =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        intent.getParcelableExtra(UsbManager.EXTRA_DEVICE, UsbDevice::class.java)
                    } else {
                        @Suppress("DEPRECATION") intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)
                    }
                if (device == null) return

                val result = intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)
                Log.d(UsbManagerPlugin.LOG_TAG, "Completing as $result for $device")
                requestMap[device]?.invoke(result)
            }
        }
    }

    fun requestPermission(device: UsbDevice, cb: (result: Boolean) -> Unit) {
        if (manager.hasPermission(device)) {
            cb(true)
            return
        }

        requestMap[device] = cb
        manager.requestPermission(device, intent)

//        Log.d(UsbManagerPlugin.LOG_TAG, "${device.manufacturerName}: Reached sync")
//        synchronized(this) {
//            Log.d(UsbManagerPlugin.LOG_TAG, "${device.manufacturerName}: Waiting for previous")
//            currentRequest?.get()
//            Log.d(UsbManagerPlugin.LOG_TAG, "${device.manufacturerName}: Asking permission")
//            currentRequest = CompletableFuture<Boolean>().apply {
//                whenComplete { res, _ ->
//                    Log.d(UsbManagerPlugin.LOG_TAG, "${device.manufacturerName}: Result $res")
//                    cb(res)
//                }
//            }
//        }
//        manager.requestPermission(device, intent)
    }

    companion object {
        private const val USB_PERMISSION = "it.elsyco.usb_thermal_printer.intents.usb_permission"
    }
}