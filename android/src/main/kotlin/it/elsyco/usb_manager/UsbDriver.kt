package it.elsyco.usb_manager

import android.app.Activity
import android.content.Context
import android.hardware.usb.UsbConstants
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import io.flutter.plugin.common.MethodChannel

class UsbDriver(private val context: Context) {
    private lateinit var activity: Activity
    private lateinit var usbManager: UsbManager
    private lateinit var permissionManager: PermissionManager

    private val usbConnections = HashMap<Int, UsbPrinterConnection>()

    fun init(activity: Activity) {
        this.activity = activity
        usbManager = activity.getSystemService(Context.USB_SERVICE) as UsbManager
        permissionManager = PermissionManager(context, usbManager)
    }

    private fun getDevice(deviceId: Int): UsbDevice? {
        return usbManager.deviceList.values.firstOrNull {
            it.deviceId == deviceId
        }
    }

    fun connectToDevice(deviceId: Int, result: MethodChannel.Result) {
        val device = getDevice(deviceId)
        if (device == null) {
            result.error(ErrorCodes.DEVICE_NOT_FOUND, "Device not found", null)
            return
        }

        permissionManager.requestPermission(device) { permission ->
            if (!permission) {
                result.error(ErrorCodes.PERMISSION_NOT_GRANTED, "Permission not granted", null)
                return@requestPermission
            }

            if (device.deviceId in usbConnections) {
                result.success(true)
                return@requestPermission
            }

            val usbInterface = device.getInterface(0)
            for (i in 0 until usbInterface.endpointCount) {
                val usbEndpoint = usbInterface.getEndpoint(i)
                if (usbEndpoint.type == UsbConstants.USB_ENDPOINT_XFER_BULK && usbEndpoint.direction == UsbConstants.USB_DIR_OUT) {
                    val connection = usbManager.openDevice(device)
                    if (connection == null) {
                        result.success(false)
                        return@requestPermission
                    }

                    if (connection.claimInterface(usbInterface, true)) {
                        usbConnections[device.deviceId] =
                            UsbPrinterConnection(usbEndpoint, connection)
                        result.success(true)
                    } else {
                        result.success(false)
                    }
                }
            }
        }
    }

    fun writeToDevice(deviceId: Int, bytes: ByteArray, result: MethodChannel.Result) {
        val device = getDevice(deviceId)
        if (device == null) {
            result.error(ErrorCodes.DEVICE_NOT_FOUND, "Device not found", null)
            return
        }

        if (device.deviceId !in usbConnections) {
            result.error(ErrorCodes.CONNECTION_NOT_FOUND, "Connection not found", null)
            return
        }

        usbConnections[device.deviceId]!!.write(bytes)
        result.success(true)
    }

    fun disconnectFromDevice(deviceId: Int, result: MethodChannel.Result) {
        if (deviceId !in usbConnections) {
            result.success(true)
            return
        }

        usbConnections[deviceId]!!.close()
        usbConnections.remove(deviceId)
    }

    fun requestPermission(deviceId: Int, result: MethodChannel.Result) {
        val device = getDevice(deviceId)
        if (device == null) {
            result.error(ErrorCodes.DEVICE_NOT_FOUND, "Device not found", null)
            return
        }

        permissionManager.requestPermission(device) { permissionResult ->
            result.success(permissionResult)
        }
    }
}