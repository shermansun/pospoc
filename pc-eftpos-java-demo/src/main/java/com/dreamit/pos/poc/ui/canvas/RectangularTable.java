package com.dreamit.pos.poc.ui.canvas;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class RectangularTable extends CanvasShape{

    private boolean isDragging;
    private boolean pressOut;

    private Rectangle rectangle;


    public RectangularTable(int id, int x, int y, int w, int h, Color color){

        rectangle = new Rectangle(x, y, w, h);
        this.id = id;
        this.color = color;
    }

    public void setLocation(int x, int y){
        rectangle.setLocation(x, y);
    }

    public int getPreX() {
        return preX;
    }

    public void setPreX(int preX) {
        this.preX = preX;
    }

    public int getPreY() {
        return preY;
    }

    public void setPreY(int preY) {
        this.preY = preY;
    }

    public boolean isPressOut() {
        return pressOut;
    }

    public void setPressOut(boolean pressOut) {
        this.pressOut = pressOut;
    }

    public boolean isDragging() {
        return isDragging;
    }

    public void setDragging(boolean dragging) {
        isDragging = dragging;
    }

    public int getId() {
        return id;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public Rectangle getBounds() {
        return rectangle.getBounds();
    }

    @Override
    public Rectangle2D getBounds2D() {
        return rectangle.getBounds2D();
    }

    @Override
    public boolean contains(double x, double y) {
        return rectangle.contains(x, y);
    }

    @Override
    public boolean contains(Point2D p) {
        return rectangle.contains(p);
    }

    @Override
    public boolean intersects(double x, double y, double w, double h) {
        return rectangle.intersects(x, y, w, h);
    }

    @Override
    public boolean intersects(Rectangle2D r) {
        return rectangle.intersects(r);
    }

    @Override
    public boolean contains(double x, double y, double w, double h) {
        return rectangle.contains(x, y, w, h);
    }

    @Override
    public boolean contains(Rectangle2D r) {
        return rectangle.contains(r);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at) {
        return rectangle.getPathIterator(at);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return rectangle.getPathIterator(at, flatness);
    }
}
