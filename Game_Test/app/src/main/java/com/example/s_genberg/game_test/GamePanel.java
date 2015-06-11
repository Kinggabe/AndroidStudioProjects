package com.example.s_genberg.game_test;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    public static final int WIDTH = 1712;
    public static final int HEIGHT = 960;
    public static int MOVESPEED = -5;
    public static final int PLAYER_SPAWN = HEIGHT - 90;
    private int shootingStartTime;
    private MainThread thread;
    private Background bg;
    private Player player;
    private BetterButton leftButton;
    private BetterButton rightButton;
    private BetterButton jumpButton;
    private BetterButton shootButton;
    private ArrayList<Border> border;
    private ArrayList<Border> dirt2;
    private ArrayList<Border> dirt1;
    private ArrayList<EnemyBase> enemies;
    private Bitmap[] healthbar;
    private Random rand = new Random();
    private boolean newGameCreated;
    //increase to slow down difficulty progression, decrease to speed up difficulty progression
    private int progressDenom = 20;


    public GamePanel(Context context) {
        super(context);

        //add the callback to the surfaceholder to intercept events
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        //make gamePanel focusable so it can handle events
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        int counter = 0;
        while (retry && counter < 1000) {
            counter++;
            try {
                thread.setRunning(false);
                thread.join();
                retry = false;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        healthbar = new Bitmap[11];
        healthbar[0] = BitmapFactory.decodeResource(getResources(), R.drawable.healthbar11);
        healthbar[1] = BitmapFactory.decodeResource(getResources(), R.drawable.healthbar10);
        healthbar[2] = BitmapFactory.decodeResource(getResources(), R.drawable.healthbar9);
        healthbar[3] = BitmapFactory.decodeResource(getResources(), R.drawable.healthbar8);
        healthbar[4] = BitmapFactory.decodeResource(getResources(), R.drawable.healthbar7);
        healthbar[5] = BitmapFactory.decodeResource(getResources(), R.drawable.healthbar6);
        healthbar[6] = BitmapFactory.decodeResource(getResources(), R.drawable.healthbar5);
        healthbar[7] = BitmapFactory.decodeResource(getResources(), R.drawable.healthbar4);
        healthbar[8] = BitmapFactory.decodeResource(getResources(), R.drawable.healthbar3);
        healthbar[9] = BitmapFactory.decodeResource(getResources(), R.drawable.healthbar2);
        healthbar[10] = BitmapFactory.decodeResource(getResources(), R.drawable.healthbar);
        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.mainbackground));
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.cow_main ), 162, 220, 6,
                BitmapFactory.decodeResource(getResources(), R.drawable.bullet), BitmapFactory.decodeResource(getResources(), R.drawable.bullethit), healthbar);
        enemies = new ArrayList<EnemyBase>();

        border = new ArrayList<Border>();
        dirt1 = new ArrayList<Border>();
        dirt2 = new ArrayList<Border>();
        leftButton = new BetterButton(BitmapFactory.decodeResource(getResources(), R.drawable.left), BitmapFactory.decodeResource(getResources(), R.drawable.leftpress), 300, 150, 1, 10, 800);
        rightButton = new BetterButton(BitmapFactory.decodeResource(getResources(), R.drawable.right), BitmapFactory.decodeResource(getResources(), R.drawable.rightpress), 300, 150, 1, 350, 800);
        jumpButton = new BetterButton(BitmapFactory.decodeResource(getResources(), R.drawable.jump), BitmapFactory.decodeResource(getResources(), R.drawable.jumppress), 300, 150, 1, 1400, 800);
        shootButton = new BetterButton(BitmapFactory.decodeResource(getResources(), R.drawable.shoot), BitmapFactory.decodeResource(getResources(), R.drawable.shootpress), 200, 200, 1, 1480, 550);

        shootingStartTime = 1;

        //we can safely start the game loop
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        System.out.println("EVENT TOUCH ISH THING");
        handleTouch(event);
        return CheckCords(event);
    }

    public void update() {
        if (player.getPlaying()) {
            bg.update();
            player.update();
            leftButton.update();
            rightButton.update();
            jumpButton.update();
            shootButton.update();

            if(player.getEnemy()) {
                   enemies.add(new EnemyBase(BitmapFactory.decodeResource(getResources(), R.drawable.enemies_main), 162, 220, 6,//alter 1 later
                           BitmapFactory.decodeResource(getResources(), R.drawable.enemybullet), BitmapFactory.decodeResource(getResources(), R.drawable.bullethit)));
                System.out.println("ENEMY");
                player.setEnemy(false);
            }
            for (EnemyBase e : enemies) {
                e.update();
            }
            for(int i = 0; i < enemies.size(); i++) {
                for(int j = 0; j < player.Arraybullets.size(); j++) {
                    if(collision(player.Arraybullets.get(i), enemies.get(i))) {
                        player.Arraybullets.remove(i);
                        enemies.get(i).damaged();
                        if(enemies.get(i).getHeath() == 0) {
                            enemies.remove(i);
                        }
                                          }
                }
                for(enemy_bullets b : enemies.get(i).Arraybullets) {
                    if(collision(b, player)) {
                        player.damaged();
                    }
                }
                if(collision(player, enemies.get(i))) {
                    player.damaged();
                }
            }
            if(player.getHealth() == 0) {
                player.setPlaying(false);
            }
            player.right = true;
            /*
            if (leftButton.getPress()) {
                player.left = true;
            } else {
                player.left = false;
            }
            if (rightButton.getPress()) {
                player.right = true;
            } else {
                player.right = false;
            }*/
            if (jumpButton.getPress()) {
                if (player.getY() == (HEIGHT - 400)) {
                    player.jumping = true;
                }
            }
            if (shootButton.getPress()) {
                if (shootingStartTime == 1) {
                    player.shooting = true;
                    shootingStartTime++;
                } else {
                    shootingStartTime++;
                    if (shootingStartTime == 10) {
                        shootingStartTime = 1;
                    }
                }
            } else {
                player.shooting = false;
                shootingStartTime = 1;
            }
            //BORDERS AKA Ground !! !! !!  !!  !!  !!  !!  !!  !! !!
            this.Updateborder();
        } else {
            player.right = false;
            newGameCreated = false;
            if (!newGameCreated) {
                newGame();
            }
        }
    }

    public boolean collision(GameObject a, GameObject b) {
        if (Rect.intersects(a.getRectangle(), b.getRectangle())) {
            return true;
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        final float scaleFactorX = getWidth() / (WIDTH * 1.f);
        final float scaleFactorY = getHeight() / (HEIGHT * 1.f);

        if (canvas != null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);
            bg.draw(canvas);
            //draw Borders
            for (Border b : border) {
                b.draw(canvas);
            }
            for (Border b : dirt1) {
                b.draw(canvas);
            }
            for (Border b : dirt2) {
                b.draw(canvas);
            }
           // leftButton.draw(canvas);
           // rightButton.draw(canvas);
            jumpButton.draw(canvas);
            shootButton.draw(canvas);
            for (EnemyBase e : enemies) {
                e.draw(canvas);
            }
            player.draw(canvas);
            canvas.restoreToCount(savedState);
        }

    }

    public void Updateborder() {
        //TOP GRASS
        for (int i = 0; i < border.size() - 1; i++) {
            if (border.get(i).getX() < -120) {
                border.remove(i);
            }
        }
        if (border.get(border.size() - 1).getX() <= 1832) {
            border.add(new Border(BitmapFactory.decodeResource(getResources(), R.drawable.grassmedium), border.get(border.size() - 1).getX() + 60, 780));
        }
        //MID DIRT
        for (int i = 0; i < dirt1.size() - 1; i++) {
            if (dirt1.get(i).getX() <= 1140 && dirt1.get(i + 1) == null) {
                dirt1.add(new Border(BitmapFactory.decodeResource(getResources(), R.drawable.dirtmedium), WIDTH + 120, 840));
            }
            if (dirt1.get(i).getX() < -120) {
                dirt1.remove(i);
            }
        }
        if (dirt1.get(dirt1.size() - 1).getX() <= 1832) {
            dirt1.add(new Border(BitmapFactory.decodeResource(getResources(), R.drawable.dirtmedium), dirt1.get(dirt1.size() - 1).getX() + 60, 840));
        }
        //BOTTOM DIRT
        for (int i = 0; i < dirt2.size() - 1; i++) {
            if (dirt2.get(i).getX() <= 1140 && dirt2.get(i + 1) == null) {
                dirt2.add(new Border(BitmapFactory.decodeResource(getResources(), R.drawable.dirtmedium), WIDTH + 120, 900));
            }
            if (dirt2.get(i).getX() < -120) {
                dirt2.remove(i);
            }
        }
        if (dirt2.get(dirt2.size() - 1).getX() <= 1832) {
            dirt2.add(new Border(BitmapFactory.decodeResource(getResources(), R.drawable.dirtmedium), dirt2.get(dirt2.size() - 1).getX() + 60, 900));
        }

        for (Border b : border) {
            b.update();
        }
        for (Border b : dirt1) {
            b.update();
        }
        for (Border b : dirt2) {
            b.update();
        }
    }

    public void newGame() {
        border.clear();
        dirt1.clear();
        dirt2.clear();

        player.resetSpeed();
        player.resetScore();
        player.setY(HEIGHT - 400);

        //initial border
        for (int i = 0; i * 20 < WIDTH + 60; i++) {
            //first top border create
            if (i == 0) {
                border.add(new Border(BitmapFactory.decodeResource(getResources(), R.drawable.grassmedium), i * 60, 780));
                dirt1.add(new Border(BitmapFactory.decodeResource(getResources(), R.drawable.dirtmedium), i * 60, 840));
                dirt2.add(new Border(BitmapFactory.decodeResource(getResources(), R.drawable.dirtmedium), i * 60, 900));
                System.out.println("STARTBORDER");
            } else {
                border.add(new Border(BitmapFactory.decodeResource(getResources(), R.drawable.grassmedium), i * 60, 780));
                dirt1.add(new Border(BitmapFactory.decodeResource(getResources(), R.drawable.dirtmedium), i * 60, 840));
                dirt2.add(new Border(BitmapFactory.decodeResource(getResources(), R.drawable.dirtmedium), i * 60, 900));
            }
        }
        newGameCreated = true;
    }

    public boolean CheckCords(MotionEvent event) {
        if (!player.getPlaying()) {
            player.setPlaying(true);
        }
        leftButton.setPressed(false);
        rightButton.setPressed(false);
        shootButton.setPressed(false);
        jumpButton.setPressed(false);
        int[] KKK = handleTouch(event);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (KKK[2] > 0 && KKK[2] < 300) {
                if (KKK[3] > 850) {
                    leftButton.setPressed(true);
                }
            }
            if (KKK[2] > 350 && KKK[2] < 650) {
                if (KKK[3] > 850) {
                    rightButton.setPressed(true);
                }
            }
            if (KKK[2] > 1555) {
                if (KKK[3] > 850) {
                    jumpButton.setPressed(true);
                }
            }
            if (KKK[2] > 1630) {
                if (KKK[3] < 800 && KKK[3] > 570) {
                    shootButton.setPressed(true);
                }
            }
            return true;
        } else if(event.getAction() == MotionEvent.ACTION_UP) {
            if (KKK[2] > 0 && KKK[2] < 300) {
                if (KKK[3] > 850) {
                    leftButton.setPressed(true);
                }
            }
            if (KKK[2] > 350 && KKK[2] < 650) {
                if (KKK[3] > 850) {
                    rightButton.setPressed(true);
                }
            }
            if (KKK[2] > 1555) {
                if (KKK[3] > 850) {
                    jumpButton.setPressed(true);
                }
            }
            if (KKK[2] > 1630) {
                if (KKK[3] < 800 && KKK[3] > 570) {
                    shootButton.setPressed(true);
                }
            }
            return true;
        }
        else  {
            return super.onTouchEvent(event);
        }
    }

    int[] handleTouch(MotionEvent m) {
        int pointerCount = m.getPointerCount();

        for (int i = 0; i < pointerCount; i++) {
            int x = (int) m.getX(i);
            int y = (int) m.getY(i);
            int id = m.getPointerId(i);
            int action = m.getActionMasked();
            int actionIndex = m.getActionIndex();
            String actionString;


            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    actionString = "DOWN";
                    break;
                case MotionEvent.ACTION_UP:
                    actionString = "UP";
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    actionString = "PNTR DOWN";
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    actionString = "PNTR UP";
                    break;
                case MotionEvent.ACTION_MOVE:
                    actionString = "MOVE";
                    break;
                default:
                    actionString = "";
            }

            String touchStatus = "Action: " + actionString + " Index: " + actionIndex + " ID: " + id + " X: " + x + " Y: " + y;
            int[] touch = new int[5];
            touch[0] = actionIndex;
            touch[1] = id;
            touch[2] = x;
            touch[3] = y;
            if (actionString.equals("")) {
                touch[4] = 0;
            } else {
                touch[4] = 1;
            }
            if (id == 0) {
                System.out.println("ID 0:" + touchStatus);
                return touch;
            } else {
                System.out.println("ID 1:" + touchStatus);
                return touch;
            }

        }
        int[] touch = new int[5];
        touch[4] = 0;
        return touch;
    }
}
