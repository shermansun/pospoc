package com.dreamit.pos.poc.printer.print;

import com.dreamit.pos.poc.printer.constants.EcsPrintContants;
import com.dreamit.pos.poc.printer.port.PrinterPort;
import com.dreamit.pos.poc.printer.port.SerialPrinterPort;
import com.dreamit.pos.poc.printer.print.job.EcsPrintJob;
import com.dreamit.pos.poc.printer.print.job.EcsPrintLine;

/**
 * Created by ssun on 30/10/17.
 */
public class EcsPosPrint {

	private PrinterPort printerPort = new SerialPrinterPort();

	public void print(String portName, EcsPrintJob printJob){

		try{
			printerPort.initialize(portName);
			printerPort.writeBytes(EcsPrintContants.INIT_PRINTER);
			printerPort.writeBytes(EcsPrintContants.CHINESE_MODE);

			for (EcsPrintLine printLine : printJob.getPrintLines()){

				printLine.print(printerPort);
			}

			printerPort.writeBytes(EcsPrintContants.CANCEL_CHINESE_MODE);
			printerPort.writeBytes(EcsPrintContants.LINE_FEED);
			printerPort.writeBytes(EcsPrintContants.LINE_FEED);
			printerPort.writeBytes(EcsPrintContants.LINE_FEED);
			printerPort.writeBytes(EcsPrintContants.LINE_FEED);
			printerPort.writeBytes(EcsPrintContants.LINE_FEED);
			printerPort.writeBytes(EcsPrintContants.LINE_FEED);
			printerPort.writeBytes(EcsPrintContants.LINE_FEED);
			printerPort.writeBytes(EcsPrintContants.LINE_FEED);
			printerPort.writeBytes(EcsPrintContants.LINE_FEED);

		}
		finally {

			printerPort.cleanup();
		}
	}


}
