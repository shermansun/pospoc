package com.dreamit.pos.poc.printer.ui;

import com.dreamit.pos.poc.printer.print.job.EcsPrintImageLine;
import com.dreamit.pos.poc.printer.print.job.EcsPrintJob;
import com.dreamit.pos.poc.printer.print.job.EcsPrintTextLine;
import com.dreamit.pos.poc.printer.ui.listener.FileDnDListener;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by sherman_sun on 30/10/17.
 */
public class PrintGui {
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel mainPanel;
    private JTextPane textInput;
    private JTable outputTable;
    private JButton printButton;
    private JPanel fileDnDPanel;
    private JLabel imageLabel;
    private JLabel pathLabel;

    public PrintGui() {
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(textInput.getText());
                processTextInput(textInput.getText());
            }
        });
    }

    private void processTextInput(String textInput){

        EcsPrintJob ecsPrintJob = new EcsPrintJob();
        String [] txtLines = textInput.split("\n");
        for (String txtLine : txtLines){
            if (StringUtils.isNotBlank(txtLine)){

                String [] txts = txtLine.split(":");
                if (txts.length == 1){
                    ecsPrintJob.addLine(
                            new EcsPrintTextLine(txtLine));
                }
                else{
                    String command = txts[0];
                    String txt = txts[1];

                }


            }
        }



    }


    public static void main(String... args){

        JFrame frame = new JFrame("Ecs Print POC");
        PrintGui printGui = new PrintGui();
        frame.setContentPane(printGui.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        new DropTarget(frame, new FileDnDListener(printGui.imageLabel, printGui.pathLabel));
    }
}
