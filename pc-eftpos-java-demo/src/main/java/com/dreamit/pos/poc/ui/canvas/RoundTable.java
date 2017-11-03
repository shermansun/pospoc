package com.dreamit.pos.poc.ui.canvas;

import java.awt.*;
import java.awt.geom.*;

public class RoundTable extends CanvasShape{

    private Ellipse2D round;


    public RoundTable(int id, double x,  double y, double w, double h, Color color){

        this.id = id;
        this.round = new Ellipse2D.Double(x, y, w, h);
        this.color = color;
    }

    @Override
    public int getPreX() {
        return preX;
    }

    @Override
    public void setPreX(int preX) {
        this.preX = preX;
    }

    @Override
    public int getPreY() {
        return preY;
    }

    @Override
    public void setPreY(int preY) {
        this.preY = preY;
    }

    @Override
    public void setLocation(int x, int y) {
        //round.getBounds().setLocation(x, y);
        round.setFrame(x, y, round.getWidth(), round.getHeight());
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public Rectangle getBounds() {
        return round.getBounds();
    }

    @Override
    public Rectangle2D getBounds2D() {
        return round.getBounds2D();
    }

    @Override
    public boolean contains(double x, double y) {
        return round.contains(x, y);
    }

    @Override
    public boolean contains(Point2D p) {
        return round.contains(p);
    }

    @Override
    public boolean intersects(double x, double y, double w, double h) {
        return round.intersects(x, y, w, h);
    }

    @Override
    public boolean intersects(Rectangle2D r) {
        return round.intersects(r);
    }

    @Override
    public boolean contains(double x, double y, double w, double h) {
        return round.contains(x, y, w, h);
    }

    @Override
    public boolean contains(Rectangle2D r) {
        return round.contains(r);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at) {
        return round.getPathIterator(at);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return round.getPathIterator(at, flatness);
    }
}
