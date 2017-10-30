package com.dreamit.pos.poc.eftpos;

/**
 * Title:        PC-EFTPOS Java Demo for TCP/IP Interface
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      PC-EFTPOS
 * @author Clinton Dean
 * @version 1.0
 */

public class PCEFTPOSControl implements SocketEventListener{

  SocketThread sckThread;
  PCEFTPOSEventListener listener;

  public PCEFTPOSControl(PCEFTPOSEventListener listener)
  {
    // Set the listener
    this.listener = listener;

    // Create a new socket thread
    sckThread = new SocketThread("127.0.0.1", 52076, this);
    //sckThread = new com.dreamit.pos.poc.eftpos.SocketThread("10.9.28.41", 6000, this);
    sckThread.start();
  }

  public void socketSend( String s )
  {
    sckThread.socketSend(s);
  }

  public void socketReceive( String s )
  {
    // Create a new message from the socket string
    PCEFTPOSMsg msg = new PCEFTPOSMsg(s);

    // cmd code should probably be mapped to a numeber
    if( msg.commandCode.equals("G") )
      listener.onLogonEvent(msg);
    else if ( msg.commandCode.equals("3") )
      listener.onReceiptEvent(msg);
    else if ( msg.commandCode.equals("M") )
      listener.onTransactionEvent(msg);
  }
}