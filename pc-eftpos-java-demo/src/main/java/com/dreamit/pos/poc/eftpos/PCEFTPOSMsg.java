package com.dreamit.pos.poc.eftpos;

/**
 * Title:        PC-EFTPOS Java Demo for TCP/IP Interface
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      PC-EFTPOS
 * @author Clinton Dean
 * @version 1.0
 */

public class PCEFTPOSMsg {

  // Common
  public int msgLen;
  public String commandCode;
  public String subCode;
  public boolean successFlag;
  public String responseCode;
  public String responseText;
  public String terminalID;
  public String merchantID;
  public String bankDate;
  public String bankTime;
  public String stan;
  public String pinpadVersion;

  // Receipt
  public String receiptData;

  // Transaction
  public String merchant;
  public String txnType;
  public String accountType;
  public String amtCash;
  public String amtPurchase;
  public String amtTip;
  public String authCode;
  public String txnRef;
  public String expiryDate;
  public String settlementDate;
  public String date;
  public String time;
  public String cardType;
  public String pan;
  public String track2;


  public PCEFTPOSMsg() {
    this("");
  }

  public PCEFTPOSMsg(String s) {

    // Init Variables
    commandCode = "";
    subCode = "";
    responseCode = "";
    responseText = "";
    terminalID = "";
    merchantID = "";
    bankDate = "";
    bankTime = "";
    stan = "";
    pinpadVersion = "";

    merchant = "";
    txnType = "";
    accountType = "";
    amtCash = "";
    amtPurchase = "";
    amtTip = "";
    authCode = "";
    txnRef = "";
    stan = "";
    merchant = "";
    terminalID = "";
    expiryDate = "";
    settlementDate = "";
    date = "";
    time = "";
    cardType = "";
    pan = "";
    track2 = "";

    if( !s.equals("") )
      parseMsgStr(s);
  }

  public String buildLogonRequest()
  {
    // This should be built using properties
    String s;
    s = "#0011G00000";
    return s;
  }


  public String buildTransactionRequest()
  {
    // This should be built using properties
    String s;
    s = "#0119M000P00000000000000000100000001000000000000000100 4507880121656412    0505                                        ";
    return s;
  }



  public String buildReceiptResponse()
  {
    // This should be built using properties
    String s;
    s = "#00073 ";
    return s;
  }


  public void parseMsgStr(String s)
  {
    // Do validity checking on the string
    //...

    // String may be multiple messages i.e "#0099...#0007L "
    // ...

    // Check what the string is
    if (s.length() >=5)
    {
      switch (s.charAt(5))
      {
        case 'G':  parseLogon(s);
          break;
        case 'S': parseDisplay(s);
          break;
        case '3': parseReceipt(s);
          break;
        case 'M': parseTransaction(s);
          break;
        default:/*Received unknown message from the client*/;
      }
    }
  }

  public void parseLogon( String s )
  {
    try {
      msgLen = Integer.parseInt(s.substring(1, 5));
    }
    catch( Exception e ) {
      e.printStackTrace();
      msgLen = 0;
    }

    commandCode = s.substring(5, 6);
    subCode = s.substring(6, 7);
    successFlag = (s.substring(7, 8).equals("1"));
    responseCode = s.substring(8, 10);
    responseText = s.substring(10, 30);
    terminalID = s.substring(30, 38);
    merchantID = s.substring(38, 53);
    bankDate = s.substring(53, 59);
    bankTime = s.substring(59, 65);
    stan = s.substring(65, 71);
    pinpadVersion = s.substring(71, 87);
  }

  public void parseDisplay( String s )
  {
    //
  }

  public void parseReceipt( String s )
  {
    try {
      msgLen = Integer.parseInt(s.substring(1, 5));
    }
    catch( Exception e ) {
      e.printStackTrace();
      msgLen = 0;
    }
    commandCode = s.substring(5, 6);
    subCode = s.substring(6, 7);

    // Parse the receipt data
    if( subCode.equals("R")  ) {
      receiptData = s.substring(7, msgLen - 7);
      //receiptData.replaceAll("\r\n", "\n");
    }
  }

  public void parseTransaction( String s )
  {
    try {
      msgLen = Integer.parseInt(s.substring(1, 5));
    }
    catch( Exception e ) {
      e.printStackTrace();
      msgLen = 0;
    }
    commandCode = s.substring(5, 6);
    subCode = s.substring(6, 7);
    successFlag = (s.substring(7, 8).equals("1"));
    responseCode = s.substring(8, 10);
    responseText = s.substring(10, 30);
    merchant = s.substring(30, 32);
    txnType = s.substring(32, 33);
    accountType = s.substring(33, 40);
    amtCash = s.substring(40, 49);
    amtPurchase = s.substring(49, 58);
    amtTip = s.substring(58, 67);
    authCode = s.substring(67, 73);
    txnRef = s.substring(73, 89);
    stan = s.substring(89, 95);
    merchantID = s.substring(95, 110);
    terminalID = s.substring(110, 118);
    expiryDate = s.substring(118, 122);
    settlementDate = s.substring(122, 126);
    date = s.substring(126, 132);
    time = s.substring(132, 138);
    cardType = s.substring(138, 158);
    pan = s.substring(158, 178);
    track2 = s.substring(178, 218);
  }
}






































