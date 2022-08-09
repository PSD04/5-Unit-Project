package com.example.myapp2;


import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.example.myapp2.activities.GameOverActivity;
import com.example.myapp2.graphics.GameDisplay;
import com.example.myapp2.graphics.SpriteSheet;
import com.example.myapp2.object.Coin;
import com.example.myapp2.object.Enemy;
import com.example.myapp2.object.GameObject;
import com.example.myapp2.object.Player;
import com.example.myapp2.object.Spell;
import com.example.myapp2.panel.Joystick;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The SurfaceView which shows the game and handles the main thread (MThread)
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    //display metrics constants:
    private static final int width = Resources.getSystem().getDisplayMetrics().widthPixels;
    private static final float density = Resources.getSystem().getDisplayMetrics().density;

    private MThread thread;             //the main thread which runs the game loop

    private final SharedPreferences prefs = getContext().getSharedPreferences(      //The shared preferences that we use
            "com.example.myapp2", Context.MODE_PRIVATE);                      //to save and pull data

    private final Player player;              //the player object

    private final Joystick joystick;    //the joystick object which we use for player control
    private int joystickPointerId = -1;

    private final Joystick spellJoystick;    //the joystick object which we use for spells
    private int spellJoystickPointerId = -1;

    private final int spellsPerSecond = prefs.getInt("castSpeedLevel",1);       //pulling the castSpeedLevel from the prefs
    private int framesPerSpell = MThread.targetFPS/spellsPerSecond;    //fires two shots per second
    private final int spellSize = width/80;                         //the size of the spell (might change this)


    private int gameTimer = 16
            *MThread.targetFPS; //represents the timer of the current game
                                                  //16 because of the division in the draw() method

    private final List<Enemy> enemyList = new ArrayList<>();//list of the enemy objects which spawn

    private final List<Spell> spellList = new ArrayList<>();//list of all the spells which are used

    private final List<Coin> coinList = new ArrayList<>();//list of all the dropped coins

    private int coinCounter = 0;        //a counter of how many coins did we get in this run

    private int enemyDeathCount = 0;        //a counter of the amount of enemies which the player killed

    private final int lastHighscore = prefs.getInt("highscore",0);    //in order to optimize runtime

    //sound management:
    private final SoundPool soundPool;
    private final int castSound;
    private final int enemyDeathSound;
    private final int playerDamageSound;
    private final int coinPickupSound;

    private final GameDisplay gameDisplay;      //the gameDisplay object which allows us to
                                                //put the player in the center of the screen

    private Typeface textFont = ResourcesCompat.getFont(getContext(), R.font.hackbot);     //the font of the text

    private final Bitmap clockLogo = BitmapFactory.decodeResource(getResources(),R.drawable.clock_logo);    //the clock logo

    private final Bitmap coinLogo = BitmapFactory.decodeResource(getResources(),R.drawable.coin_logo);      //the coin logo



    /**
     * Constructor for the game view
     * @param context - Context for the game view creation
     */
    public GameView(Context context) {
        super(context);

        //Create Main Thread:
        thread = new MThread(getHolder(), this);
        //set DIMENSION,RADIUS and other values:
        Player.DIMENSION =(int)(128*density);
        Player.RADIUS = Player.DIMENSION/2;
        int speedLevel = prefs.getInt("speedLevel",3);
        Player.MAX_SPEED = (int)(speedLevel*density);
        Player.MAX_HEALTH_POINTS = (prefs.getInt("healthLevel",3));

        Coin.DIMENSION = (int)(32*density);
        Coin.RADIUS = Coin.DIMENSION/2;

        Enemy.DIMENSION =(int)(64*density);
        Enemy.RADIUS = Enemy.DIMENSION/2;
        Enemy.MAX_SPEED = (Player.MAX_SPEED-(speedLevel-5)); //might want to change this so the game
                                                            // will be more fun
        //Get audio:
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_GAME).build();
        soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).build();
        if(!prefs.getBoolean("isMute",false)){
            castSound = soundPool.load(context,R.raw.cast_sound,1);
            enemyDeathSound = soundPool.load(context,R.raw.enemy_death_sound,1);
            playerDamageSound = soundPool.load(context,R.raw.player_damage_sound,1);
            coinPickupSound = soundPool.load(context,R.raw.coin_pickup_sound,1);
        }else{
            castSound = -1;
            enemyDeathSound = -1;
            playerDamageSound = -1;
            coinPickupSound = -1;
        }

        //Create panels (user interface graphics):
        joystick = new Joystick((int)(width*0.066),(int)(width*0.04));
        spellJoystick = new Joystick((int)(width*0.066),(int)(width*0.04));

        //initialize bitmaps (spriteSheets and logos):
        Bitmap playerSheet = BitmapFactory.decodeResource(getResources(),
                R.drawable.player_sheet);
        Bitmap coinSheet = BitmapFactory.decodeResource(getResources(),R.drawable.coin_sheet);
        Enemy.enemySprite = BitmapFactory.decodeResource(getResources(),R.drawable.enemy);
        Enemy.enemySprite = Bitmap.createScaledBitmap(Enemy.enemySprite,Enemy.DIMENSION,
                Enemy.DIMENSION,false);


        //set graphics:
        Coin.spriteSheet = new SpriteSheet(coinSheet,Coin.DIMENSION);
        player = new Player(joystick,playerSheet);
        gameDisplay = new GameDisplay(player);

        getHolder().addCallback(this);
        setFocusable(true);

    }


    /**
     * Updates the game view after a surface is created
     * @param holder - The surface holder object which we display on
     */
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        if(thread.getState().equals(Thread.State.TERMINATED)) {
            getHolder().addCallback(this);
            thread = new MThread(getHolder(),this);
        }
        thread.setRunning(true);
        thread.start();
    }

    /**
     * These are mandatory because of the the implement keyword but they are not needed
     */
    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {}
    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {}


    /**
     * Checks for touch input from the user
     * left 1/3 of the screen is the movement stick and the right 1/3 of the screen is the cast stick
     * @param event - The MotionEvent object which is received
     * @return - Returns if the event was handled or not
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(player.getHealthPoints()<=0 || gameTimer<=0){
            return true;
        }
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);
        int maskedAction = event.getActionMasked();
        switch (maskedAction){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                float x = event.getX(pointerIndex);
                float y = event.getY(pointerIndex);
                if(x>width*(2/3.0) && (spellJoystickPointerId == pointerId ||
                        spellJoystickPointerId == -1)){
                    spellJoystick.setBaseCenterPositionX((int) x);
                    spellJoystick.setBaseCenterPositionY((int) y);
                    spellJoystickPointerId = pointerId;
                    spellJoystick.setIsPressed(true);
                    return true;
                }
                if(x<width/3.0 &&(joystickPointerId == pointerId ||
                        joystickPointerId == -1)){
                    joystick.setBaseCenterPositionX((int) x);
                    joystick.setBaseCenterPositionY((int) y);
                    joystickPointerId = pointerId;
                    joystick.setIsPressed(true);
                    return true;
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                for(int size = event.getPointerCount(),i=0;i<size;i++){
                    int tmpPointerId = event.getPointerId(i);
                    if(tmpPointerId == joystickPointerId && joystick.getIsPressed())
                        joystick.setActuators(event.getX(i),event.getY(i));
                    else if(tmpPointerId == spellJoystickPointerId && spellJoystick.getIsPressed()){
                        spellJoystick.setActuators(event.getX(i),event.getY(i));

                    }
                }
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if(spellJoystickPointerId == pointerId){
                    spellJoystick.setIsPressed(false);
                    spellJoystick.resetActuators();
                    spellJoystickPointerId =-1;
                    framesPerSpell = MThread.targetFPS/spellsPerSecond;
                    return true;
                }
                if(joystickPointerId == pointerId){
                    joystick.setIsPressed(false);
                    joystick.resetActuators();
                    joystickPointerId=-1;
                    return true;
                }

        }
        return super.onTouchEvent(event);
    }


    /**
     * You'll never guess what this one does... it pauses the thread that the game is running on
     */
    public void pause() {
        thread.stopThread();
    }

    /**
     * Draws the relevant fields of game view
     * @param canvas - The canvas that the game view object is drawn upon
     */
    @Override
    public void draw(Canvas canvas){
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        //this values might be used for more things in the future.
        super.draw(canvas);
        if(canvas != null){
            canvas.drawColor(Color.argb(180,54, 208, 48));
            player.draw(canvas,gameDisplay);
            for(Enemy enemy:enemyList){
                enemy.draw(canvas,gameDisplay);
            }
            for(Spell spell:spellList){
                spell.draw(canvas,gameDisplay);
            }
            for(Coin coin:coinList){
                coin.draw(canvas,gameDisplay);
            }
            joystick.draw(canvas);
            spellJoystick.draw(canvas);

            Paint paint = new Paint();
            paint.setTypeface(textFont);
            paint.setColor(Color.WHITE);
            paint.setTextSize((float)(width*0.035));
            paint.setTextAlign(Paint.Align.LEFT);
            canvas.drawBitmap(coinLogo
                    ,(float)(width*0.01),(float)(height*0.13),null);
            canvas.drawBitmap(clockLogo
                    ,(float)(width*0.01),(float)(height*0.03),null);
            canvas.drawText(String.valueOf(coinCounter)
                    ,(float)(width*0.06),(float)(height*0.2),paint);
            canvas.drawText("Score:   "+enemyDeathCount*100
                    ,(float)(width*0.4),(float)(height*0.1),paint);
            if(gameTimer/MThread.targetFPS<=5){
                paint.setColor(Color.RED);
            }
            canvas.drawText(String.valueOf(gameTimer/MThread.targetFPS)
                    ,(float)(width*0.06),(float)(height*0.1),paint);


        }


    }

    /**
     * Updates the game view values
     */
    public void update(){
        //Check if the game is over or not (either out of time or out of health):
        if(player.getHealthPoints()<=0 || gameTimer<=0){
            prefs.edit().putInt("coinCounter",
                    prefs.getInt("coinCounter",0)+coinCounter).apply();
            if(enemyDeathCount*100>lastHighscore){
                prefs.edit().putInt("highscore",enemyDeathCount*100).apply();
            }
            prefs.edit().putInt("lastScore",enemyDeathCount*100).apply();
            startActivity(getContext(),new Intent(getContext(), GameOverActivity.class),null);
            return;
        }

        //Tick the clock:
        gameTimer--;

        //Update the Joysticks and the player:
        joystick.update();
        spellJoystick.update();
        player.update();

        //Spawn new enemies and update the enemies
        if(Enemy.readyToSpawn(enemyDeathCount) && enemyList.size()<=20){
            enemyList.add(new Enemy(player));
        }


        //Shoot new spell if condition are met:
        if(spellJoystick.getIsPressed()){
            framesPerSpell--;
            if(framesPerSpell==0 &&
                    !(spellJoystick.getActuatorY() == 0 && spellJoystick.getActuatorX() == 0)){
                spellList.add(new Spell(player,spellSize, spellJoystick.getActuatorX(),
                        spellJoystick.getActuatorY()));
                soundPool.play(castSound,1,1,0,0,1);
                framesPerSpell = MThread.targetFPS/spellsPerSecond;
            }
        }

        //Update spells:
        for(Spell spell:spellList){
            spell.update();
        }

        //Check for enemy collision with player:
        Iterator<Enemy> enemyIterator = enemyList.iterator();
        while(enemyIterator.hasNext()){//might want to split this into two loops to avoid weird stuff
            Enemy tmpEnemy = enemyIterator.next();
            tmpEnemy.update();
            if(GameObject.isColliding(tmpEnemy,player)){
                soundPool.play(playerDamageSound,1,1,0,0,1);
                player.setHealthPoints(player.getHealthPoints()-1);
                enemyIterator.remove();
            }
            //Remove old spells and check for enemy and spell collision:
            Iterator<Spell> spellIterator = spellList.iterator();
            while(spellIterator.hasNext()){
                Spell tmpSpell = spellIterator.next();
                if(tmpSpell.getFramesToLive()<=0){
                    spellIterator.remove();
                }
                else if(GameObject.isColliding(tmpEnemy,tmpSpell)){
                    try{
                        soundPool.play(enemyDeathSound,1,1,0,0,1);
                        enemyIterator.remove();
                        spellIterator.remove();
                    }catch (IllegalStateException ignored){
                        //This cant really fail but it might throw an exception
                        //Just ignoring it for now
                    }
                    enemyDeathCount++;
                    gameTimer+=MThread.targetFPS;
                    //summon a new coin ~30% of the time (and remove old coins if needed):
                    if(Math.random()>0.7){
                        if(coinList.size()>15){
                            coinList.remove(0);
                        }
                        coinList.add(new Coin(tmpEnemy.getPositionX(),
                                tmpEnemy.getPositionY(),Coin.RADIUS));
                    }
                }
            }
        }
        //Check for coin and player collision:
        Iterator<Coin> coinIterator = coinList.iterator();
        while(coinIterator.hasNext()){
            Coin tmpCoin = coinIterator.next();
            tmpCoin.update();
            if(GameObject.isColliding(player,tmpCoin)){
                coinIterator.remove();
                soundPool.play(coinPickupSound,1,1,0,0,1);
                coinCounter++;
            }
        }
        //Update the gameDisplay object:
        gameDisplay.update();   //This HAS to be last
    }

}

