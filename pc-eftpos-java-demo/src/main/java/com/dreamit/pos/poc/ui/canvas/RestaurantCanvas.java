package com.dreamit.pos.poc.ui.canvas;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RestaurantCanvas extends JFrame {
    private JPanel controlPanel;
    private JPanel canvasPanel;
    private JButton addTableButton;

    public RestaurantCanvas(){

        canvasPanel = new CanvasPanel(canvasPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Restaurant Canvas");
        pack();
        setVisible(true);

        addTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public static void main(String s[]) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RestaurantCanvas();
            }
        });
    }
}
