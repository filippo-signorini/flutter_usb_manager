import 'dart:typed_data';

import 'package:usb_manager/src/usb_connection.dart';
import 'package:usb_manager/src/usb_serial_models.dart';
import 'package:usb_serial/usb_serial.dart';
import 'package:usb_manager/src/usb_manager_platform_interface.dart';

class UsbManagerInternal {
  static Future<List<UsbDevice>> listDevices() async {
    return await UsbSerial.listDevices();
  }

  static Future<UsbPort?> connectSerial(
    UsbDevice device, {
    int baudRate = 9600,
    UsbDatabits dataBits = UsbDatabits.d8,
    UsbStopbits stopBits = UsbStopbits.s1,
    UsbParity parity = UsbParity.none,
  }) async {
    final port = await device.create();
    if (port == null) return null;
    if (!await port.open()) return null;
    await port.setDTR(true);
    await port.setRTS(true);
    await port.setPortParameters(
      baudRate,
      dataBits.value,
      stopBits.value,
      parity.value,
    );
    return port;
  }

  static Future<UsbConnection?> connectUsb(UsbDevice device) async {
    return await UsbManagerPlatform.instance.connectUsb(device);
  }

  static Future<void> writeUsb(
      UsbConnection connection, Uint8List bytes) async {
    return await UsbManagerPlatform.instance.writeUsb(connection, bytes);
  }

  static Future<void> disconnect(UsbConnection connection) async {
    return await UsbManagerPlatform.instance.disconnect(connection);
  }

  static Stream<UsbEvent> get usbEventStream {
    return UsbSerial.usbEventStream!;
  }

  static Future<bool> requestPermision(UsbDevice device) async {
    return await UsbManagerPlatform.instance.requestPermission(device);
  }
}
