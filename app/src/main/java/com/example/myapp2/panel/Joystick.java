package com.example.myapp2.panel;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.annotation.NonNull;

/**
 * Joystick class which acts as the control for the player and the spells allowing movement of in-game objects.
 */
public class Joystick {
    //fields which represent attributes of the base circle of the joystick
    private int baseCenterPositionX;
    private int baseCenterPositionY;
    private final int baseCircleRadius;
    private final Paint basePaint;

    //fields which represent attributes of the inner (movable) circle of the joystick
    private int innerCenterPositionX;
    private int innerCenterPositionY;
    private final int innerCircleRadius;
    private final Paint innerPaint;

    private boolean isPressed;      //true if the joystick is currently being used, false otherwise

    //normalized values which represent how hard the object is being "pulled"
    private double actuatorX;
    private double actuatorY;

    /**
     * Creates a new Joystick object
     * @param baseCircleRadius - determines the radius of the base joystick circle
     * @param innerCircleRadius - determines the radius of the inner (movable) joystick circle
     */
    public Joystick(int baseCircleRadius,int innerCircleRadius){
        this.baseCircleRadius = baseCircleRadius;
        this.innerCircleRadius = innerCircleRadius;


        basePaint = new Paint();
        basePaint.setColor(Color.LTGRAY);
        basePaint.setAlpha(140);
        basePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        innerPaint = new Paint();
        innerPaint.setColor(Color.GRAY);
        innerPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        actuatorX=0;
        actuatorY=0;
    }



    /**
     * Moves the inner circle to the correct position during player touch.
     */
    private void updateInnerCirclePos() {
        innerCenterPositionX = (int) (baseCenterPositionX+actuatorX*baseCircleRadius);
        innerCenterPositionY = (int) (baseCenterPositionY+actuatorY*baseCircleRadius);
    }

    public void setIsPressed(boolean isPressed) {this.isPressed = isPressed;}
    public boolean getIsPressed() {return isPressed;}

    /**
     * Calculates the actuation for the X and Y axes
     * @param touchPosX - the X value of the touch
     * @param touchPosY - the Y value of the touch
     */
    public void setActuators(double touchPosX, double touchPosY) {
        double deltaX = touchPosX-baseCenterPositionX;
        double deltaY = touchPosY-baseCenterPositionY;
        double distance = Math.sqrt(Math.pow(deltaX,2)+
                Math.pow(deltaY,2));
        if(distance <baseCircleRadius){
            actuatorX = deltaX/baseCircleRadius;
            actuatorY = deltaY/baseCircleRadius;
        }
        else{
            actuatorX = deltaX/distance;
            actuatorY = deltaY/distance;
        }
    }

    /**
     * Setters and getters for the actuators (both X and Y)
     */
    public void resetActuators() {actuatorX = 0;actuatorY = 0;}

    public double getActuatorX() {return actuatorX;}

    public double getActuatorY() {return actuatorY;}

    public void setBaseCenterPositionX(int baseCenterPositionX){
        this.baseCenterPositionX=baseCenterPositionX;
    }

    public void setBaseCenterPositionY(int baseCenterPositionY){
        this.baseCenterPositionY = baseCenterPositionY;
    }


    /**
     * Draws the joystick object at the relevant position
     * @param canvas - The canvas that the joystick object is drawn upon
     */
    public void draw(@NonNull Canvas canvas) {
        if(!isPressed){
            return;
        }
        canvas.drawCircle(baseCenterPositionX,
                baseCenterPositionY,
                baseCircleRadius,
                basePaint);

        canvas.drawCircle(innerCenterPositionX,
                innerCenterPositionY,
                innerCircleRadius,
                innerPaint);
    }

    /**
     * Updates the joystick object's values according to relevant changes.
     */
    public void update() {
        updateInnerCirclePos();
    }

}
