import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:usb_manager/src/usb_connection.dart';
import 'package:usb_serial/usb_serial.dart';

import 'package:usb_manager/src/usb_manager_platform_interface.dart';

/// An implementation of [UsbManagerPlatform] that uses method channels.
class MethodChannelUsbManager extends UsbManagerPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('it.elsyco.usb_manager/channel');

  @override
  Future<UsbConnection?> connectUsb(UsbDevice device) async {
    final resp = await methodChannel.invokeMethod<bool>(
      'connect',
      {'deviceId': device.deviceId},
    );

    if (resp == true) {
      return UsbConnection.fromPlatform(device);
    }

    return null;
  }

  @override
  Future<void> writeUsb(UsbConnection connection, Uint8List bytes) async {
    await methodChannel.invokeMethod('write', {
      'deviceId': connection.device.deviceId,
      'bytes': bytes,
    });
  }

  @override
  Future<void> disconnect(UsbConnection connection) async {
    await methodChannel.invokeMethod('disconnect', {
      'deviceId': connection.device.deviceId,
    });
  }

  @override
  Future<bool> requestPermission(UsbDevice device) async {
    return await methodChannel.invokeMethod<bool>('requestPermission', {
          'deviceId': device.deviceId,
        }) ??
        false;
  }
}
