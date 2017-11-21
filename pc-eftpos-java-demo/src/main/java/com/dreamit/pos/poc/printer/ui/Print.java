package com.dreamit.pos.poc.printer.ui;

import com.dreamit.pos.poc.printer.constants.LineAlignment;
import com.dreamit.pos.poc.printer.print.EcsPosPrint;
import com.dreamit.pos.poc.printer.print.job.EcsPrintJob;
import com.dreamit.pos.poc.printer.print.job.EcsPrintLine;
import com.dreamit.pos.poc.printer.print.job.EcsPrintTextLine;
import org.apache.commons.lang.StringUtils;

import javax.print.*;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterName;
import java.util.Arrays;
import java.util.List;

public class Print {


    public static void main(String... arg){

        processTextInput(arg[0],"a-CENTER,bold:Testing ABC\nhave a look at this text");



    }

    private static boolean feedPrinter(byte[] b)
    {
        try
        {

            AttributeSet attrSet = new HashPrintServiceAttributeSet(new PrinterName("PRINTERNAME", null));
            DocPrintJob job = PrintServiceLookup.lookupPrintServices(null, attrSet)[0].createPrintJob();
            DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
            Doc doc = new SimpleDoc(b, flavor, null);
            //PrintJobWatcher pjDone = new PrintJobWatcher(job);

            job.print(doc, null);
            //pjDone.waitForDone();
            System.out.println("Done !");
        }
        catch(javax.print.PrintException pex)
        {

            System.out.println("Printer Error " + pex.getMessage());
            return false;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static void processTextInput(String portName, String textInput){

        System.out.println("Printing at " + portName);

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
        ecsPosPrint.print(portName, ecsPrintJob);
    }

    private static EcsPrintLine createPrintLine(String text){

        if (StringUtils.contains(text, "<blank>")){
            return new EcsPrintTextLine("");
        }
        else if (StringUtils.contains(text, "<image>")){

            return null;
        }
        else{
            return new EcsPrintTextLine(text);
        }

    }
}
