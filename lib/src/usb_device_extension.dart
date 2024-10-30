import 'package:usb_manager/src/internal_plugin_interface.dart';
import 'package:usb_manager/src/usb_connection.dart';
import 'package:usb_manager/src/usb_serial_models.dart';
import 'package:usb_serial/usb_serial.dart';

extension CreateConnection on UsbDevice {
  Future<UsbPort?> connectSerial({
    int baudRate = 9600,
    UsbDatabits dataBits = UsbDatabits.d8,
    UsbStopbits stopBits = UsbStopbits.s1,
    UsbParity parity = UsbParity.none,
  }) async {
    return await UsbManagerInternal.connectSerial(
      this,
      baudRate: baudRate,
      dataBits: dataBits,
      stopBits: stopBits,
      parity: parity,
    );
  }

  Future<UsbConnection?> connectUsb() async {
    return await UsbManagerInternal.connectUsb(this);
  }
}
