package com.prova.nonogram;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;

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
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth((float) 4.0);
            canvas.drawRect(x-5, y-5, x+w+5, y+h+5, paint);
        }
    }

    public void pintaAmbFiltre(Canvas canvas, int x, int y, int w, int h, int c, int transparencia, Form form)
    {
        if (!visible) return;

        int red = (c & 0xFF0000) / 0xFFFF;
        int green = (c & 0xFF00) / 0xFF;
        int blue = c & 0xFF;

        float[] colorTransform = {
                0, 0f, 0, 0, red,
                0, 0, 0f, 0, green,
                0, 0, 0, 0f, blue,
                0, 0, 0, 1f, 0};

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0f); //Remove Colour
        colorMatrix.set(colorTransform); //Apply the Red
        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);

        Rect src = new Rect(0, 0, width, height);
        Rect dst = new Rect(x, y, x + w, y + h);
        Paint paint = new Paint();

        canvas.drawBitmap(bmp, src, dst, paint);
        paint.setAlpha(transparencia);
        paint.setColorFilter(colorFilter);
        canvas.drawBitmap(bmp, src, dst, paint);
        posX = x;
        posY = y;
        ampla = w;
        alt = h;

        if (seleccionat)
        {
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth((float) 4.0);
            //
            if(form==Form.CIRCLE){
                canvas.drawCircle(x+w/2,y+h/2,w/2+5,paint);
            } else {
                canvas.drawRect(x-5, y-5, x+w+5, y+h+5, paint);
            }


        }
    }

    public boolean contePunt(float x2, float y2) {
        boolean aux = (x2 > posX && x2 < posX + ampla) && (y2 > posY && y2 < posY + alt);
        if (aux){
            selecciona();
        } else {
            deselecciona();
        }
        return aux;
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