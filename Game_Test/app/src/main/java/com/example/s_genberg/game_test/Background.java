package com.example.s_genberg.game_test;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Background {

    private Bitmap image;
    private int x, y, speed;

    public Background(Bitmap res)
    {
        image = res;
    }
    public void update()
    {
        speed = GamePanel.MOVESPEED;
        x+=speed;
        if(x<-GamePanel.WIDTH){
            x=0;
        }
    }
    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(image, x, y,null);
        if(x<0)
        {
            canvas.drawBitmap(image, x+GamePanel.WIDTH, y, null);
        }
    }
    public void setVector(int dx)
    {
        speed = dx;
    }
}