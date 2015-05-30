package com.example.s_genberg.game_test;


import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Player extends GameObject{
    private Bitmap spritesheet;
    private int score;
    private boolean up;
    private boolean down; //added by gabe
    private boolean playing;
    private Animation animation = new Animation();
    private long startTime;

    public Player(Bitmap res, int w, int h, int numFrames) {
        x = 100;
        y = GamePanel.PLAYER_SPAWN;
        dy = 0;
        score = 0;
        height = h;
        width = w;

        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;

        for(int i = 0; i < image.length; i++) {
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }

        animation.setFrames(image);
        animation.setDelay(100); // DELAY
        startTime = System.nanoTime();
    }
    public void setUp(boolean b) {up = b;}
    public void setDown(boolean d) {down = d;} // added by gabe
    public void update() {
        long elapsed = (System.nanoTime()-startTime)/1000000;
        if(elapsed> 100) {
            score++;
            startTime = System.nanoTime();
        }
        animation.update();
        if(up) {
            dy -= 1;
        } else if(down){  //changed from else by gabe
            dy += 1;
        } else {  //changed by gabe
            dy += 1;
        }
        if(dy > 14) {
            dy = 14;
        }
        if(dy < -14) {
            dy = -14;
        }
        y += dy*2;
        
    }
    public void draw(Canvas canvas) {
    canvas.drawBitmap(animation.getImage(), x, y, null);
    }
    public int getScore() {return score;}
    public boolean getPlaying() {return playing;}
    public void setPlaying(boolean b){playing = b;}
    public void resetDY() {dy = 0;}
    public void resetScore() {score = 0;}
    public void setDownSpeed() {dy = 0;}


}


