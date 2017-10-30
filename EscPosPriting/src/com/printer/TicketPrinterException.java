package com.printer;

/**
 * Wrapping Exception for all exceptions thrown during printing.
 * @author nMoncho
 */
public class TicketPrinterException extends Exception {

  public TicketPrinterException(Throwable cause) {
    super(cause);
  }

  public TicketPrinterException(String message, Throwable cause) {
    super(message, cause);
  }

  public TicketPrinterException(String message) {
    super(message);
  }

  public TicketPrinterException() {
  }
}
