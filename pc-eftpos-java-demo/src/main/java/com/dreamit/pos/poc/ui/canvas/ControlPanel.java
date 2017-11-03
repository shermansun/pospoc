package com.dreamit.pos.poc.ui.canvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlPanel extends JPanel {

    private GridBagConstraints gridBagConstraints;
    GridLayout layout = new GridLayout(4, 12);

    public ControlPanel(JPanel parent, CanvasPanel canvasPanel){


        setLayout(layout);

        JButton addRectTable = new JButton();
        addRectTable.setText("Add Rectangular Table");
        add(addRectTable);
        addRectTable.addActionListener(e -> {
            canvasPanel.addTable(TableShape.RECTANGLE, Color.BLUE);
        });

        JButton addRoundTable = new JButton();
        addRoundTable.setText("Add Round Table");
        add(addRoundTable);
        addRoundTable.addActionListener(e -> {
            canvasPanel.addTable(TableShape.ROUND, Color.BLUE);
        });

        for (int i = 0; i < 46; i++){
            JButton spaceButton = new JButton();
            add(spaceButton);
        }

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = gridBagConstraints.gridheight = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = gridBagConstraints.weighty = 20;
        parent.add(this, gridBagConstraints);
    }


}
