import 'package:usb_serial/usb_serial.dart';

enum UsbDatabits {
  d5(UsbPort.DATABITS_5),
  d6(UsbPort.DATABITS_6),
  d7(UsbPort.DATABITS_7),
  d8(UsbPort.DATABITS_8);

  const UsbDatabits(this.value);
  final int value;
}

enum UsbParity {
  none(UsbPort.PARITY_NONE),
  even(UsbPort.PARITY_EVEN),
  odd(UsbPort.PARITY_ODD),
  mark(UsbPort.PARITY_MARK),
  space(UsbPort.PARITY_SPACE);

  const UsbParity(this.value);
  final int value;
}

enum UsbStopbits {
  s1(UsbPort.STOPBITS_1),
  s1_5(UsbPort.STOPBITS_1_5),
  s2(UsbPort.STOPBITS_2);

  const UsbStopbits(this.value);
  final int value;
}
