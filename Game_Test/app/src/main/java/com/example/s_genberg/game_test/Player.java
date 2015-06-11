package com.example.s_genberg.game_test;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.lang.reflect.Array;
import java.util.ArrayList;
public class Player extends GameObject{
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
    public ArrayList<bullets> Arraybullets;
    private Bitmap bullet;
    private Bitmap bulletexplode;
    private boolean enemy = false;

    public Player(Bitmap res, int w, int h, int numFrames, Bitmap bullet, Bitmap bulletexplode, Bitmap[] healthB) {
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
            System.out.println("JUMP");
            if(y != 300) {
                y-=20;
            } else {
                jumping = false;
                y+=10;
            }
        }
        else {
            if(y != 560) {
                y+=10;
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
            speed = speed+2;
            if(speed >= 6) {
                speed = 6;
            } if(speed ==8) {
                speed = 6;
            }
                distanceTraveled-=1;
        } else if(!left) {
            speed = speed-2;
            if(speed <= 0) {
                speed = 0;
            }
        } if(right) {
            speed = speed-2;
            if(speed == -2) {
                speed = -4;
            } if(speed == -4) {
                speed = -6;
            }
            if(speed <= -6) {
                speed = -6;
            }
            distanceTraveled+=1;
            if(distanceTraveled == 400) {
                enemy = true;
                distanceTraveled = 0;
            }
        } else if(!right){
            speed = speed+2;
            if(!left) {
                if (speed >= 0) {
                    speed = 0;
                }
            }
        }
        GamePanel.MOVESPEED = speed;
    }
    public void draw(Canvas canvas) {
        if(health == 100) {
            borderhealth.changeImage(healthbar[10]);
        } else if(health == 90) {
            borderhealth.changeImage(healthbar[9]);
        } else if(health == 80) {
            borderhealth.changeImage(healthbar[8]);
        } else if(health == 70) {
            borderhealth.changeImage(healthbar[7]);
        } else if(health == 60) {
            borderhealth.changeImage(healthbar[6]);
        } else if(health == 50) {
            borderhealth.changeImage(healthbar[5]);
        } else if(health == 40) {
            borderhealth.changeImage(healthbar[4]);
        } else if(health == 30) {
            borderhealth.changeImage(healthbar[3]);
        } else if(health == 20) {
            borderhealth.changeImage(healthbar[2]);
        } else if(health == 10) {
            borderhealth.changeImage(healthbar[1]);
        } else if(health == 0) {
            borderhealth.changeImage(healthbar[0]);
            setPlaying(false);
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
    public boolean getEnemy() {return enemy;}
    public void setEnemy(boolean g) {enemy = g;}


}