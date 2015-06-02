package com.example.s_genberg.game_test;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Random;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback
{
    public static final int WIDTH = 1712;
    public static final int HEIGHT = 960;
    public static int MOVESPEED = -5;
    public static final int PLAYER_SPAWN = HEIGHT-90;
    private long smokeStartTime;
    private long missileStartTime;
    private MainThread thread;
    private Background bg;
    private Player player;
    private BetterButton leftButton;
    private BetterButton rightButton;
    private BetterButton jumpButton;
    private BetterButton shootButton;
    private ArrayList<Smokepuff> smoke;
    private ArrayList<Border> border;
    private ArrayList<Border> dirt2;
    private ArrayList<Border> dirt1;
    private Random rand = new Random();
    private boolean newGameCreated;
    //increase to slow down difficulty progression, decrease to speed up difficulty progression
    private int progressDenom = 20;


    public GamePanel(Context context)
    {
        super(context);

        //add the callback to the surfaceholder to intercept events
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        //make gamePanel focusable so it can handle events
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        int counter = 0;
        while(retry && counter<1000)
        {
            counter++;
            try{thread.setRunning(false);
                thread.join();
                retry = false;

            }catch(InterruptedException e){e.printStackTrace();}
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.mainbackground));
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.maincow), 90, 160, 5);
        smoke = new ArrayList<Smokepuff>();
        border = new ArrayList<Border>();
        dirt1 = new ArrayList<Border>();
        dirt2 = new ArrayList<Border>();
        leftButton = new BetterButton(BitmapFactory.decodeResource(getResources(), R.drawable.left),BitmapFactory.decodeResource(getResources(), R.drawable.leftpress), 300, 150, 1, 10, 800);
        rightButton = new BetterButton(BitmapFactory.decodeResource(getResources(), R.drawable.right),BitmapFactory.decodeResource(getResources(), R.drawable.rightpress), 300, 150, 1, 350, 800);
        jumpButton = new BetterButton(BitmapFactory.decodeResource(getResources(), R.drawable.jump),BitmapFactory.decodeResource(getResources(), R.drawable.jumppress), 300, 150, 1, 1400, 800);
        shootButton = new BetterButton(BitmapFactory.decodeResource(getResources(), R.drawable.shoot),BitmapFactory.decodeResource(getResources(), R.drawable.shootpress), 200, 200, 1, 1480, 550);

        smokeStartTime=  System.nanoTime();
        missileStartTime = System.nanoTime();

        //we can safely start the game loop
        thread.setRunning(true);
        thread.start();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getAction()==MotionEvent.ACTION_DOWN) {
            if(!player.getPlaying()) {
                player.setPlaying(true);
                if(event.getX() > 10 && event.getX() < 300) {
                    if(event.getY() < 950 && event.getY() > 800) {
                        System.out.println("LEFT");
                    }
                }
                if(event.getX() > 350 && event.getX() < 650) {
                    if(event.getY() < 950 && event.getY() > 800) {
                        System.out.println("RIGHT");
                    }
                }
                if(event.getX() > 1400 && event.getX() < 1700) {
                    if(event.getY() < 950 && event.getY() > 800) {
                        System.out.println("JUMP");
                    }
                }
                if(event.getX() > 1480 && event.getX() < 300) {
                    if(event.getY() < 750 && event.getY() > 550) {
                        System.out.println("SHOOT");
                    }
                }
            }
            else {
                if(event.getX() > 10 && event.getX() < 300) {
                    if(event.getY() < 950 && event.getY() > 800) {
                        System.out.println("LEFT");
                    }
                }
                if(event.getX() > 350 && event.getX() < 650) {
                    if(event.getY() < 950 && event.getY() > 800) {
                        System.out.println("RIGHT");
                    }
                }
                if(event.getX() > 1400 && event.getX() < 1700) {
                    if(event.getY() < 950 && event.getY() > 800) {
                        System.out.println("JUMP");
                    }
                }
                if(event.getX() > 1480 && event.getX() < 300) {
                    if(event.getY() < 750 && event.getY() > 550) {
                        System.out.println("SHOOT");
                    }
                }
            }
            return true;
        }
        if(event.getAction()==MotionEvent.ACTION_UP)
        {
            return true;
        }
        return super.onTouchEvent(event);
    }
    public void update()
    {
        if(player.getPlaying()) {
                System.out.println("PLAYING!!");
            bg.update();
            player.update();
            //BORDERS AKA Ground !! !! !!  !!  !!  !!  !!  !!  !! !!
            this.Updateborder();
            //add smoke puffs on timer
            long elapsed = (System.nanoTime() - smokeStartTime)/1000000;
            if(elapsed > 120){
                smoke.add(new Smokepuff(player.getX(), player.getY()+60));
                smokeStartTime = System.nanoTime();
            }
            for(int i = 0; i<smoke.size();i++)
            {
                smoke.get(i).update();
                if(smoke.get(i).getX()<-10)
                {
                    smoke.remove(i);
                }
            }
        }
        else{
            newGameCreated = false;
            if(!newGameCreated) {
                newGame();
            }
        }
    }
    public boolean collision(GameObject a, GameObject b)
    {
        if(Rect.intersects(a.getRectangle(), b.getRectangle()))
        {
            return true;
        }
        return false;
    }
    @Override
    public void draw(Canvas canvas)
    {
        final float scaleFactorX = getWidth()/(WIDTH*1.f);
        final float scaleFactorY = getHeight()/(HEIGHT*1.f);

        if(canvas!=null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);
            bg.draw(canvas);
            player.draw(canvas);
            //draw smokepuffs
            for(Smokepuff sp: smoke)
            {
                sp.draw(canvas);
            }
            //draw Borders
            for(Border b: border)
            {
                b.draw(canvas);
            }
            for(Border b: dirt1)
            {
                b.draw(canvas);
            }
            for(Border b: dirt2)
            {
                b.draw(canvas);
            }
            leftButton.draw(canvas);
            rightButton.draw(canvas);
            jumpButton.draw(canvas);
            shootButton.draw(canvas);
            canvas.restoreToCount(savedState);
        }
    }


    public void Updateborder() {
        for(int i = 0; i < border.size(); i++) {
            if(border.get(i).getX() <=1140 && border.get(i+1) == null) {
                border.add(new Border(BitmapFactory.decodeResource(getResources(), R.drawable.grassmedium), WIDTH + 120, 780));
                System.out.println("BORDER GRASS");
            }
            if(border.get(i).getX() < -1) {
                border.remove(i);
                System.out.println("BORDER REMOVE GRASS");
            }
        }
        for(int i = 0; i < dirt1.size(); i++) {
            if(dirt1.get(i).getX() <=1140 && dirt1.get(i + 1) == null) {
                dirt1.add(new Border(BitmapFactory.decodeResource(getResources(),R.drawable.dirtmedium),WIDTH+120,840));
                System.out.println("BORDER DIRT");
            }
            if(dirt1.get(i).getX() < -1) {
                dirt1.remove(i);
                System.out.println("BORDER REMOVE");
            }
        }
        for(int i = 0; i < dirt2.size(); i++) {
            if(dirt2.get(i).getX() <=1140 && dirt2.get(i+1) == null) {
                dirt2.add(new Border(BitmapFactory.decodeResource(getResources(), R.drawable.dirtmedium), WIDTH + 120, 900));
                System.out.println("BORDER DIRT #2");
            }
            if(dirt2.get(i).getX() < -1) {
                dirt2.remove(i);
                System.out.println("BORDER REMOVE DIRT #2");
            }
        }
    }
    public void newGame() {
        border.clear();
        dirt1.clear();
        dirt2.clear();
        smoke.clear();

        player.resetSpeed();
        player.resetScore();
        player.setY(HEIGHT-340);

        //initial border
        for(int i = 0; i*20<WIDTH+60;i++)
        {
            //first top border create
            if(i==0)
            {
                border.add(new Border(BitmapFactory.decodeResource(getResources(),R.drawable.grassmedium),i*60,780));
                dirt1.add(new Border(BitmapFactory.decodeResource(getResources(),R.drawable.dirtmedium),i*60,840));
                dirt2.add(new Border(BitmapFactory.decodeResource(getResources(),R.drawable.dirtmedium),i*60,900));
                System.out.println("STARTBORDER");
            }
            else
            {
                border.add(new Border(BitmapFactory.decodeResource(getResources(),R.drawable.grassmedium),i*60,780));
                dirt1.add(new Border(BitmapFactory.decodeResource(getResources(),R.drawable.dirtmedium),i*60,840));
                dirt2.add(new Border(BitmapFactory.decodeResource(getResources(),R.drawable.dirtmedium),i*60,900));
            }
        }
        newGameCreated = true;
    }


}