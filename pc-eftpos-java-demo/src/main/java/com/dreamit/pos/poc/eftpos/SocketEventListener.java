package com.dreamit.pos.poc.eftpos;

/**
 * Title:        PC-EFTPOS Java Demo for TCP/IP Interface
 * Description:  Interface for receiving socket data
 * Copyright:    Copyright (c) 2002
 * Company:      PC-EFTPOS
 * @author Clinton Dean
 * @version 1.0
 */

public interface SocketEventListener {

public void socketReceive( String s );
}