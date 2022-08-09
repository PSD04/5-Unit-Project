package com.example.myapp2.graphics;

import android.graphics.Bitmap;

/**
 * The SpriteSheet class represents a sprite sheet of an object and it allows us to
 * do animations and draw different things
 */
public class SpriteSheet {
    private final Bitmap[][] spriteArray;     //an array which gives us fast access to each sprite

    /**
     * A constructor for a spritesheet object
     * @param sheet - The bitmap of the spritesheet
     * @param spriteSize - The size of each block in the sprite sheet (the blocks are square)
     */
    public SpriteSheet(Bitmap sheet,int spriteSize){
        //the bitmap of the sprite sheet
        this.spriteArray = new Bitmap[sheet.getHeight()/spriteSize][sheet.getWidth()/spriteSize];
        for(int row = 0;row<spriteArray.length;row++){
            for(int column = 0;column<spriteArray[0].length;column++){
                spriteArray[row][column] = Bitmap.createBitmap(sheet,spriteSize*column,
                        spriteSize*row,spriteSize,spriteSize);
            }
        }
    }

    /**
     * Returns the sprite at row and column
     * @param row - the row we want
     * @param column - the column we want
     * @return - returns the relevant sprite
     */
    public Bitmap getSprite(int row,int column){
        return spriteArray[row][column];
    }
}
