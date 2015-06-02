package com.example.s_genberg.game_test;


import android.graphics.Bitmap;
import android.graphics.Canvas;

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


    public Player(Bitmap res, int w, int h, int numFrames) {
        x = 100;
        y = GamePanel.PLAYER_SPAWN;
        speed = -5;
        score = 0;
        height = h;
        width = w;
        shooting = false;
        jumping = false;
        distanceTraveled = 0;

        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;

        for(int i = 0; i < image.length; i++) {
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }

        animation.setFrames(image);
        animation.setDelay(100); // DELAY
        startTime = System.nanoTime();
    }
    public void setRight(boolean b) {right = b;}
    public void setLeft(boolean d) {left = d;}
    public void update() {
        long elapsed = (System.nanoTime()-startTime)/1000000;
        if(elapsed> 100) {
            score++;
            startTime = System.nanoTime();
        }
        animation.update();
        if(shooting) {

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