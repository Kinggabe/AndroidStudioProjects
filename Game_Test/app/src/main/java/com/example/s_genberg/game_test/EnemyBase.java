package com.example.s_genberg.game_test;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;

/**
 * Created by s-genberg on 6/4/2015.
 */
public class EnemyBase extends GameObject {
    private Bitmap spritesheet;
    private int score;
    public int leftmax;
    public boolean left;
    public boolean right;
    public boolean shooting;
    public boolean jumping;
    private boolean playing;
    private int speed;
    private int health;
    private Bitmap[] healthbar;
    private int distanceTraveled;
    private Animation animation = new Animation();
    private long startTime;
    private long jumpstartTime;
    private HealthBar borderhealth;
    private ArrayList<bullets> Arraybullets;
    private Bitmap bullet;
    private Bitmap bulletexplode;

    public EnemyBase(Bitmap res, int w, int h, int numFrames, Bitmap bullet, Bitmap bulletexplode, Bitmap[] healthB) {
        x = 100;
        y = GamePanel.PLAYER_SPAWN;
        speed = -5;
        score = 0;
        height = h;
        width = w;
        health = 100;
        healthbar = healthB;
        borderhealth = new HealthBar(healthB[0], 700, 800);
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
            if(y != 535) {
                y-=17;
            } else {
                jumping = false;
                y+=5;
            }
        }
        else {
            if(y <= 615) {
                y+=5;
            }
        }
        animation.update();
        if(shooting) {
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
            speed+=2;
            if(speed >= 6) {
                speed = 6;
            }
            distanceTraveled-=1;
        } else if(!left){
            speed-=2;
            if(speed <= 0) {
                speed = 0;
            }
        }
        if(right) {
            speed -= 2;
            if(speed <= -6) {
                speed = -6;
            }
            distanceTraveled+=2;
        } else if(!right) {
            speed+=2;
            if(speed >= 0) {
                speed = 0;
            }
        }
        GamePanel.MOVESPEED = speed;
        System.out.println(distanceTraveled);
    }
    public void draw(Canvas canvas) {
        if(health == 100) {
            borderhealth.changeImage(healthbar[0]);
        } else if(health >= 60) {
            borderhealth.changeImage(healthbar[1]);
        } else if(health >= 40) {
            borderhealth.changeImage(healthbar[2]);
        } else if(health >= 20) {
            borderhealth.changeImage(healthbar[3]);
        } else if(health >= 0) {
            borderhealth.changeImage(healthbar[4]);
        }
        borderhealth.draw(canvas);
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
    public void setLeft(int l) {leftmax = l;}
    public void damaged() {health-=10; if(health < 0) {health = 0;}}
    public int getHealth() {return health;}





}
