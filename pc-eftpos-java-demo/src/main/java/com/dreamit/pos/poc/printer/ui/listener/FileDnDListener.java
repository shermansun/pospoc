package com.dreamit.pos.poc.printer.ui.listener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileDnDListener implements DropTargetListener {

    JLabel imageLabel = new JLabel();
    JLabel pathLabel = new JLabel();

    public FileDnDListener(JLabel imageLabel, JLabel pathLabel){
        this.imageLabel = imageLabel;
        this.pathLabel = pathLabel;
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {

    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {

    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {

    }

    @Override
    public void dragExit(DropTargetEvent dte) {

    }

    @Override
    public void drop(DropTargetDropEvent event) {

        System.out.println("Event received.");
        event.acceptDrop(DnDConstants.ACTION_COPY);

        Transferable t = event.getTransferable();
        for (DataFlavor df : t.getTransferDataFlavors()){

            if (df.isFlavorJavaFileListType()){
                try {
                    List<File> files = (List<File>) t.getTransferData(df);

                    //files.forEach(f -> displayImage(f));

                    for (File f : files){
                        displayImage(f);
                    }


                } catch (UnsupportedFlavorException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    private void displayImage(File file) throws IOException {
        BufferedImage img = ImageIO.read(file);

        ImageIcon icon = new ImageIcon(img);
        imageLabel.setIcon(icon);
        pathLabel.setText(file.getPath());

    }
}
