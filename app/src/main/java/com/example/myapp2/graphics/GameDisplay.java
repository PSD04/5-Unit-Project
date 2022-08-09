package com.example.myapp2.graphics;

import android.content.res.Resources;

import com.example.myapp2.object.GameObject;

/**
 * The GameDisplay object calculates the offset which is needed in order to keep the player in
 * the center. It has a lot of math stuff but it actually isn't that scary.
 */
public class GameDisplay {

    private double displayOffsetX;      //the current offset of the X axis

    private double displayOffsetY;      //the current offset of the Y axis

    private final double displayCenterX;        //X position of the center of the screen

    private final double displayCenterY;        //Y position of the center of the screen

    private final GameObject centerObject;    //The object which we want to center

    /**
     * Constructor for the GameDisplay object
     * @param centerObject - The object which we want to put in the center
     */
    public GameDisplay(GameObject centerObject){
        this.centerObject = centerObject;

        displayCenterX = Resources.getSystem().getDisplayMetrics().widthPixels/2.0;
        displayCenterY = Resources.getSystem().getDisplayMetrics().heightPixels/2.0;
    }

    /**
     * calculates the offset of the X axis
     * @param x - Point to calculate
     * @return - The result of the calculation
     */
    public float gameToDisplayCoordinatesX(double x) {
        return (float) (x+displayOffsetX);
    }

    /**
     * calculates the offset of the Y axis
     * @param y - Point to calculate
     * @return - The result of the calculation
     */
    public float gameToDisplayCoordinatesY(double y) {
        return (float) (y+displayOffsetY);
    }

    /**
     * Updates relevant fields of the GameDisplay object
     */
    public void update(){
        //the X position of the object which we want to center
        double gameCenterX = centerObject.getPositionX();
        //the Y position of the object which we want to center
        double gameCenterY = centerObject.getPositionY();

        displayOffsetX = displayCenterX- gameCenterX;
        displayOffsetY = displayCenterY- gameCenterY;
    }
}
