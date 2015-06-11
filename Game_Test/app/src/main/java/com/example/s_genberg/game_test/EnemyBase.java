package com.example.s_genberg.game_test;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;

/**
 * Created by s-genberg on 6/4/2015.
 */
public class EnemyBase extends GameObject {
    private Bitmap spritesheet;
    public int leftmax;
    public boolean left;
    public boolean right;
    public boolean shooting;
    public boolean jumping;
    private boolean playing;
    private int distanceTraveled;
    private Animation animation = new Animation();
    public ArrayList<enemy_bullets> Arraybullets;
    private Bitmap bullet;
    private Bitmap bulletexplode;
    private int health = 100;

    public EnemyBase(Bitmap res, int w, int h, int numFrames, Bitmap bullet, Bitmap bulletexplode) {
        x = 1200;
        y = 560;
        height = h;
        width = w;

        this.bullet = bullet;
        this.bulletexplode = bulletexplode;
        shooting = false;
        jumping = false;
        distanceTraveled = 0;
        Arraybullets = new ArrayList<enemy_bullets>();
        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;
        for(int i = 0; i < image.length; i++) {
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }
        animation.setFrames(image);
        animation.setDelay(100); // DELAY
    }
    public void setRight(boolean b) {right = b;}
    public void setLeft(boolean d) {left = d;}
    public void update() {
        animation.update();
        if(shooting) {
            Arraybullets.add(new enemy_bullets(bullet, bulletexplode, 9, 6, 4, x, y+90));
            shooting = false;
        }
        for(enemy_bullets b: Arraybullets) {
            b.update();
        }
        for(int i = 0; i > Arraybullets.size();i++) {
            if(Arraybullets.get(i).getX() > 1780) {
                Arraybullets.remove(i);
            }
        }
        x+= GamePanel.MOVESPEED/2;
    }
    public void draw(Canvas canvas) {
        for(enemy_bullets b: Arraybullets) {
            b.draw(canvas);
        }
        canvas.drawBitmap(animation.getImage(), x, y, null);
    }
    public boolean getPlaying() {return playing;}
    public void setPlaying(boolean b){playing = b;}
    public void setDistanceTraveled(int j) {distanceTraveled = j;}
    public int getDistanceTraveled() {return distanceTraveled;}
    public void damaged(){health-=5;}
    public int getHeath() {return health;}



}
