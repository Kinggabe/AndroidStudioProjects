package com.example.s_genberg.game_test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class BetterButton extends GameObject{
    private Bitmap spritesheet;
    private Animation animation = new Animation();
    private boolean pressed;
    private boolean Visible;
    private int speed;
    private Bitmap pressedres;
    public BetterButton(Bitmap res, Bitmap pressedres, int w, int h, int numFrames, int x, int y) {
        height = h;
        width = w;
        this.x = x;
        this.y = y;
        speed = 0;
        Visible = true;
        pressed = false;
        this.pressedres = pressedres;
        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;
        for(int i = 0; i < image.length; i++) {
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }
        animation.setFrames(image);
        animation.setDelay(100);
    }
    public void draw(Canvas canvas) {
        if(Visible) {
            if(!pressed) {
                canvas.drawBitmap(animation.getImage(), x, y, null);
            }
            else {
                canvas.drawBitmap(pressedres, x, y, null);
            }
        }
    }
    public void update() {
        speed = GamePanel.MOVESPEED;
        animation.update();

    }
    public boolean getPress() {return pressed;}
    public void setVisible(boolean v) {Visible = v;}
    public void setPressed(boolean v) {pressed = v;}
    public boolean checkPress(double xx, double yy) {
        if(xx > x-5 && xx < x+width+5) {
            if(yy < y+height+100 && yy > y+5) {
               return true;
            }
        }
        return false;
    }

}
