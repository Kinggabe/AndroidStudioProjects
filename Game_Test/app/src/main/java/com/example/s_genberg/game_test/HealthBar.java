package com.example.s_genberg.game_test;

import android.graphics.Bitmap;
import android.graphics.Canvas;


public class HealthBar extends GameObject {
    private Bitmap image;
    private int speed;
    public HealthBar(Bitmap res, int x, int y) {
        height = 130;
        width = 600;
        this.x = x;
        this.y = y;
        speed = 0;
        dx = GamePanel.MOVESPEED;
        image = Bitmap.createBitmap(res, 0, 0, width, height);
    }
    public void draw(Canvas canvas) {
        try{ canvas.drawBitmap(image, x, y, null); }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void update() {
    }
    public int getX() {return x;}
    public void changeImage(Bitmap b) {image = Bitmap.createBitmap(b, 0, 0, width, height);}
}
