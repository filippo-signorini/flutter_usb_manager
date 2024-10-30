import 'dart:typed_data';

import 'package:plugin_platform_interface/plugin_platform_interface.dart';
import 'package:usb_manager/src/usb_connection.dart';
import 'package:usb_serial/usb_serial.dart';

import 'package:usb_manager/src/usb_manager_method_channel.dart';

abstract class UsbManagerPlatform extends PlatformInterface {
  /// Constructs a UsbManagerPlatform.
  UsbManagerPlatform() : super(token: _token);

  static final Object _token = Object();

  static UsbManagerPlatform _instance = MethodChannelUsbManager();

  /// The default instance of [UsbManagerPlatform] to use.
  ///
  /// Defaults to [MethodChannelUsbManager].
  static UsbManagerPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [UsbManagerPlatform] when
  /// they register themselves.
  static set instance(UsbManagerPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<UsbConnection?> connectUsb(UsbDevice device) {
    throw UnimplementedError('connectUsb() has not been implemented.');
  }

  Future<void> writeUsb(UsbConnection connection, Uint8List bytes) {
    throw UnimplementedError('writeUsb() has not been implemented.');
  }

  Future<void> disconnect(UsbConnection connection) {
    throw UnimplementedError('disconnect() has not been implemented.');
  }

  Future<bool> requestPermission(UsbDevice device) {
    throw UnimplementedError('requestPermission() has not been implemented');
  }
}
