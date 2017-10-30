package com.dreamit.pos.poc.printer.print.job;

import com.dreamit.pos.poc.printer.constants.EcsPrintContants;
import com.dreamit.pos.poc.printer.constants.LineAlignment;
import com.dreamit.pos.poc.printer.port.PrinterPort;
import org.w3c.dom.Element;

import java.io.Serializable;

/**
 * Created by ssun on 30/10/17.
 */
public abstract class EcsPrintLine implements Serializable{

	protected LineAlignment align = LineAlignment.LEFT;

	public LineAlignment getAlign() {
		return align;
	}

	public void setAlign(LineAlignment align) {
		this.align = align;
	}

	protected void specifyAlign(PrinterPort printerPort, LineAlignment align) {
		if (align == LineAlignment.LEFT) {
			printerPort.writeBytes(EcsPrintContants.ALIGN_LEFT);
		} else if (align == LineAlignment.CENTER) {
			printerPort.writeBytes(EcsPrintContants.ALIGN_CENTER);
		} else {
			printerPort.writeBytes(EcsPrintContants.ALIGN_RIGHT);
		}
	}

	public abstract void print(PrinterPort printerPort);
}
