package com.dreamit.pos.poc.printer.print;

/**
 * Created by ssun on 30/10/17.
 */
public class EcsPrintContants {

	public final static char ESC_CHAR = 0x1B;
	public final static char GS = 0x1D;
	public final static byte[] LINE_FEED = new byte[]{0x0A};
	public final static byte[] CUT_PAPER = new byte[]{GS, 0x56, 0x00};
	public final static byte[] INIT_PRINTER = new byte[]{ESC_CHAR, 0x40};
	public final static byte[] SET_PRINT_MODE = new byte[]{ESC_CHAR, 0x21, 0x00};
	public final static byte[] UNDERLINED_MODE = new byte[]{ESC_CHAR, 0x2D, 0x32};
	public final static byte[] EMPHASIZED_MODE_ON = new byte[]{ESC_CHAR, 0x45, 0x01};
	public final static byte[] EMPHASIZED_MODE_OFF = new byte[]{ESC_CHAR, 0x45, 0x00};
	public final static byte[] DSTRIKE_MODE_ON = new byte[]{ESC_CHAR, 0x47, 0x01};
	public final static byte[] DSTRIKE_MODE_OFF = new byte[]{ESC_CHAR, 0x47, 0x00};
	public final static byte[] ALIGN_LEFT = new byte[]{ESC_CHAR, 0x61, 0x30};
	public final static byte[] ALIGN_CENTER = new byte[]{ESC_CHAR, 0x61, 0x31};
	public final static byte[] ALIGN_RIGHT = new byte[]{ESC_CHAR, 0x61, 0x32};
	public final static byte[] UPSIDE_ON = new byte[]{ESC_CHAR, 0x7B, 0x01};
	public final static byte[] UPSIDE_OFF = new byte[]{ESC_CHAR, 0x7B, 0x00};
	public final static byte[] SELECT_BIT_IMAGE_MODE = {0x1B, 0x2A, 33};
	public final static byte[] SET_LINE_SPACE_24 = new byte[]{ESC_CHAR, 0x33, 24};
	public final static byte[] SET_LINE_SPACE_30 = new byte[]{ESC_CHAR, 0x33, 30};
	public final static byte FONT_POINT = 0x11;
	public final static String DEFAULT_TYPE = "SERIAL", SERIAL_TYPE = "SERIAL", USB_TYPE = "USB";
	public final static String[] PRINTER = {"ESC/POS Epson Printers"};


}
