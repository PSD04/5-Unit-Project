package com.example.myapp2.object;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.myapp2.MThread;
import com.example.myapp2.graphics.GameDisplay;

/**
 * The spell class extends the GamObject class and represents a spell casted by the player.
 * If it collides with an enemy it kills the enemy and disappears
 */
public class Spell extends GameObject {

    public static final int MAX_SPEED = 15;         //Max speed of the spell

    private final int secondsToLive = 2;          //lifetime length of spells

    private int framesToLive = MThread.targetFPS*secondsToLive;     //lifetime in frames

    private final double directionX;              //X direction of the shot

    private final double directionY;              //Y direction of the shot

    private final Paint spellPaint;          //paint of the spell


    /**
     * Constructor for a game object of type Spell
     * @param player - The player object of the game
     * @param hitboxRadius - The hitbox radius of the spell
     */
    public Spell(Player player, double hitboxRadius,
                 double directionX,double directionY) {
        super(player.getPositionX(),player.getPositionY(), hitboxRadius);
        this.directionX = directionX;
        this.directionY = directionY;


        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.RED);
        this.spellPaint = paint;
    }


    /**
     * Draws the spell object on the canvas
     * @param canvas - The canvas that the object is drawn upon
     * @param gameDisplay - The GameDisplay object which calculates the offset
     */
    @Override
    public void draw(Canvas canvas, GameDisplay gameDisplay) {
        canvas.drawCircle(
                gameDisplay.gameToDisplayCoordinatesX(positionX),
                gameDisplay.gameToDisplayCoordinatesY(positionY),
                (float) hitboxRadius,
                spellPaint
        );
    }

    /**
     * Updates the relevant fields of the spell object
     */
    @Override
    public void update() {
        velocityX = directionX*MAX_SPEED;
        velocityY = directionY*MAX_SPEED;
        positionX += velocityX;
        positionY += velocityY;
        framesToLive--;
    }

    /**
     * Getter for framesToLive
     */
    public int getFramesToLive() {
        return framesToLive;
    }
}
