package com.example.s_genberg.game_test;

import android.graphics.Bitmap;
import android.graphics.Canvas;


public class Border extends GameObject {
    private Bitmap image;
    public Border(Bitmap res, int x, int y) {
        height = 60;
        width = 60;
        this.x = x;
        this.y = y;

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
        x+=dx;
    }
}
