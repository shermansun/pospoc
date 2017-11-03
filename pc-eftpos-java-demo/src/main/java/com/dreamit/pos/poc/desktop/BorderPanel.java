package com.dreamit.pos.poc.desktop;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class BorderPanel extends JFrame {

    private static final long serialVersionUID = 1L;

    public BorderPanel() {
        setLayout(new GridBagLayout());// set LayoutManager
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel panel1 = new JPanel();
        Border eBorder = BorderFactory.createEtchedBorder();

        panel1.setBorder(BorderFactory.createTitledBorder(eBorder, "80pct"));
        gbc.gridx = gbc.gridy = 0;
        gbc.gridwidth = gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = gbc.weighty = 80;
        add(panel1, gbc); // add component to the ContentPane

        JPanel panel2 = new JPanel();
        panel2.setBorder(BorderFactory.createTitledBorder(eBorder, "20pct"));
        gbc.gridy = 1;
        gbc.weightx = gbc.weighty = 20;
        gbc.insets = new Insets(2, 2, 2, 2);
        add(panel2, gbc); // add component to the ContentPane

        JPanel panel3 = new JPanel();
        panel3.setBorder(BorderFactory.createTitledBorder(eBorder, "20pct"));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 2;
        gbc.weightx = /*gbc.weighty = */ 20;
        gbc.insets = new Insets(2, 2, 2, 2);
        add(panel3, gbc); // add component to the ContentPane

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // important
        pack();
        setVisible(true); // important
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() { // important

            @Override
            public void run() {
                BorderPanel borderPanels = new BorderPanel();
            }
        });
    }
}
