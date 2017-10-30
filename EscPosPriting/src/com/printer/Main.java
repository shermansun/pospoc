package com.printer;

public class Main {
  
  /**
   * Main entry point.
   * @param args 
   */
  public static void main(String[] args) throws TicketPrinterException {
    if (argsValid(args)) {
      EscPosStrategy printer;
      if ("usb".equals(args[0].toLowerCase())) {
        printer = EscPosStrategy.createUSBPortStrategy();
      } else {
        printer = EscPosStrategy.createSerialPortStrategy();
      }
      String[] lines = new String[args.length - 1];
      System.arraycopy(args, 1, lines, 0, lines.length);
      TicketPrinterJob job = new TicketPrinterJob(lines);
      System.out.println("Sending:");
      System.out.println(job);
      printer.print(job);
    }
  }
  
  private static boolean argsValid(String[] args) {
    if (args.length < 2) {
      System.err.println("USAGE: java -jar printer.jar [usb|serial] line1 line2 line3 ...");
      return false;
    }
    if (!"usb".equals(args[0].toLowerCase()) && !"serial".equals(args[0].toLowerCase())) {
      System.err.println("USAGE: java -jar printer.jar [usb|serial] line1 line2 line3 ...");
      System.err.println("Printer port must be \"usb\" or \"serial\"");
      return false;
    }
    
    return true;
  }
}
