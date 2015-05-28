package com.example.s_genberg.game_test;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Background {
    private Bitmap spritesheet;
    private Bitmap image;
    private int x, y, dx;
    private Animation animation = new Animation();
    private long startTime;

    public Background(Bitmap res, int w, int h, int numFrames) {
        image = res;
        dx = GamePanel.MOVESPEED;
        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;

        for(int i = 0; i < image.length; i++) {
            image[i] = Bitmap.createBitmap(spritesheet, i*w, 0, w, h);
        }
        startTime = System.nanoTime();
    }
    public void update() {
        animation.update();
    x += dx;
        if(x <-GamePanel.WIDTH) {
            x = 0;
        }
    }
    public void draw(Canvas canvas) {
    canvas.drawBitmap(image, x, y, null);
        if(x< 0) {
            canvas.drawBitmap(image, x+GamePanel.WIDTH, y, null);
        }
    }

}
