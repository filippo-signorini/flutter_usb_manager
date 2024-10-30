import 'package:usb_manager/src/internal_plugin_interface.dart';
import 'package:usb_serial/usb_serial.dart';

class UsbManager {
  static Future<List<UsbDevice>> listDevices() async {
    return await UsbManagerInternal.listDevices();
  }

  static Stream<UsbEvent> get usbEventStream {
    return UsbManagerInternal.usbEventStream;
  }
}
