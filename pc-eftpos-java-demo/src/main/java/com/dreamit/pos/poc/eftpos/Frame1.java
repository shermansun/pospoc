package com.dreamit.pos.poc.eftpos;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Title:        PC-EFTPOS Java Demo for TCP/IP Interface
 * Description:  Test application for the com.dreamit.pos.poc.eftpos.PCEFTPOSControl.
 *               Implements the com.dreamit.pos.poc.eftpos.PCEFTPOSEventListener interface
 * Copyright:    Copyright (c) 2002
 * Company:      PC-EFTPOS
 * @author Clinton Dean
 * @version 1.0
 */

public class Frame1 extends JFrame implements PCEFTPOSEventListener {
  JPanel contentPane;

  PCEFTPOSControl ctrl;
  TextArea textArea1 = new TextArea();
  Button btnDoLogon = new Button();
  Button btnDoTransaction = new Button();
  GridBagLayout gridBagLayout1 = new GridBagLayout();

  public static void main( String[] args )
  {
    Frame1 frm = new Frame1();
    frm.setVisible(true);
  }

  /**Construct the frame*/
  public Frame1() {

    ctrl = new PCEFTPOSControl(this);

    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  /**Component initialization*/
  private void jbInit() throws Exception  {
    //setIconImage(Toolkit.getDefaultToolkit().createImage(com.dreamit.pos.poc.eftpos.Frame1.class.getResource("[Your Icon]")));
    contentPane = (JPanel) this.getContentPane();
    contentPane.setLayout(gridBagLayout1);
    this.setSize(new Dimension(640, 480));
    this.setTitle("PC-EFTPOS Java Demo");
    btnDoLogon.setLabel("Do Logon");
    btnDoLogon.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        btnDoLogon_actionPerformed(e);
      }
    });
    contentPane.setMinimumSize(new Dimension(320, 240));
    contentPane.setPreferredSize(new Dimension(320, 240));
    btnDoTransaction.setLabel("Do $1.00 Transaction");
    btnDoTransaction.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        btnDoTransaction_actionPerformed(e);
      }
    });
    textArea1.setFont(new Font("DialogInput", 1, 12));
    contentPane.add(btnDoLogon,     new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 551, 5));
    contentPane.add(btnDoTransaction,     new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 565, 5));
    contentPane.add(textArea1,        new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 620, 135));
  }
  /**Overridden so we can exit when window is closed*/
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      System.exit(0);
    }
  }








  //////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////
  // IMPLEMENTATION OF PCEFTPOSListener INTERFACE
  //////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////
  public void onLogonEvent(PCEFTPOSMsg msg)
  {
    textArea1.append("Logon Event Received");
    textArea1.append("Logon Event Received");
    textArea1.append("Logon Event Received");


    // Could use all the properties of msg here
  }

  public void onReceiptEvent(PCEFTPOSMsg msg)
  {
    //
    textArea1.append("\n");
    textArea1.append("Receipt Event Received\n");
    textArea1.append("ReceiptType=" + msg.subCode +'\n');
    if( msg.subCode.equals("R") )
      textArea1.append("ReceiptData=" + msg.receiptData+"\n");

    // Must respond to all print events
    ctrl.socketSend( msg.buildReceiptResponse() );
  }

  public void onTransactionEvent(PCEFTPOSMsg msg)
  {
    textArea1.append("\n");
    textArea1.append("Transaction Event Received\n\n");
    // Could use all the properties of msg here
  }




  //////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////
  // BUTTON ONCLICK EVENT HANDLERS
  //////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////
  void btnDoLogon_actionPerformed(ActionEvent e) {
    PCEFTPOSMsg msg = new PCEFTPOSMsg();
    // Should fill in the properties of msg here
    ctrl.socketSend( msg.buildLogonRequest() );
  }

  void btnDoTransaction_actionPerformed(ActionEvent e) {
    PCEFTPOSMsg msg = new PCEFTPOSMsg();
    // Should fill in the properties of msg here
    ctrl.socketSend( msg.buildTransactionRequest() );
  }


}
