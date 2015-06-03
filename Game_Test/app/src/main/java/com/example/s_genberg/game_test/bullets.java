package com.example.s_genberg.game_test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class bullets extends GameObject{
    private Bitmap spritesheet;
    private Animation animation = new Animation();
    private int speed;
    private boolean Visible;
    private boolean hit;
    private Bitmap res;
    public bullets(Bitmap res, Bitmap hitres, int w, int h, int numFrames, int x, int y) {
      width = w;
      height = h;
      this.x = x;
      this.y = y;
      speed = 15;
      Visible = true;
      this.res = res;
      Bitmap[] image = new Bitmap[numFrames];
      spritesheet = hitres;
        for(int i = 0; i < image.length; i++) {
            System.out.println(i*width);
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }
      animation.setFrames(image);
      animation.setDelay(100);
    }
    public void update() {
        x+=speed;
    }
    public void draw(Canvas canvas) {
        if(Visible) {
            if(!hit) {
                canvas.drawBitmap(res, x, y, null);
            }
            else {
                canvas.drawBitmap(animation.getImage(), x, y, null);
            }
        }
    }
    public int getX() {return x;}
    public void setHit() {}
}
