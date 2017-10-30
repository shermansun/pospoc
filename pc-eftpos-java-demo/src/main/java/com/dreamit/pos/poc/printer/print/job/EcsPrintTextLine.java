package com.dreamit.pos.poc.printer.print.job;

import com.dreamit.pos.poc.printer.constants.EcsPrintContants;
import com.dreamit.pos.poc.printer.constants.LineAlignment;
import com.dreamit.pos.poc.printer.port.PrinterPort;
import org.w3c.dom.Element;

/**
 * Created by sherman_sun on 30/10/17.
 */
public class EcsPrintTextLine extends EcsPrintLine {

	public static final String LINEA_TAG_NAME = "Linea";

	public static final String ATTR_TEXTO = "texto";
	public static final String ATTR_FUENTE = "fuente";
	public static final String ATTR_SIZE = "size";
	public static final String ATTR_BOLD = "bold";
	public static final String ATTR_ITALIC = "italic";

	public static final String TRUE_VALUE = "Y";
	public static final String FALSE_VALUE = "N";

	private String text;
	private String font;
	private int size;
	private boolean bold;
	private boolean italic;

	public EcsPrintTextLine(String text, String font, int size
			, boolean bold, boolean italic, LineAlignment align) {
		this.text = text;
		this.font = font;
		this.size = size;
		this.bold = bold;
		this.italic = italic;
		this.align = align;
	}

	public EcsPrintTextLine(String text){

		this(text, "Arial", 12, false, false, LineAlignment.LEFT);
	}

	public void print(PrinterPort printerPort){

		printerPort.writeBytes(EcsPrintContants.SET_PRINT_MODE);
		printerPort.writeBytes(EcsPrintContants.DSTRIKE_MODE_ON);
		specifyTextPrintOptions(printerPort);
		printerPort.writeBytes(text.getBytes());
		printerPort.writeBytes(EcsPrintContants.LINE_FEED);
	}

	private void specifyTextPrintOptions(PrinterPort printerPort){

		if (this.bold){
			printerPort.writeBytes(EcsPrintContants.EMPHASIZED_MODE_ON);
		}
		else{
			printerPort.writeBytes(EcsPrintContants.EMPHASIZED_MODE_OFF);
		}

		byte size = (byte) ((this.size - 12) * EcsPrintContants.FONT_POINT); // TODO introduce constant for Default "font size" (12)
		printerPort.writeBytes(new byte[]{EcsPrintContants.GS, 0x21, size});

		specifyAlign(printerPort, this.align);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getFont() {
		return font;
	}

	public void setFont(String font) {
		this.font = font;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public boolean isBold() {
		return bold;
	}

	public void setBold(boolean bold) {
		this.bold = bold;
	}

	public boolean isItalic() {
		return italic;
	}

	public void setItalic(boolean italic) {
		this.italic = italic;
	}
}
