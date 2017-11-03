package com.dreamit.pos.poc.ui.canvas;


import java.awt.*;

public abstract class CanvasShape implements Shape{

    protected int id;
    protected int preX;
    protected int preY;
    protected Color color;

    public abstract int getPreX();

    public abstract void setPreX(int preX);

    public abstract int getPreY();

    public abstract void setPreY(int preY);

    //public abstract boolean isPressOut();

    //public abstract void setPressOut(boolean pressOut);

    public abstract void setLocation(int x, int y);

    public abstract Color getColor();

    public int getId(){
        return id;
    }




}
