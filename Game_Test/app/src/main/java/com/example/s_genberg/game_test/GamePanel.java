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
        leftButton = new ImageButton(getContext());
        rightButton = new ImageButton(getContext());
        jumpButton = new ImageButton(getContext());
        shootButton = new ImageButton(getContext());
        leftButton.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.left));
        rightButton.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.right));
        jumpButton.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.jump));
        shootButton.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.shoot));

        leftButton.setX(HEIGHT/2);

        //leftButton.setX(HEIGHT-10);
        rightButton.setX(HEIGHT-10);
        jumpButton.setX(HEIGHT-10);
        shootButton.setX(HEIGHT-10);

        leftButton.setY(WIDTH/2);

        //leftButton.setY(2);
        rightButton.setY(400);
        jumpButton.setY(WIDTH-10);
        shootButton.setY(WIDTH-400);

        smokeStartTime=  System.nanoTime();
        missileStartTime = System.nanoTime();

        //we can safely start the game loop
        thread.setRunning(true);
        thread.start();

    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            if(!player.getPlaying())
            {
                player.setPlaying(true);
                player.setUp(true);
            }
            else
            {
                player.setUp(true);
            }
            return true;
        }
        if(event.getAction()==MotionEvent.ACTION_UP)
        {
            player.setUp(false);
            return true;
        }

        return super.onTouchEvent(event);
    }

    public void update()

    {
        if(player.getPlaying()) {

            bg.update();
            player.update();

            //calculate the threshold of height the border can have based on the score
            //max and min border heart are updated, and the border switched direction when either max or
            //min is met



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
            canvas.restoreToCount(savedState);
        }
    }


    public void Updateborder() {
        for(Border b : border) {
            if(b.getX() <= (WIDTH+30)) {
                border.add(new Border(BitmapFactory.decodeResource(getResources(),R.drawable.grass),WIDTH+60,930));
            }
        }
        for(int i = 0; i < border.size(); i++) {
            if(border.get(i).getX() < -1) {
                border.remove(i);
            }
        }
    }
    public void newGame() {
        border.clear();
        smoke.clear();

        player.resetDY();
        player.resetScore();
        player.setY(HEIGHT-190);

        //initial border
        for(int i = 0; i*20<WIDTH+60;i++)
        {
            //first top border create
            if(i==0)
            {
                border.add(new Border(BitmapFactory.decodeResource(getResources(),R.drawable.grass),i*30,930));
            }
            else
            {
                border.add(new Border(BitmapFactory.decodeResource(getResources(),R.drawable.grass),i*30,930));
            }
        }
        newGameCreated = true;
    }


}