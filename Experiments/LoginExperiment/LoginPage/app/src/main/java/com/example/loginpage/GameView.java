package com.example.loginpage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.MainThread;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

public class GameView extends SurfaceView implements SurfaceHolder.Callback
{
    public GameThread thread;
    
    public GameView(Context context)
    {
        super(context);
        getHolder().addCallback(this);
        
        thread = new GameThread(getHolder(), this);
        setFocusable(true);
    }
    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
    
    }
    
    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        thread.setRunning(true);
        thread.start();
    }
    
    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        boolean retry = true;
        while(retry)
        {
            try
            {
                thread.setRunning(false);
                thread.join();
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
            retry = false;
        }
    }
    
    public void update()
    {
    
    }
    
    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);
        if(canvas != null)
        {
            canvas.drawColor(Color.WHITE);
            Paint paint = new Paint();
            paint.setColor(Color.rgb(250, 0, 0));
            canvas.drawRect(100, 100, 200, 200, paint);
        }
    }
}
