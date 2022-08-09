package com.example.myapp2.object;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import androidx.annotation.NonNull;

import com.example.myapp2.MThread;
import com.example.myapp2.panel.HealthBar;
import com.example.myapp2.graphics.SpriteSheet;
import com.example.myapp2.graphics.GameDisplay;
import com.example.myapp2.panel.Joystick;

/**
 * The SpriteDirection allows us to choose the correct sprite
 */
enum SpriteDirection {
    Down,
    Left,
    Right,
    Up
}


/**
 * The Player is the main character of the game which is being controlled by a joystick.
 * It inherits from the GameObject class.
 */
public class Player extends GameObject{

    public static int DIMENSION;     //dimension of the sprite

    public static int RADIUS = 80;     //the radius of the hitbox


    public static int MAX_SPEED = 5;        //maximum speed of the player object's movement

    public static int MAX_HEALTH_POINTS;     //maximum health of the player

    public static final int MAX_ANIMATION_PHASE = 4;    //the amount of frames in the player's animation

    private final SpriteSheet spriteSheet;          //the spritesheet of the player

    private Bitmap playerSprite;                   //the current sprite of the player object

    private final Joystick joystick;              //the joystick object used to control the movement

    private final HealthBar healthBar;          //healthbar of the player

    private int healthPoints;               //current health of the player



    private int animationPhase = 0;         //in which part of the animation are we (0-4)

    private int animationFrameCounter = MThread.targetFPS/MAX_ANIMATION_PHASE;     //display time of each frame;

    private SpriteDirection spriteDirection = SpriteDirection.Down;     //the direction of the walk cycle, default is down



    /**
     * Creates a new player object
     * @param joystick - The joystick which controls the player object
     * @param spriteSheetBmap - A bitmap of the player's sprite sheet
     */
    public Player(Joystick joystick,Bitmap spriteSheetBmap){
        super(0,0,RADIUS/3.0);         //tmp values,yet to be changed
        this.joystick = joystick;
        this.healthBar = new HealthBar(this,100,20,2);//might want to scale this
        this.healthPoints = MAX_HEALTH_POINTS;
        this.spriteSheet = new SpriteSheet(spriteSheetBmap,DIMENSION);
        this.playerSprite = spriteSheet.getSprite(0,0);
    }

    /**
     * Getter and setter for the health
     */
    public int getHealthPoints() {
        return healthPoints;
    }
    public void setHealthPoints(int healthPoints) {
        if(healthPoints>=0) {
            this.healthPoints = healthPoints;
        }
    }

    /**
     * Draws the player object at the relevant position
     * @param canvas - The canvas that the player object is drawn upon
     */
    @Override
    public void draw(@NonNull Canvas canvas, GameDisplay gameDisplay){
        canvas.drawBitmap(playerSprite,
                gameDisplay.gameToDisplayCoordinatesX(positionX-playerSprite.getWidth()/2.0),
                gameDisplay.gameToDisplayCoordinatesY(positionY-playerSprite.getHeight()/2.0),
                null);
        healthBar.draw(canvas,gameDisplay);

    }

    /**
     * Updates the player object's velocity according to the joystick's actuator
     * and moves the player accordingly
     */
    public void update() {
        if(!joystick.getIsPressed()){
            animationPhase = 0;
            spriteDirection = SpriteDirection.Down;
            playerSprite = spriteSheet.getSprite(spriteDirection.ordinal(),animationPhase);
            return;
        }
        double directionX = joystick.getActuatorX();
        double directionY = joystick.getActuatorY();
        if(Math.abs(directionX)>=Math.abs(directionY)){
            if(directionX>=0){
                spriteDirection = SpriteDirection.Right;
            }
            else{
                spriteDirection = SpriteDirection.Left;
            }
        }
        else{
            if(directionY>=0) {
                spriteDirection = SpriteDirection.Down;
            }
            else{
                spriteDirection = SpriteDirection.Up;
            }
        }
        velocityX = joystick.getActuatorX()*MAX_SPEED;
        velocityY = joystick.getActuatorY()*MAX_SPEED;
        positionX += velocityX;
        positionY += velocityY;
        animationFrameCounter--;
        if(animationFrameCounter==0){
            animationPhase = (animationPhase+1)%MAX_ANIMATION_PHASE;
            animationFrameCounter = MThread.targetFPS/MAX_ANIMATION_PHASE;
            playerSprite = spriteSheet.getSprite(spriteDirection.ordinal(),animationPhase);
        }
    }

}
