import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:usb_manager/usb_manager.dart';

void main() {
  runApp(const MainApp());
}

class MainApp extends StatelessWidget {
  const MainApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              ElevatedButton(
                onPressed: _list,
                child: const Text('List'),
              ),
              ElevatedButton(
                onPressed: _test,
                child: const Text('Test'),
              ),
            ],
          ),
        ),
      ),
    );
  }

  void _list() async {
    if (kDebugMode) {
      print(await UsbManager.listDevices());
    }
  }

  void _test() async {
    final devices = await UsbManager.listDevices();
    final device = devices
        .firstWhere((element) => element.vid == 0x5f9 && element.pid == 0x4204);

    final port = await device.connectSerial();
    if (port == null) return;

    port.inputStream!.listen((event) {
      if (kDebugMode) {
        print(event);
      }
    });
  }
}
