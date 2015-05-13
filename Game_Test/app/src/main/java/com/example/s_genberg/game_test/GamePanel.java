package com.example.s_genberg.game_test;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    public static final int WIDTH = 856;
    public static int HEIGHT = 480;
    private MainThread thread;
    private Background bg;
    public GamePanel(Context context) {
        super(context);

        //add call back to sufrace BUttons pressessge
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);

        //make game panel focusable
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    //
        boolean retry = true;
        while(retry) {
            try{thread.setRunning(false);
            thread.join();
            }catch(Exception e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.grassbg1));
        bg.setVector(-5);
    // if created safe start game loop
        thread.setRunning(true);
        thread.start();

    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
    public void update() {
    bg.update();
    }
    @Override
    public void draw(Canvas canvas) {
    bg.draw(canvas);
    }
}
