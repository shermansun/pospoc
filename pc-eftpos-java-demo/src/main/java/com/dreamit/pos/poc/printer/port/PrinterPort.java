package com.dreamit.pos.poc.printer.port;

/**
 * Created by ssun on 29/10/17.
 */
public interface PrinterPort {

	/**
	 * Initialize port before executing print job.
	 */
	void initialize();

	void initialize(String portName);

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
