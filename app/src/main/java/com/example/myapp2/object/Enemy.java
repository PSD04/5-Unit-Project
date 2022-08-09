package com.example.myapp2.object;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.myapp2.MThread;

import com.example.myapp2.graphics.GameDisplay;


/**
 * The Enemy class represents an in-game enemy attempts to touch the player and hurt him.
 * He can be shot down and killed for points etc'
 */
public class Enemy extends GameObject {
    public static double MAX_SPEED;     //max enemy speed

    public static Bitmap enemySprite;       //sprite of the default enemy

    public static int DIMENSION;      //The dimension of the enemies

    public static int RADIUS;      //the radius of the enemies

    //Class wide definition for spawning new Enemies:
    private static final int SECONDS_PER_SPAWN = 1;
    private static final int FRAMES_PER_SPAWN = MThread.targetFPS*SECONDS_PER_SPAWN;
    private static final double ENEMY_COUNT_SCALE = 21.0;

    private static int framesUntilSpawn = FRAMES_PER_SPAWN;   //the amount of frames until another
    private static int lastSpawnDirection;
    //enemy spawns

    private final Player player;        //the player object of the game


    /**
     * Constructor for the enemy object which places the enemy at a random location on the screen
     * @param player - The player object of the game
     */
    public Enemy(Player player) {
        super(
                player.getPositionX()+getSpawnDirection()*Resources.
                        getSystem().getDisplayMetrics().widthPixels,
                player.getPositionY()+getSpawnDirection()*Resources.
                        getSystem().getDisplayMetrics().heightPixels,
                RADIUS
        );
        this.player = player;
    }

    /**
     * Helper function for spawning the enemy in a random location
     * @return - return 1,0 or -1 with an equal chance (but it cant return 0 twice in a row)
     */
    private static int getSpawnDirection(){
        double key = Math.random();
        if(key<0.33){
            lastSpawnDirection = -1;
            return -1;
        }
        else if(key<0.66 && lastSpawnDirection !=0){
            lastSpawnDirection = 0;
            return 0;
        }
        else{
            lastSpawnDirection = 1;
            return 1;
        }
    }



    /**
     * Checks if enough frames have passed since the last enemy in order to spawn a new one
     * @return - True if enough frames have passed, false otherwise
     */
    public static boolean readyToSpawn(int enemyDeathCount) {
        if(framesUntilSpawn<=0){
            framesUntilSpawn+=Math.max((FRAMES_PER_SPAWN)/(1+(enemyDeathCount+1)/(ENEMY_COUNT_SCALE))
                    ,0.3*MThread.targetFPS);
            return true;
        }
        else {
            framesUntilSpawn--;
            return false;
        }
    }

    /**
     * Draws the enemy object on the canvas
     * @param canvas - The canvas that the object is drawn upon
     */
    @Override
    public void draw(Canvas canvas, GameDisplay gameDisplay) {
        canvas.drawBitmap(enemySprite,
                gameDisplay.gameToDisplayCoordinatesX(positionX-enemySprite.getWidth()/2.0),
                gameDisplay.gameToDisplayCoordinatesY(positionY-enemySprite.getHeight()/2.0),
                null);
    }

    /**
     * Updates the relevant fields of the enemy object
     */
    @Override
    public void update() {
        double distanceToPlayerX = player.getPositionX()-positionX;
        double distanceToPlayerY = player.getPositionY()-positionY;
        double distanceToPlayer = GameObject.getDistanceBetweenObjects(this,player);
        double directionX;
        double directionY;

        directionX = distanceToPlayerX/distanceToPlayer;
        directionY = distanceToPlayerY/distanceToPlayer;
        velocityX = directionX* MAX_SPEED;
        velocityY = directionY* MAX_SPEED;

        positionX+=velocityX;
        positionY+=velocityY;
    }
}
