package com.example.s_genberg.game_test;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Player extends GameObject{
    private Bitmap spritesheet;
    private int score;
    public boolean left;
    public boolean right;
    public boolean shooting;
    public boolean jumping;
    private boolean playing;
    private int speed;
    private int distanceTraveled;
    private Animation animation = new Animation();
    private long startTime;
    private long jumpstartTime;
    private ArrayList<bullets> Arraybullets;
    private Bitmap bullet;
    private Bitmap bulletexplode;

    public Player(Bitmap res, int w, int h, int numFrames, Bitmap bullet, Bitmap bulletexplode) {
        x = 100;
        y = GamePanel.PLAYER_SPAWN;
        speed = -5;
        score = 0;
        height = h;
        width = w;
        this.bullet = bullet;
        this.bulletexplode = bulletexplode;
        shooting = false;
        jumping = false;
        distanceTraveled = 0;
        Arraybullets = new ArrayList<bullets>();
        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;

        for(int i = 0; i < image.length; i++) {
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }
        animation.setFrames(image);
        animation.setDelay(100); // DELAY
        startTime = System.nanoTime();
        jumpstartTime = System.nanoTime();
    }
    public void setRight(boolean b) {right = b;}
    public void setLeft(boolean d) {left = d;}
    public void update() {
        long elapsed = (System.nanoTime()-startTime)/1000000;
        if(elapsed> 100) {
            score++;
            startTime = System.nanoTime();
        }
        if(jumping) {
            if(y != 400) {
                y-=4;
            } else {
                jumping = false;
                y+=4;
            }
        }
        else {
            if(y <= 620) {
                y+=4;
            }
        }
        animation.update();
        if(shooting) {
            System.out.println("BUllet");
            Arraybullets.add(new bullets(bullet, bulletexplode, 9, 6, 4, x+87, y+88));
            shooting = false;
        }
        for(bullets b: Arraybullets) {
            b.update();
        }
        for(int i = 0; i > Arraybullets.size();i++) {
            if(Arraybullets.get(i).getX() > 1780) {
                Arraybullets.remove(i);
            }
        }
        if(left) {
            speed = 5;
                distanceTraveled-=2;
        }
        if(right) {
            speed = -5;
            distanceTraveled+=2;
        }
        GamePanel.MOVESPEED = speed;
    }
    public void draw(Canvas canvas) {
        for(bullets b: Arraybullets) {
            b.draw(canvas);
        }
    canvas.drawBitmap(animation.getImage(), x, y, null);
    }
    public int getScore() {return score;}
    public boolean getPlaying() {return playing;}
    public void setPlaying(boolean b){playing = b;}
    public void resetSpeed() {speed = 0;}
    public void resetScore() {score = 0;}
    public void setDistanceTraveled(int j) {distanceTraveled = j;}
    public int getDistanceTraveled() {return distanceTraveled;}


}