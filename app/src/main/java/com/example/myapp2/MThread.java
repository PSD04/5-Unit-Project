package com.example.myapp2;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
//NOT TODAY
public class MThread extends  Thread{
    private final SurfaceHolder surfaceHolder;      //the surface holder (screen) of the game

    private final GameView gameView;    //the GameView object which we play with

    public static final int targetFPS = 60;     //our target FPS and UPS (frames per second and updates per second)

    private boolean running;        //is the thread running or not

    public static Canvas canvas;        //canvas object which we draw on

    /**
     * Constructor for the MThread object
     * @param surfaceHolder - The surface holder object which we display on
     * @param gameView - The GameView object of our game
     */
    public MThread(SurfaceHolder surfaceHolder, GameView gameView){
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
    }

    /**
     * Stops the current thread
     */
    public void stopThread() {
        running = false;
        try {
            join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    /**
     * The lifeblood of the game. this is the while(true) loop which draws and updates the game.
     */
    @Override
    public void run(){
        long startTime,timeMillis,waitTime,targetTime = 1000 / targetFPS;
        int frameCount = 0;

        while(running) {
            startTime = System.nanoTime();
            canvas = null;
            try {
                canvas = this.surfaceHolder.lockCanvas();
                assert(canvas != null);
                synchronized (surfaceHolder) {
                    this.gameView.update();
                    this.gameView.draw(canvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMillis;

            try {
                sleep(waitTime);
            } catch (Exception ignored) {

            }

            frameCount++;
            if (frameCount == targetFPS)        {
                frameCount = 0;
            }

        }
    }

    /**
     * Setter for running
     */
    public void setRunning(boolean running) {
        this.running = running;
    }
}
