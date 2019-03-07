package com.example.loginpage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.MainThread;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

public class GameView extends SurfaceView implements SurfaceHolder.Callback
{
    public GameThread thread;
    int height = 20;
    int width = 20;
    TileFactory tileMap = new TileFactory(width, height);
    Bitmap grass, path, tallgrass, bush, treeBottomLeft, treeTopLeft, treeTopRight, treeBottomRight;
    public GameView(Context context)
    {
        super(context);
        getHolder().addCallback(this);
        grass = new BitmapFactory().decodeResource(getResources(), R.drawable.pokemon_test_grass);
        path = new BitmapFactory().decodeResource(getResources(), R.drawable.pokemon_test_path);
        tallgrass = new BitmapFactory().decodeResource(getResources(), R.drawable.pokemon_test_tallgrass);
        bush = new BitmapFactory().decodeResource(getResources(), R.drawable.pokemon_test_bush);
        treeBottomLeft = new BitmapFactory().decodeResource(getResources(), R.drawable.pokemon_test_tree_bottom_left);
        treeTopLeft = new BitmapFactory().decodeResource(getResources(), R.drawable.pokemon_test_tree_top_left);
        treeTopRight = new BitmapFactory().decodeResource(getResources(), R.drawable.pokemon_test_tree_top_right);
        treeBottomRight = new BitmapFactory().decodeResource(getResources(), R.drawable.pokemon_test_tree_bottom_right);
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
            canvas.drawColor(Color.BLACK);
            Paint paint = new Paint();
            for(int x = 0; x < 20; x++)
            {
                for(int y = 0; y < 20; y++)
                {
                    if(tileMap.getTile(x, y) == 0)
                    {
                        canvas.drawBitmap(grass, x*16, y*16, paint);
                    }
                    else if(tileMap.getTile(x, y) == 1)
                    {
                        canvas.drawBitmap(path, x*16, y*16, paint);
                    }
                    /*else if(tileMap.getTile(x, y) == 2)
                    {
                        canvas.drawBitmap(tallgrass, x, y, paint);
                    }
                    else if(tileMap.getTile(x, y) == 3)
                    {
                        canvas.drawBitmap(bush, x, y, paint);
                    }
                    else if(tileMap.getTile(x, y) == 4)
                    {
                        canvas.drawBitmap(treeBottomLeft, x, y, paint);
                    }
                    else if(tileMap.getTile(x, y) == 5)
                    {
                        canvas.drawBitmap(treeTopLeft, x, y, paint);
                    }
                    else if(tileMap.getTile(x, y) == 6)
                    {
                        canvas.drawBitmap(treeTopRight, x, y, paint);
                    }
                    else if(tileMap.getTile(x, y) == 7)
                    {
                        canvas.drawBitmap(treeBottomRight, x, y, paint);
                    }*/
                }
            }
            /*paint.setColor(Color.rgb(250, 0, 0));
            canvas.drawRect(100, 100, 200, 200, paint);*/
        }
    }
}
