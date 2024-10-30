package it.elsyco.usb_manager

import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbEndpoint

class UsbPrinterConnection(
    private val usbEndpoint: UsbEndpoint,
    private val usbConnection: UsbDeviceConnection,
) {
    fun write(bytes: ByteArray) {
        usbConnection.bulkTransfer(usbEndpoint, bytes, bytes.size, 10 * 1000)
    }

    fun close() {
        usbConnection.close()
    }
}