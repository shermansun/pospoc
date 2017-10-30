package com.dreamit.pos.poc.eftpos;

/**
 * Title:        PC-EFTPOS Java Demo for TCP/IP Interface
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      PC-EFTPOS
 * @author Clinton Dean
 * @version 1.0
 */

public interface PCEFTPOSEventListener {

public void onLogonEvent(PCEFTPOSMsg msg);
public void onReceiptEvent(PCEFTPOSMsg msg);
public void onTransactionEvent(PCEFTPOSMsg msg);
}