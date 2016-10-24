package com.prova.nonogram;

import android.graphics.Canvas;

public class LoopJoc extends Thread 
{
    private GameDisplay view;
    private boolean running = false;
    static final long FPS = 30;

    public LoopJoc(GameDisplay view)
    {
        this.view = view;
    }

    public void setRunning(boolean run) 
    {
        running = run;
    }

    @Override
    public void run() 
    {
        long ticksPS = 1000 / FPS;
        long startTime;
        long sleepTime;
        while (true) 
        {
            Canvas c = null;
            if (running)
            {
	            startTime = System.currentTimeMillis();
	            try 
	       
	            {
	                c = view.getHolder().lockCanvas();
	                synchronized (view.getHolder()) 
	                {
	                    view.drawGame(c);
	                }
	            } finally 
	            {
	                if (c != null) view.getHolder().unlockCanvasAndPost(c);
	            }
	            sleepTime = ticksPS - (System.currentTimeMillis() - startTime);
	            try 
	            {
	                if (sleepTime > 0) sleep(sleepTime);
	            } 
	            catch (Exception e) 
	            {
                    e.printStackTrace();
	            }
            }
        }
    }
}