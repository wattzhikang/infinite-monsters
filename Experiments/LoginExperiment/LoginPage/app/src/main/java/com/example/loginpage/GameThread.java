package com.example.loginpage;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends Thread
{
    private SurfaceHolder surfaceHolder;
    private GameView gameView;
    private boolean running;
    public static Canvas canvas;
    
    public GameThread(SurfaceHolder holder, GameView gameView)
    {
       super();
       this.surfaceHolder = holder;
       this.gameView = gameView;
    }
    
    public void setRunning(boolean isRunning)
    {
        running = isRunning;
    }
    
    @Override
    public void run()
    {
        while(running)
        {
            canvas = null;
            
            try
            {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder)
                {
                    this.gameView.update();
                    this.gameView.draw(canvas);
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            
            finally
            {
                if(canvas != null)
                {
                    try
                    {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
