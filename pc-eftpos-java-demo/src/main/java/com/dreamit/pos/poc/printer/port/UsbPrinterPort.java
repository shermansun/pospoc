package com.dreamit.pos.poc.printer.port;

import com.dreamit.pos.poc.util.PropertiesReader;

import javax.usb.*;
import java.util.List;
import java.util.Properties;

/**
 * Created by sherman_sun on 30/10/17.
 */
public class UsbPrinterPort implements PrinterPort {

	private static final int ID_VENDOR = 0x04b8, ID_PRODUCT = 0x0202;
	private static final byte INTERFACE_NUMBER = 0, ENDPOINT_NUMBER = 1;
	private int idVendor, idProduct;
	private byte interfaceNumber, endpointNumber;
	private UsbInterface usbInterface;
	private UsbEndpoint endpoint;
	private UsbPipe pipe;

	@Override
	public void initialize() {

		loadParameters();

		try {
			UsbServices services = UsbHostManager.getUsbServices();
			UsbDevice printer = null;
			List<UsbDevice> devices = services.getRootUsbHub().getAttachedUsbDevices();
			for (UsbDevice child : devices){
				UsbDeviceDescriptor descriptor = child.getUsbDeviceDescriptor();
				if (descriptor.idVendor() == idVendor && descriptor.idProduct() == idProduct){
					printer = child;
				}
			}

			if (printer == null){
				throw new IllegalStateException("USB Printer not found");
			}

			UsbConfiguration configuration = printer.getActiveUsbConfiguration();

			usbInterface = configuration.getUsbInterface(interfaceNumber);
			usbInterface.claim();

			endpoint = usbInterface.getUsbEndpoint(endpointNumber);
			pipe = endpoint.getUsbPipe();
			pipe.open();


		} catch (UsbException e) {
			e.printStackTrace();
		}


	}

	@Override
	public void initialize(String portName) {

	}

	@Override
	public void cleanup() {

		try{
			if (pipe != null){
				pipe.close();
			}
		}
		catch (UsbException ex){
			throw new IllegalStateException("Cannot close usb port.", ex);
		}

		try{
			if (usbInterface != null){
				usbInterface.release();
			}
		}
		catch (UsbClaimException ex){
			throw new IllegalStateException("Cannot close usb port.", ex);
		}
		catch (UsbException ex){
			throw new IllegalStateException("Cannot close usb port.", ex);
		}
	}

	@Override
	public int writeBytes(byte[] bytes) {

		int numBytes = 0;

		try{
			numBytes = pipe.syncSubmit(bytes);
		}
		catch (Exception ex){
			throw new IllegalStateException("Cannot write bytes to usb port.", ex);
		}
		return numBytes;
	}


	private void loadParameters() {
		Properties props = PropertiesReader.getProperties();
		idVendor = props.getProperty("printer.usb.idvendor") != null
				? Integer.parseInt(props.getProperty("printer.usb.idvendor"))
				: ID_VENDOR;
		idProduct = props.getProperty("printer.usb.idproduct") != null
				? Integer.parseInt(props.getProperty("printer.usb.idproduct"))
				: ID_PRODUCT;
		interfaceNumber = props.getProperty("printer.usb.interface") != null
				? Byte.parseByte(props.getProperty("printer.usb.interface"))
				: INTERFACE_NUMBER;
		endpointNumber = props.getProperty("printer.usb.endpoint") != null
				? Byte.parseByte(props.getProperty("printer.usb.endpoint"))
				: ENDPOINT_NUMBER;
	}
}
