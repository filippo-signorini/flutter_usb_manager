package it.elsyco.usb_manager

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** UsbManagerPlugin */
class UsbManagerPlugin: FlutterPlugin, MethodCallHandler, ActivityAware {
    private lateinit var channel: MethodChannel
    private lateinit var driver: UsbDriver

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel =
            MethodChannel(flutterPluginBinding.binaryMessenger, "it.elsyco.usb_manager/channel")
        channel.setMethodCallHandler(this)
        driver = UsbDriver(flutterPluginBinding.applicationContext)
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        when (call.method) {
            "connect" -> connectToDevice(call, result)
            "write" -> writeToDevice(call, result)
            "disconnect" -> disconnectFromDevice(call, result)
            "requestPermission" -> requestPermission(call, result)
            else -> result.notImplemented()
        }
    }

    private fun connectToDevice(call: MethodCall, result: Result) {
        val deviceId = call.argument<Int>("deviceId")
        if (deviceId == null) {
            result.error(ErrorCodes.MISSING_ARGUMENT, "Missing device id", null)
            return
        }

        driver.connectToDevice(deviceId, result)
    }

    private fun writeToDevice(call: MethodCall, result: Result) {
        val deviceId = call.argument<Int>("deviceId")
        val bytes = call.argument<ByteArray>("bytes")
        if (deviceId == null || bytes == null) {
            result.error(ErrorCodes.MISSING_ARGUMENT, "Missing device id or bytes.", null)
            return
        }

        driver.writeToDevice(deviceId, bytes, result)
    }

    private fun disconnectFromDevice(call: MethodCall, result: Result) {
        val deviceId = call.argument<Int>("deviceId")
        if (deviceId == null) {
            result.error(ErrorCodes.MISSING_ARGUMENT, "Missing device id", null)
            return
        }

        driver.disconnectFromDevice(deviceId, result)
    }

    private fun requestPermission(call: MethodCall, result: Result) {
        val deviceId = call.argument<Int>("deviceId")
        if (deviceId == null) {
            result.error(ErrorCodes.MISSING_ARGUMENT, "Missing device id", null)
            return
        }

        driver.requestPermission(deviceId, result)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        driver.init(binding.activity)
    }

    override fun onDetachedFromActivityForConfigChanges() {}

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        onAttachedToActivity(binding)
    }

    override fun onDetachedFromActivity() {}

    companion object {
        const val LOG_TAG = "it.elsyco.usb_manager"
    }
}
