package com.example.myapp2.object;

import android.graphics.Canvas;

import com.example.myapp2.graphics.GameDisplay;

/**
 * GameObject class represents an abstract game object
 * Parent of Player and Enemy
 */
public abstract class GameObject {
    protected double positionX;             //X position of the game object

    protected double positionY;             //Y position of the game object

    protected double hitboxRadius;          //radius for the hitbox (last comment wasn't family friendly
                                            //but it was absolutely hilarious

    protected double velocityX;             //X axis velocity of the game object

    protected double velocityY;             //Y axis velocity of the game object

    /**
     * Constructor for a game object
     * @param positionX - The X position for the new game object
     * @param positionY - The Y position for the new game object
     * @param hitboxRadius - The radius of the object's hitbox
     */
    public GameObject(double positionX,double positionY,double hitboxRadius){
        this.positionX = positionX;
        this.positionY = positionY;
        this.hitboxRadius=hitboxRadius;
    }


    /**
     * Getters and setters for the position and velocity
     */

    public double getPositionX() {
        return positionX;
    }
    public double getPositionY() {
        return positionY;
    }
    public double getVelocityX() {
        return velocityX;
    }
    public double getVelocityY() {
        return velocityY;
    }

    /**
     * calculates the distance between 2 game objects
     * @param gameObject1 - The first object
     * @param gameObject2 - The second object
     * @return - the distance between the two objects
     */
    protected static double getDistanceBetweenObjects(GameObject gameObject1
            ,GameObject gameObject2){
        double deltaX = gameObject1.getPositionX()-gameObject2.getPositionX();
        double deltaY = gameObject1.getPositionY()-gameObject2.getPositionY();

        return (Math.sqrt(Math.pow(deltaX,2)+Math.pow(deltaY,2)));

    }

    /**
     * Checks for collision between objects
     * @param gameObject1 - The first game object
     * @param gameObject2 - The second game object
     * @return - true if gameObject1 is colliding with gameObject2, false otherwise
     */
    public static boolean isColliding(GameObject gameObject1,GameObject gameObject2) {
        return getDistanceBetweenObjects(gameObject1, gameObject2) <
                gameObject1.hitboxRadius + gameObject2.hitboxRadius;
    }


    /**
     * Abstract function for drawing the object
     * @param canvas - The canvas that the object is drawn upon
     */
    public abstract void draw(Canvas canvas, GameDisplay gameDisplay);

    /**
     * Abstract function for updating the object's values
     */
    public abstract void update();


}
