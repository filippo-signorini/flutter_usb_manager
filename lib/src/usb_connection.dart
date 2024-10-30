import 'dart:typed_data';

import 'package:flutter/services.dart';
import 'package:usb_manager/src/internal_plugin_interface.dart';
import 'package:usb_serial/usb_serial.dart';

class UsbConnection {
  const UsbConnection.fromPlatform(this.device);
  final UsbDevice device;

  Future<void> write(Uint8List bytes) async {
    return await UsbManagerInternal.writeUsb(this, bytes);
  }

  Future<void> writeInt(List<int> bytes) async {
    final b = Uint8List.fromList(bytes);
    return await write(b);
  }

  Future<void> close() async {
    return await UsbManagerInternal.disconnect(this);
  }
}
