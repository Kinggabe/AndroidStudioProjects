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
    private ImageButton leftButton;
    private ImageButton rightButton;
    private ImageButton jumpButton;
    private ImageButton shootButton;
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
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.mainchar), 90, 160, 3);
        smoke = new ArrayList<Smokepuff>();
        border = new ArrayList<Border>();
        dirt1 = new ArrayList<Border>();
        dirt2 = new ArrayList<Border>();
        leftButton = new ImageButton(getContext());
        rightButton = new ImageButton(getContext());
        jumpButton = new ImageButton(getContext());
        shootButton = new ImageButton(getContext());
        leftButton.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.left));
        rightButton.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.right));
        jumpButton.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.jump));
        shootButton.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.shoot));



        leftButton.setX(HEIGHT - 10);
        rightButton.setX(HEIGHT - 10);
        jumpButton.setX(HEIGHT - 10);
        shootButton.setX(HEIGHT - 10);

        leftButton.setY(2);
        rightButton.setY(400);
        jumpButton.setY(WIDTH - 10);
        shootButton.setY(WIDTH - 400);

        leftButton.setEnabled(true);
        rightButton.setEnabled(true);
        jumpButton.setEnabled(true);
        shootButton.setEnabled(true);

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
            System.out.println("CLICK!!");
            if(!player.getPlaying()) {
                player.setPlaying(true);
                player.setUp(true);
            }
            else {
                player.setUp(true);
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
            if(player.getY() == 90) {
                player.setDownSpeed();
            }

            //BORDERS AKA Ground !! !! !!  !!  !!  !!  !!  !!  !! !!
            this.Updateborder();

            //add smoke puffs on timer
            long elapsed = (System.nanoTime() - smokeStartTime)/1000000;
            if(elapsed > 120){
                smoke.add(new Smokepuff(player.getX(), player.getY()+10));
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
            leftButton.draw(canvas);
            rightButton.draw(canvas);
            jumpButton.draw(canvas);
            shootButton.draw(canvas);
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
            canvas.restoreToCount(savedState);
        }
    }


    public void Updateborder() {
        /*
        for(Border b : border) {
            if(b.getX() <= 1020 && b.getX() >= WIDTH) {
                border.add(new Border(BitmapFactory.decodeResource(getResources(), R.drawable.grass), WIDTH + 60, 870));
                System.out.println("BORDER GRASS");
            }
        }
        */
        for(int i = 0; i < border.size(); i++) {
            if(border.get(i).getX() <=1020 && border.get(i+1) == null) {
                border.add(new Border(BitmapFactory.decodeResource(getResources(), R.drawable.grass), WIDTH + 60, 870));
                System.out.println("BORDER GRASS");
            }
            if(border.get(i).getX() < -1) {
                border.remove(i);
                System.out.println("BORDER REMOVE GRASS");
            }
        }

        for(int i = 0; i < dirt1.size(); i++) {
            if(dirt1.get(i).getX() <=1020 && dirt1.get(i + 1) == null) {
                dirt1.add(new Border(BitmapFactory.decodeResource(getResources(),R.drawable.dirt),WIDTH+60,900));
                System.out.println("BORDER DIRT");
            }
            if(dirt1.get(i).getX() < -1) {
                dirt1.remove(i);
                System.out.println("BORDER REMOVE");
            }
        }

        for(int i = 0; i < dirt2.size(); i++) {
            if(dirt2.get(i).getX() <=1020 && dirt2.get(i+1) == null) {
                dirt2.add(new Border(BitmapFactory.decodeResource(getResources(), R.drawable.dirt), WIDTH + 60, 930));
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

        player.resetDY();
        player.resetScore();
        player.setY(HEIGHT-320);

        //initial border
        for(int i = 0; i*20<WIDTH+60;i++)
        {
            //first top border create
            if(i==0)
            {
                border.add(new Border(BitmapFactory.decodeResource(getResources(),R.drawable.grass),i*30,870));
                dirt1.add(new Border(BitmapFactory.decodeResource(getResources(),R.drawable.dirt),i*30,900));
                dirt2.add(new Border(BitmapFactory.decodeResource(getResources(),R.drawable.dirt),i*30,930));
                System.out.println("STARTBORDER");
            }
            else
            {
                border.add(new Border(BitmapFactory.decodeResource(getResources(),R.drawable.grass),i*30,870));
                dirt1.add(new Border(BitmapFactory.decodeResource(getResources(),R.drawable.dirt),i*30,900));
                dirt2.add(new Border(BitmapFactory.decodeResource(getResources(),R.drawable.dirt),i*30,930));
            }
        }
        newGameCreated = true;
    }


}