package com.dreamit.pos.poc.printer.ui;

import com.dreamit.pos.poc.printer.constants.LineAlignment;
import com.dreamit.pos.poc.printer.print.EcsPosPrint;
import com.dreamit.pos.poc.printer.print.job.EcsPrintImageLine;
import com.dreamit.pos.poc.printer.print.job.EcsPrintJob;
import com.dreamit.pos.poc.printer.print.job.EcsPrintLine;
import com.dreamit.pos.poc.printer.print.job.EcsPrintTextLine;
import com.dreamit.pos.poc.printer.ui.listener.FileDnDListener;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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
    private File image;

    public PrintGui() {
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(textInput.getText());
                processTextInput(textInput.getText());
            }
        });
    }

    private EcsPrintLine createPrintLine(String text){

        if (StringUtils.contains(text, "<blank>")){
            return new EcsPrintTextLine("");
        }
        else if (StringUtils.contains(text, "<image>")){

            try {
                return new EcsPrintImageLine(image);
            } catch (IOException e) {

                e.printStackTrace();
                return new EcsPrintTextLine("<can't print image>");
            }
        }
        else{
            return new EcsPrintTextLine(text);
        }

    }

    private void processTextInput(String textInput){

        EcsPrintJob ecsPrintJob = new EcsPrintJob();
        String [] txtLines = textInput.split("\n");
        for (String txtLine : txtLines){
            if (StringUtils.isNotBlank(txtLine)){

                String [] txts = txtLine.split(":");
                if (txts.length == 1){

                    ecsPrintJob.addLine(
                            createPrintLine(txtLine));
                }
                else{
                    String command = txts[0];
                    String txt = txts[1];

                    EcsPrintLine printLine = createPrintLine(txt);

                    if (printLine instanceof EcsPrintTextLine){

                        EcsPrintTextLine textLine = (EcsPrintTextLine) printLine;
                        List<String> commands = Arrays.asList(command.split(","));

                        commands.stream().filter(c -> c.contains("f-")).forEach(c ->
                                textLine.setFont(c.split("-")[1]));

                        commands.stream().filter(c -> c.contains("s-")).forEach(c ->
                                textLine.setSize(Integer.parseInt(c.split("-")[1])));

                        commands.stream().filter(c -> c.contains("a-")).forEach(c ->
                                textLine.setAlign(LineAlignment.valueOf(c.split("-")[1])));

                        commands.stream().filter(c -> c.contains("bold")).forEach(c ->
                                textLine.setBold(true));

                        commands.stream().filter(c -> c.contains("italic")).forEach(c ->
                                textLine.setItalic(true));
                    }


                    ecsPrintJob.addLine(printLine);
                }

            }
        }

        EcsPosPrint ecsPosPrint = new EcsPosPrint();
        ecsPosPrint.print("COM1", ecsPrintJob);
    }


    public static void main(String... args){

        JFrame frame = new JFrame("Ecs Print POC");
        PrintGui printGui = new PrintGui();
        frame.setContentPane(printGui.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        new DropTarget(frame, new FileDnDListener(printGui.imageLabel, printGui.pathLabel, printGui));
    }

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }
}
