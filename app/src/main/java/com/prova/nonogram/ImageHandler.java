package com.prova.nonogram;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by Ramon on 26/9/16.
 */
public class ImageHandler
{
    private final boolean DEBUG = false;
    private Bitmap bmp;
    private int x;
    private int y;
    private Random ran;
    private boolean visible;
    private int width;
    private int height;  // de la imatge
    private int dibuix;
    private int posX;
    private int posY;
    private int ampla;   // de la instància
    private int alt;
    private int alpha; // nivell de transparència
    private boolean seleccionat;

    public ImageHandler(GameDisplay joc, int dib)
    {
        seleccionat = false;
        dibuix = dib;
        alpha = 255;  // totalment opac
        visible = true;
        bmp = BitmapFactory.decodeResource(joc.getResources(), dibuix);
        this.width = bmp.getWidth();
        this.height = bmp.getHeight();
    }

    public void pinta(Canvas canvas, int x, int y, int w, int h)
    {
        if (!visible) return;

        Rect src = new Rect(0, 0, width, height);
        Rect dst = new Rect(x, y, x + w, y + h);
        Paint paint = new Paint();
        paint.setAlpha(alpha);
        canvas.drawBitmap(bmp, src, dst, paint);
        posX = x;
        posY = y;
        ampla = w;
        alt = h;

        if (seleccionat)
        {
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth((float) 1.0);
            canvas.drawRect(x-2, y-2, x+w+4, y+h+4, paint);
        }
    }

    public boolean contePunt(float x2, float y2) {
        return (x2 > posX && x2 < posX + ampla) && (y2 > posY && y2 < posY + alt);
    }

    public void selecciona() { seleccionat = true; }

    public void deselecciona()
    {
        seleccionat = false;
    }

    public void setPosicio(int a, int b)
    {
        x = a;
        y = b;
    }

    public boolean esVisible()
    {
        return visible;
    }

    public void setVisible(boolean v)
    {
        visible = v;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public int getAlpha() {
        return alpha;
    }
    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }
}