package com.prova.nonogram;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Juancki on 15/09/16.
 */

public class Col {
    private int color;
    private Form f;


    public Col(int color, Form f) {
        this.color = color;
        this.f = f;
    }

    @Override
    public boolean equals(Object o) {
        Col c = (Col) o;
        if (c.getF() != this.getF()){
            return false;
        }
        if(c.getC() != this.getC()){
            return false;
        }
        return true;

    }

    public int getC() {
        return color;
    }

    public Form getF() {
        return f;
    }


    public void drawColOnCanvas(Canvas c, int startX,int startY, int widht,int height,int padding,Paint p){
        switch (f){
            case CIRCLE:
                int r = widht/2-padding;
                int halfx = widht/2;
                int halfy = height/2;
                c.drawCircle(startX+halfx, startY+halfy, r-padding, p);
                break;
            case SQUARE:
                c.drawRect(startX+padding,startY+padding,startX+ widht-padding,startY+height-padding,p);
        }
    }
    public void drawColOnCanvas(Canvas c, int startX,int startY, int widht,int height,int padding){

        Paint p = new Paint();
        p.setColor(color);
        p.setAlpha(255);
        p.setStyle(Paint.Style.FILL);
        switch (f){
            case CIRCLE:
                int r = widht/2-padding/2;
                p.setStrokeWidth(4);
                c.drawCircle(startX+widht/2, startY+height/2, r-padding/2, p);
                break;
            case SQUARE:
                c.drawRect(startX+padding,startY+padding,startX+ widht-padding,startY+height-padding,p);
        }

        /*Paint p = new Paint();
        p.setColor(color);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(4);
        drawColOnCanvas(c, startX,startY, finishX, finishY, padding, p);*/
    }

}
