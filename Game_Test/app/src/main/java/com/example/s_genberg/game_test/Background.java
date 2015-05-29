package com.example.s_genberg.game_test;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Background {
    private Bitmap spritesheet;
    private Bitmap mimage;
    private int x, y, dx;
    private long startTime;
    private int bgon;
    private Bitmap[] image;

    public Background(Bitmap res, int w, int h, int numFrames) {
        mimage = res;
        bgon = 0;
        dx = GamePanel.MOVESPEED;
        image = new Bitmap[numFrames];
        spritesheet = res;
        for(int i = 0; i < image.length; i++) {
            image[i] = Bitmap.createBitmap(spritesheet, i*w, 0, w, h);
        }
        startTime = System.nanoTime();
    }
    public void update() {
        Long bgElapsed = (System.nanoTime() - startTime) / 1000000;
        System.out.println(bgElapsed);
        System.out.println(bgon);
        if(bgElapsed > 2000) {
            startTime = System.nanoTime();
            bgon++;
            if(bgon > 4) {
                bgon = 0;
            }
        }
        else {
            bgon = 0;
        }

    x += dx;
        if(x <-GamePanel.WIDTH) {
            x = 0;
        }
    }
    public void draw(Canvas canvas) {
    canvas.drawBitmap(image[0], x, y, null);
        if(x< 0) {
            canvas.drawBitmap(image[0], x, y, null);
        }
    }

}
