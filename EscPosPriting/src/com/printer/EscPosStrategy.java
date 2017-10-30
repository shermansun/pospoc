package com.printer;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import javax.usb.UsbClaimException;
import javax.usb.UsbConfiguration;
import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.UsbEndpoint;
import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbInterface;
import javax.usb.UsbPipe;
import javax.usb.UsbServices;
import jssc.SerialPort;
import jssc.SerialPortException;
import org.apache.log4j.Logger;

/**
 * ESC/POS implementation of the {@link IPrintStrategy} interface. Oriented to
 * Epson ESC/POS printers (ie. Thermal printers)
 *
 * @author nMoncho
 */
public class EscPosStrategy {

  private final static char ESC_CHAR = 0x1B;
  private final static char GS = 0x1D;
  private final static byte[] LINE_FEED = new byte[]{0x0A};
  private final static byte[] CUT_PAPER = new byte[]{GS, 0x56, 0x00};
  private final static byte[] INIT_PRINTER = new byte[]{ESC_CHAR, 0x40};
  private final static byte[] SET_PRINT_MODE = new byte[]{ESC_CHAR, 0x21, 0x00};
  private final static byte[] UNDERLINED_MODE = new byte[]{ESC_CHAR, 0x2D, 0x32};
  private final static byte[] EMPHASIZED_MODE_ON = new byte[]{ESC_CHAR, 0x45, 0x01};
  private final static byte[] EMPHASIZED_MODE_OFF = new byte[]{ESC_CHAR, 0x45, 0x00};
  private final static byte[] DSTRIKE_MODE_ON = new byte[]{ESC_CHAR, 0x47, 0x01};
  private final static byte[] DSTRIKE_MODE_OFF = new byte[]{ESC_CHAR, 0x47, 0x00};
  private final static byte[] ALIGN_LEFT = new byte[]{ESC_CHAR, 0x61, 0x30};
  private final static byte[] ALIGN_CENTER = new byte[]{ESC_CHAR, 0x61, 0x31};
  private final static byte[] ALIGN_RIGHT = new byte[]{ESC_CHAR, 0x61, 0x32};
  private final static byte[] UPSIDE_ON = new byte[]{ESC_CHAR, 0x7B, 0x01};
  private final static byte[] UPSIDE_OFF = new byte[]{ESC_CHAR, 0x7B, 0x00};
  private final static byte[] SELECT_BIT_IMAGE_MODE = {0x1B, 0x2A, 33};
  private final static byte[] SET_LINE_SPACE_24 = new byte[]{ESC_CHAR, 0x33, 24};
  private final static byte[] SET_LINE_SPACE_30 = new byte[]{ESC_CHAR, 0x33, 30};
  private final static byte FONT_POINT = 0x11;
  private final static String DEFAULT_TYPE = "SERIAL", SERIAL_TYPE = "SERIAL", USB_TYPE = "USB";
  private final static String[] PRINTER = {"ESC/POS Epson Printers"};
  private final static Logger logger = Logger.getLogger(EscPosStrategy.class);
  private PrintPort printPort;

  /**
   * Factory method for creating a Printer Service pointing to a serial port
   * printer.
   *
   * @return printing service.
   */
  public static EscPosStrategy createSerialPortStrategy() {
    EscPosStrategy strategy = new EscPosStrategy();
    strategy.printPort = new SerialPrintPort();
    return strategy;
  }

  /**
   * Factory method for creating a Printer Service pointing to a USB port
   * printer.
   *
   * @return printing service.
   */
  public static EscPosStrategy createUSBPortStrategy() {
    EscPosStrategy strategy = new EscPosStrategy();
    strategy.printPort = new USBJavaxPrintPort();
    return strategy;
  }

  public EscPosStrategy() {
    Properties props = loadProperties();
    String type = props.getProperty("printer.escpos.type", DEFAULT_TYPE);
    if (SERIAL_TYPE.equalsIgnoreCase(type)) {
      printPort = new SerialPrintPort();
    } else if (USB_TYPE.equalsIgnoreCase(type)) {
      printPort = new USBJavaxPrintPort();
    } else {
      throw new IllegalArgumentException("Printer type not specified. Please configure USB or Serial.");
    }
  }

  public void print(TicketPrinterJob job) throws TicketPrinterException {
    try {
      printPort.initialize();
      printPort.writeBytes(INIT_PRINTER);
      // Iterate over the print job
      for (TicketPrinterJobLine line : job.getLines()) {
        if (line instanceof TicketPrinterJobText) { // TODO could use polymorphism instead of If/ElseIf 
          printPort.writeBytes(SET_PRINT_MODE);
          printPort.writeBytes(DSTRIKE_MODE_ON);
          TicketPrinterJobText text = (TicketPrinterJobText) line;
          specifyTextPrintOptions(printPort, text);
          printPort.writeBytes(text.getText().getBytes());
          printPort.writeBytes(LINE_FEED);
        } else if (line instanceof TicketPrinterJobImage) {
          TicketPrinterJobImage image = (TicketPrinterJobImage) line;
          specifyAlign(printPort, image.getAlign());
          BufferedImage bi = (BufferedImage) image.getImage();
          int[][] pixels = getPixelsSlow(bi);
          printImage(pixels);
        }
      }
      // Finish print job with some spaces (line feeds) at the bottom.
      // TODO this could be fixed with some printer configuration.
      printPort.writeBytes(LINE_FEED);
      printPort.writeBytes(LINE_FEED);
      printPort.writeBytes(LINE_FEED);
      printPort.writeBytes(LINE_FEED);
      printPort.writeBytes(LINE_FEED);
      printPort.writeBytes(LINE_FEED);
      printPort.writeBytes(LINE_FEED);
      printPort.writeBytes(LINE_FEED);
      printPort.writeBytes(CUT_PAPER);
    } finally {
      if (printPort != null) {
        printPort.cleanup();
      }
    }
  }

  /**
   * Configures printer for the specified line (Bold, Italic, Size, etc.)
   * @param port printing port
   * @param line line to be printed in this iteration.
   */
  private void specifyTextPrintOptions(PrintPort port, TicketPrinterJobText line) {
    if (line.isBold()) {
      port.writeBytes(EMPHASIZED_MODE_ON);
    } else {
      port.writeBytes(EMPHASIZED_MODE_OFF);
    }
    byte size = (byte) ((line.getSize() - 12) * FONT_POINT); // TODO introduce constant for Default "font size" (12)
    port.writeBytes(new byte[]{GS, 0x21, size});

    specifyAlign(port, line.getAlign());
  }

  /**
   * Configures printer for the specified alignment.
   * @param port priting port.
   * @param align alignment to be used in this iteration.
   */
  private void specifyAlign(PrintPort port, LineAlignEnum align) {
    if (align == LineAlignEnum.LEFT) {
      port.writeBytes(ALIGN_LEFT);
    } else if (align == LineAlignEnum.CENTER) {
      port.writeBytes(ALIGN_CENTER);
    } else {
      port.writeBytes(ALIGN_RIGHT);
    }
  }

  /**
   * Send image to the printer to be printed.
   * 
   * @param pixels 2D Array of RGB colors (Row major order)
   */
  private void printImage(int[][] pixels) {
    printPort.writeBytes(SET_LINE_SPACE_24);
    for (int y = 0; y < pixels.length; y += 24) {
      printPort.writeBytes(SELECT_BIT_IMAGE_MODE);// bit mode
      printPort.writeBytes(new byte[]{(byte) (0x00ff & pixels[y].length), (byte) ((0xff00 & pixels[y].length) >> 8)});// width, low & high
      for (int x = 0; x < pixels[y].length; x++) {
        // For each vertical line/slice must collect 3 bytes (24 bytes)
        printPort.writeBytes(collectSlice(y, x, pixels));
      }

      printPort.writeBytes(PrinterCommands.FEED_LINE);
    }
    printPort.writeBytes(SET_LINE_SPACE_30);
  }

  /**
   * Gets the pixels stored in an image. 
   * TODO very slow, could be improved (use different class, cache result, etc.)
   * @param image image to get pixels from.
   * @return 2D array of pixels of the image (RGB, row major order)
   */
  private int[][] getPixelsSlow(BufferedImage image) {
    int width = image.getWidth();
    int height = image.getHeight();
    int[][] result = new int[height][width];
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        result[row][col] = image.getRGB(col, row);
      }
    }

    return result;
  }

  /**
   * Defines if a color should be printed (burned).
   * @param color RGB color.
   * @return true if should be printed/burned (black), false otherwise (white).
   */
  private boolean shouldPrintColor(int color) {
    final int threshold = 127;
    int a, r, g, b, luminance;
    a = (color >> 24) & 0xff;
    if (a != 0xff) { // ignore pixels with alpha channel
      return false;
    }
    r = (color >> 16) & 0xff;
    g = (color >> 8) & 0xff;
    b = color & 0xff;

    luminance = (int) (0.299 * r + 0.587 * g + 0.114 * b);

    return luminance < threshold;
  }

  /**
   * Collect a slice of 3 bytes with 24 dots for image printing.
   * @param y row position of the pixel.
   * @param x column position of the pixel.
   * @param img 2D array of pixels of the image (RGB, row major order).
   * @return 3 byte array with 24 dots (field set).
   */
  private byte[] collectSlice(int y, int x, int[][] img) {
    byte[] slices = new byte[]{0, 0, 0};
    for (int yy = y, i = 0; yy < y + 24 && i < 3; yy += 8, i++) {// va a hacer 3 ciclos
      byte slice = 0;
      for (int b = 0; b < 8; b++) {
        int yyy = yy + b;
        if (yyy >= img.length) {
          continue;
        }
        int col = img[yyy][x];
        boolean v = shouldPrintColor(col);
        slice |= (byte) ((v ? 1 : 0) << (7 - b));
      }
      slices[i] = slice;
    }

    return slices;
  }

  public String[] getPrinter() {
    return PRINTER;
  }

  /**
   * Print port abstraction.
   * Handle USB and Serial Ports the same way in the printing flow.
   */
  interface PrintPort {

    /**
     * Initialize port before executing print job.
     */
    void initialize();

    /**
     * Clean up port before executing print job.
     */
    void cleanup();

    /**
     * Write bytes to the print port. All printing commands boils down to bytes.
     *
     * @param bytes bytes to write.
     * @return number of bytes written.
     */
    int writeBytes(byte[] bytes);
  }

  /**
   * Serial Port implementation of {@link PrintPort}
   */
  static class SerialPrintPort implements PrintPort {

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
      Properties props = loadProperties();
      return props.getProperty("printer.com", DEFAULT_COM);
    }

    public void cleanup() {
      try {
        if (serialPort != null) {
          serialPort.closePort();
        }
      } catch (SerialPortException ex) {
        logger.error("Couldn't close serial port.", ex);
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

  /**
   * USB port implementation of {@link PrintPort}
   */
  static class USBJavaxPrintPort implements PrintPort {

    private static final int ID_VENDOR = 0x04b8, ID_PRODUCT = 0x0202;
    private static final byte INTERFACE_NUMBER = 0, ENDPOINT_NUMBER = 1;
    private int idVendor, idProduct;
    private byte interfaceNumber, endpointNumber;
    private UsbInterface iface;
    private UsbEndpoint endpoint;
    private UsbPipe pipe;

    public void initialize() {
      loadParameters();
      try {
        UsbServices services = UsbHostManager.getUsbServices();
        UsbDevice printer = null;
        for (UsbDevice child : (List<UsbDevice>) services.getRootUsbHub().getAttachedUsbDevices()) {
          UsbDeviceDescriptor descr = child.getUsbDeviceDescriptor();
          if (descr.idVendor() == idVendor && descr.idProduct() == idProduct) {
            printer = child;
          }
        }

        if (printer == null) {
          throw new IllegalStateException("Didn't find USB printer with"
                  + "idVendor: (" + idVendor
                  + ") and idProduct: (" + idProduct + ")");
        }
        UsbConfiguration configuration = printer.getActiveUsbConfiguration();

        iface = configuration.getUsbInterface(interfaceNumber);
        iface.claim();
        endpoint = iface.getUsbEndpoint(endpointNumber);
        pipe = endpoint.getUsbPipe();
        pipe.open();
      } catch (UsbException ex) {
        throw new IllegalStateException("Couldn't init usb port", ex);
      }
    }

    public void cleanup() {
      try {
        if (pipe != null) {
          pipe.close();
        }
      } catch (UsbException ex) {
        throw new IllegalStateException("Couldn't close usb port.", ex);
      }
      try {
        if (iface != null) {
          iface.release();
        }
      } catch (UsbClaimException ex) {
        throw new IllegalStateException("Couldn't close usb port.", ex);
      } catch (UsbException ex) {
        throw new IllegalStateException("Couldn't close usb port.", ex);
      }
    }

    public int writeBytes(byte[] bytes) {
      int numBytes = 0;
      try {
        numBytes = pipe.syncSubmit(bytes);
      } catch (Exception ex) {
        throw new IllegalStateException("Couldn't write bytes to usb port.", ex);
      }

      return numBytes;
    }

    private void loadParameters() {
      Properties props = loadProperties();
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

  public static Properties loadProperties() {
    Properties properties = new Properties();
    FileInputStream fi = null;
    InputStream is = null;
    try {
      is = EscPosStrategy.class.getResourceAsStream("/com/printer/printer-dispatcher.properties");
      properties.load(is);
    } catch (Exception ex) {
      logger.error(ex.getMessage(), ex);
    } finally {
      try {
        if (fi != null) {
          fi.close();
        }
        if (is != null) {
          is.close();
        }
      } catch (IOException ex) {
        logger.error(ex.getMessage(), ex);
      }
    }

    return properties;
  }
}
