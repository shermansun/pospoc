package com.dreamit.pos.poc.printer;

import com.dreamit.pos.poc.printer.port.SerialPrinterPort;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by ssun on 21/11/17.
 */
public class PrintChinese {

	public static void main(String argv[]) throws Exception {

		ResourceBundle rb = ResourceBundle.getBundle("printword", Locale.CHINESE);
		System.out.println(rb.getKeys().nextElement());
		String printline = rb.getObject("printline").toString();
		System.out.println(printline);

		boolean doPrint = false;
		if (doPrint){
			SerialPrinterPort serialPrinterPort = new SerialPrinterPort();
			serialPrinterPort.initialize(argv[0]);

			String str = "\n\n你好嗎? 1234567890"; // start str on new line
			String encoding = "GBK";
			DataInputStream in = null;
			try {

				//DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				in = new DataInputStream(new ByteArrayInputStream(str.getBytes(encoding)));

				serialPrinterPort.writeBytes(new byte[]{0x1C});
				serialPrinterPort.writeString("&");

				while (in.available() != 0) {
					serialPrinterPort.writeByte(in.readByte());
				}

				serialPrinterPort.writeBytes(new byte[]{0x1C});
				serialPrinterPort.writeString(".");
				serialPrinterPort.writeBytes(new byte[]{0x00}); // terminate line else str may be cut off and appear in next printout

				serialPrinterPort.cleanup();


			} catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				in.close();
			}
		}
	}

}
