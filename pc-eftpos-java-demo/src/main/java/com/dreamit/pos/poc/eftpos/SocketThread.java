package com.dreamit.pos.poc.eftpos; /**
 * Title:        PC-EFTPOS Java Demo for TCP/IP Interface
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      PC-EFTPOS
 * @author Clinton Dean
 * @version 1.0
 */
import java.net.*;

public class SocketThread extends Thread {

  private Socket socket;
  private SocketEventListener listener;

  public SocketThread(String inetAddr, int inetPort, SocketEventListener listener)
  {
    // Set the listener
    this.listener = listener;

    // Create a new socket
    try {
      socket = new Socket( inetAddr, inetPort );
      socket.setKeepAlive(true);
    }
    catch( Exception e ) {
      e.printStackTrace();
    }
  }

  public boolean socketSend( String s )
  {
    try {
      socket.getOutputStream().write( s.getBytes() );
      return true;
    }
    catch( Exception e ) {
      e.printStackTrace();
      return false;
    }
  }

  public void run()  {
    boolean Exit = false;

    while( !Exit ) {
      try  {

        int len = socket.getReceiveBufferSize();
        if( len != 0 )
        {
          byte[] bytes = new byte[len];
          socket.getInputStream().read(bytes);
          String s = new String(bytes);
          listener.socketReceive(s);
        }

      }
      catch( Exception e )  {
        e.printStackTrace();
        Exit = true;
      }
    }
    try {
      socket.close();
    }
    catch( Exception e ) {
      e.printStackTrace();
    }
  }
}