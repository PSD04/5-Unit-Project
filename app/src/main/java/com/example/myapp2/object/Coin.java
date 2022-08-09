package com.example.myapp2.object;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.myapp2.graphics.GameDisplay;
import com.example.myapp2.MThread;
import com.example.myapp2.graphics.SpriteSheet;

/**
 * This class represents a coin which has a certain chance to drop from an enemy
 */
public class Coin extends GameObject{


    private Bitmap coinSprite;         //the current sprite of the coin

    public static int DIMENSION;       //scaled during runtime

    public static int RADIUS;          //the radius of the coin

    public static final int MAX_ANIMATION_PHASE = 15;       //amount of images in the animation cycle

    public static SpriteSheet spriteSheet;      //the sprite sheet for the Coin objects

    private int animationPhase = 0;     //the current phase of the animation

    private int animationFrameCounter = MThread.targetFPS/MAX_ANIMATION_PHASE;      //how long each frame is displayed

    /**
     * Constructor for a coin object
     *
     * @param positionX - The X position for the new game object
     * @param positionY - The Y position for the new game object
     * @param hitboxRadius - The radius of the object's hitbox
     */
    public Coin(double positionX, double positionY, double hitboxRadius) {
        super(positionX, positionY, hitboxRadius);
        coinSprite = spriteSheet.getSprite(0,0);
    }

    /**
     * Draws the coin object at the relevant position
     * @param canvas - The canvas that the object is drawn upon
     * @param gameDisplay - the GameDisplay object which places the coin correctly
     */
    @Override
    public void draw(Canvas canvas, GameDisplay gameDisplay) {
        canvas.drawBitmap(coinSprite,
                gameDisplay.gameToDisplayCoordinatesX(positionX-DIMENSION/2.0),
                gameDisplay.gameToDisplayCoordinatesY(positionY-DIMENSION/2.0),
                null);
    }

    /**
     * Updates the coin's values and the animation
     */
    @Override
    public void update() {
        animationFrameCounter--;
        if(animationFrameCounter==0){
            animationPhase = (animationPhase+1)%MAX_ANIMATION_PHASE;
            animationFrameCounter = MThread.targetFPS/MAX_ANIMATION_PHASE;
            coinSprite = spriteSheet.getSprite(0,animationPhase);
        }
    }
}
