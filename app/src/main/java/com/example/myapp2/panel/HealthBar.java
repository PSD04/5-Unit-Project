package com.example.myapp2.panel;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


import com.example.myapp2.graphics.GameDisplay;
import com.example.myapp2.object.Player;

/**
 * The healthBar class represents the health of an object (player or enemy).
 */
public class HealthBar {

    private final Player player;    //the player which the healthbar belongs to

    private final int width;        //width of the healthbar

    private final int height;       //height of the healthbar

    private final int margin;       //margin of the healthbar

    private final Paint borderPaint;    //paint of the healthbar's border

    private final Paint healthPaint;    //paint of the healthbar's current health

    /**
     * Constructor for the healthbar object
     * @param player - the player which "owns" the healthbar
     * @param width - width of the healthbar
     * @param height - height of the healthbar
     * @param margin - margin of the healthbar
     */
    public HealthBar(Player player, int width, int height, int margin){
        this.player = player;
        this.width = width;
        this.height = height;
        this.margin = margin;
        this.borderPaint = new Paint();
        borderPaint.setColor(Color.LTGRAY);
        this.healthPaint = new Paint();
        healthPaint.setColor(Color.GREEN);
    }

    /**
     * Draws the healthbar object on the canvas
     * @param canvas - canvas to draw upon
     * @param gameDisplay - the GameDisplay object which corrects the offset
     */
    public void draw(Canvas canvas, GameDisplay gameDisplay){
        float x = (float) player.getPositionX();
        float y = (float) player.getPositionY();
        float distanceToPlayer = (float)Resources.getSystem().getDisplayMetrics().heightPixels/8;
        float healthPercentage = (float) player.getHealthPoints()/Player.MAX_HEALTH_POINTS;

        float borderLeft,borderTop,borderRight,borderBottom;

        borderLeft = x-(float)width/2;
        borderRight = x+(float)width/2;
        borderBottom = y-distanceToPlayer;
        borderTop = y-height-distanceToPlayer;

        canvas.drawRect(
                gameDisplay.gameToDisplayCoordinatesX(borderLeft),
                gameDisplay.gameToDisplayCoordinatesY(borderTop),
                gameDisplay.gameToDisplayCoordinatesX(borderRight),
                gameDisplay.gameToDisplayCoordinatesY(borderBottom),
                borderPaint);

        float healthLeft,healthTop,healthRight,healthBottom,healthWidth,healthHeight;

        healthWidth = width-2*margin;
        healthHeight = height-2*margin;

        healthLeft = borderLeft+margin;
        healthRight =healthLeft+healthWidth*healthPercentage;
        healthBottom = borderBottom-margin;
        healthTop = healthBottom-healthHeight;

        canvas.drawRect(
                gameDisplay.gameToDisplayCoordinatesX(healthLeft),
                gameDisplay.gameToDisplayCoordinatesY(healthTop),
                gameDisplay.gameToDisplayCoordinatesX(healthRight),
                gameDisplay.gameToDisplayCoordinatesY(healthBottom),
                healthPaint);

    }
}
