package com.dreamit.pos.poc.printer.port;

import com.dreamit.pos.poc.util.PropertiesReader;
import jssc.SerialPort;
import jssc.SerialPortException;


/**
 * Created by ssun on 29/10/17.
 */
public class SerialPrinterPort implements PrinterPort{

	private final static String DEFAULT_COM = "COM1";
	private SerialPort serialPort;

	public void initialize() {
		try {
			serialPort = new SerialPort(getPort());
			serialPort.openPort();
			serialPort.setParams(SerialPort.BAUDRATE_9600,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
		} catch (SerialPortException ex) {
			throw new IllegalStateException("Coudn't init serial port.", ex);
		}

	}

	private String getPort() {
		PropertiesReader.loadProperties();
		return PropertiesReader.getProperties().getProperty("printer.com", DEFAULT_COM);
	}

	public void cleanup() {
		try {
			if (serialPort != null) {
				serialPort.closePort();
			}
		} catch (SerialPortException ex) {
			//logger.error("Couldn't close serial port.", ex);
		}
	}

	public int writeBytes(byte[] bytes) {
		try {
			serialPort.writeBytes(bytes);
			return bytes.length;
		} catch (SerialPortException ex) {
			throw new IllegalStateException("Couldn't write bytes to serial port.", ex);
		}
	}
}