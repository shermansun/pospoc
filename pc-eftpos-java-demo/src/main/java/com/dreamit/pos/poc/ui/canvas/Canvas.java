package com.dreamit.pos.poc.ui.canvas;

import javax.swing.*;
import java.awt.*;

public class Canvas {

    public Canvas() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Shape Mover");

        initComponents(frame);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String s[]) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Canvas();
            }
        });

    }

    private void initComponents(JFrame frame) {
        JPanel mainPanel = new JPanel();
        frame.getContentPane().add(mainPanel);
        GridLayout grid = new GridLayout(2, 1);
        //mainPanel.setLayout(grid);
        mainPanel.setLayout(new GridBagLayout());

        CanvasPanel canvasPanel = new CanvasPanel(mainPanel);
        ControlPanel controlPanel = new ControlPanel(mainPanel, canvasPanel);
        //mainPanel.add(canvasPanel);
        //mainPanel.add(controlPanel);




        //frame.getContentPane().add(new CanvasPanel());
        //frame.getContentPane().add(new ControlPanel());
        //frame.getContentPane().add(new ControlPanel());

    }
}