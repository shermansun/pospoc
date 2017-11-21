package com.dreamit.pos.poc.ui.canvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class CanvasPanel extends JPanel {

    private GridBagConstraints gridBagConstraints;

    int selectedTableId = -1;
    List<CanvasShape> shapes = new ArrayList<>();

    boolean isFirstTime = true;
    Rectangle area;

    private Dimension dim = new Dimension(1200, 800);

    public CanvasPanel(JPanel parent) {
        setBackground(Color.white);
        addMouseMotionListener(new CanvasPanel.MyMouseAdapter());
        addMouseListener(new CanvasPanel.MyMouseAdapter());

        //.
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = gridBagConstraints.gridheight = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        gridBagConstraints.weightx = gridBagConstraints.weighty = 80;

        parent.add(this, gridBagConstraints);

        //add(panel1, gbc); // add component to the ContentPane

    }

    private static int tableId = 1;


    public void addTable(TableShape tableShape, Color color){


        CanvasShape shape = null;
        switch (tableShape){
            case ROUND:
                shape = new RoundTable(tableId, 100, 100, 100, 100, Color.BLUE);
                break;
            case RECTANGLE:
                shape = new RectangularTable(tableId, 200, 200, 100, 100, Color.MAGENTA);
                break;
        }
        System.out.println(tableId);
        tableId++;

        shapes.add(shape);
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return dim;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        if (isFirstTime) {
            area = new Rectangle(dim);
            isFirstTime = false;
            addShape();
        }

        shapes.stream().forEach(s -> {
            g2d.setColor(s.getColor());
            g2d.fill(s);

            if (selectedTableId == s.id){
                g2d.setColor(Color.BLACK);
                g2d.draw(s);
            }
            //g2d.drawRect();
            int offset1 = 3;
            int offset2 = 8;
            int offset3 = 13;
            int offset4 = 18;
            int offsetY = 3;
            g2d.setColor(Color.WHITE);
            g2d.drawString(((Integer) s.id).toString() + "11A", (s.getBounds().x + s.getBounds().width/2) - offset4, (s.getBounds().y+s.getBounds().height/2)+offsetY);
            //g2d.draw(s);


        });

        //g2d.drawOval(100, 100, 50, 50);

    }

    private void addShape(){

        //RectangularTable rect1 = new RectangularTable(1, 200, 200, 50, 50, Color.CYAN);
        //RectangularTable rect2 = new RectangularTable(2, 0, 0, 50, 50, Color.GREEN);
        //RoundTable round = new RoundTable(3, 100, 100, 50, 50, Color.BLUE);


        //Ellipse2D circle = new Ellipse2D.Double(100, 100, 50, 50);

        //shapes.add(rect1);
        //shapes.add(rect2);
        //shapes.add(round);
        //shapes.add(circle);

    }

    boolean checkRect(Rectangle rect) {
        if (area == null) {
            return false;
        }

        if (area.contains(rect.x, rect.y, rect.getWidth(), rect.getHeight())) {
            return true;
        }

        int new_x = rect.x;
        int new_y = rect.y;

        if ((rect.x + rect.getWidth()) > area.getWidth()) {
            new_x = (int) area.getWidth() - (int) (rect.getWidth() - 1);
        }
        if (rect.x < 0) {
            new_x = -1;
        }
        if ((rect.y + rect.getHeight()) > area.getHeight()) {
            new_y = (int) area.getHeight() - (int) (rect.getHeight() - 1);
        }
        if (rect.y < 0) {
            new_y = -1;
        }
        rect.setLocation(new_x, new_y);
        return false;
    }

    private class MyMouseAdapter extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {

            selectedTableId = -1;
            shapes.stream().forEach( shape -> {

                if (shape.contains(e.getX(), e.getY())) {
                    System.out.println("click on: " + shape.getId());

                    shape.setPreX(shape.getBounds().x - e.getX());
                    shape.setPreY(shape.getBounds().y - e.getY());
                    updateLocation(shape, e);
                    //shape.setDragging(true);
                    selectedTableId = shape.getId();
                }
            });
            repaint();


        }

        @Override
        public void mouseDragged(MouseEvent e) {

            shapes.stream().forEach( shape -> {
                System.out.println("drag: " + selectedTableId);
                if (shape.getId() == selectedTableId) {


                    updateLocation(shape, e);
                } else {

                }
            });

        }

        @Override
        public void mouseReleased(MouseEvent e) {

            shapes.stream().forEach(shape -> {
                if (shape.contains(e.getX(), e.getY())) {
                    updateLocation(shape, e);

                } else {
                    //shape.setPressOut(false);
                }
            });

            //selectedTableId = -1;
        }

        public void updateLocation(CanvasShape shape, MouseEvent e) {

            shape.setLocation(shape.getPreX() + e.getX(), shape.getPreY() + e.getY());
            //checkRect((Rectangle) shape);

            repaint();

        }
    }
}
