package utils;

//This class file has a lot of helpful methods that can be used throughout the main game files to help make coding the game simpler

import main.Game;
import objects.*;

import java.awt.geom.Rectangle2D;

public class HelpMethods {

//This method checks all corners of players hitbox to see if it is touching a solid part of the level
//And checks to see if the player is allowed to move there, otherwise causes collision
//hitbox is in float values so this method takes float values of the hitbox instead of player
    public static boolean canMoveHere(float x, float y, float width, float height, int[][] lvlData){

        if(!IsSolid(x, y, lvlData)){ //Checks if top left corner of hitbox is not at a coordinate where a solid tile is located
            if(!IsSolid(x + width, y + height, lvlData)){ //Checks bottom right corner of hitbox next
                if(!IsSolid(x + width, y, lvlData)){ //Checks top right corner next
                    if(!IsSolid(x, y + height, lvlData)){ //Finaly, checks bottom left corner
                        return true; //If all isSold methods return false, passing through the if conditions,
                                    //Then return true beause that means player can move here
                    }
                }
            }
        }

        return false;

    }

    private static boolean IsSolid(float x, float y, int[][] lvlData){ //Checks first if tile is in bounds of level, then checks if its solid
        int maxWidth = lvlData[0].length * Game.TILES_SIZE;//maxWidth of entire level
        if(x < 0 || x >= maxWidth){ //If x (height) is less than 0 OR greater than width of game, return that this coordinate is solid, so you cannot move here
            return true;
        }
        if(y < 0 || y >= Game.GAME_HEIGHT){ //If y (height) is less than 0 OR greater than height of game, return that this coordinate is solid, so player cannot move here
            return true;
        }
        //If neither of these if conditions return anything, that means that coordinate within bounds of the level
        //So we must check where exactly these coordinates are in the level using lvlDatat

        float xIndex = x /Game.TILES_SIZE;
        float yIndex = y / Game.TILES_SIZE;

        return IsTileSolid((int)xIndex, (int)yIndex, lvlData); //checks if tile is solid

    }

    //Checks if projectile is hitting a solid tile in the level
    public static boolean IsProjectileHittingLevel(Projectile p, int[][] lvlData){
        return IsSolid(p.getHitbox().x + p.getHitbox().width/2, p.getHitbox().y + p.getHitbox().height/2, lvlData);
    }

    //checks if golem projectiles are hitting a solid level tile
    public static boolean IsProjectileHittingLevel(GolemProjectile p, int[][] lvlData){
        return IsSolid(p.getHitbox().x + p.getHitbox().width/2, p.getHitbox().y + p.getHitbox().height/2, lvlData);
    }

    public static boolean IsEntityInWater(Rectangle2D.Float hitbox, int[][] lvlData) {
        // Will only check if entity touch top water. Can't reach bottom water if not
        // touched top water.
        if (GetTileValue(hitbox.x, hitbox.y + hitbox.height + 1, lvlData) != 48) {
            if (GetTileValue(hitbox.x + hitbox.width, hitbox.y + hitbox.height, lvlData) != 48) {
                return false;
            }
        }
        return true;
    }

    //gets value of a specific tile at a specific x and y coordinate
    private static int GetTileValue(float xPos, float yPos, int[][] lvlData) {
        int xCord = (int) (xPos / Game.TILES_SIZE);
        int yCord = (int) (yPos / Game.TILES_SIZE);
        return lvlData[yCord][xCord];
    }

    //checks if tile is solid
    public static boolean IsTileSolid(int xTile, int yTile, int[][] lvlData){
        int value = lvlData[yTile][xTile]; //Returns a value to be checked if value is a tile or not

        //48 is number of sprites in level data, must be changed if new tileset is being used
        //If condition makes sure value is an actual time as well si if value is not equal to transparent tile
        //Because obviously player should be allowed to pass through transparent tile
        if(value >= 48 || value < 0 || value != 11){
            return true;
        }
        else{
            return false;
        }
    }

    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed){

        int currentTile = (int)(hitbox.x / Game.TILES_SIZE); //Gets current tile entity is on

        //xSpeed obviously cannot be 0, because then entity would not be moving
        if(xSpeed > 0){ //Checks if player is moving
            //This condition checks to see if entity is colliding with a tile to the right
            int tileXPos = currentTile * Game.TILES_SIZE;
            //This int gives us the correct pixel value of the current tile
            int xOffset = (int)(Game.TILES_SIZE - hitbox.width);
            //Gets the offset value of the size of the entity and the size of the tile
            return tileXPos + xOffset - 1;
            //adding position of the tile and offset of player size and tile size returns the value required to get hitbox to the rightmost of a tile
            // -1 because the hitbox shouldn't overlap with the tile, just be next to it
        }
        else{
            //If not, player would be colliding with a tile to the left/not moving
            return currentTile * Game.TILES_SIZE;
            //Returns value to the leftmost side of a tile
        }

    }

    public static float getEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed){
        int currentTile = (int)(hitbox.y / Game.TILES_SIZE); //Gets current tile entity is on
        if(airSpeed > 0){ //Checks if entity is moving downwards in air or touching the floor
            int tileYPos = currentTile * Game.TILES_SIZE;
            int yOffset = (int)(Game.TILES_SIZE - hitbox.height);
            //Gets the offset value of the size of the entity and the size of the floor
            return tileYPos + yOffset - 1;
            //adding position of the tile and offset of player size and tile size returns the value required to get hitbox to the bottom most of a tile
            // -1 because the hitbox shouldn't overlap with the tile, just be next to it
        }
        else{ //else entity is jumping/up in the air
            return currentTile * Game.TILES_SIZE;
            //Returns value to the uppermost side of a tile
        }
    }

    public static boolean isEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData){
        //Check the pixel below bottom left and bottom right
        if(!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData)){ //Checks if bottom left (hitbox cannot be overlapping tile so + 1 is added) is not touching a solid tile
            if(!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData)){ //If so, checks bottom right (hitbox cannot be overlapping tile so + 1 is added) of hitbox as well
                return false;
            }
        }
        return true;
    }

    //checks if tile below and ahead is a floor tile or not
    public static boolean isFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData){ //Checks if there is a floor at the coordinate checked (in lvldata)
        if(xSpeed > 0){ //if moving to the right
            return IsSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
        }
        else{
            return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
        }
    }

    public static boolean IsFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
            if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
                return false;
        return true;
    }

    public static boolean CanCannonSeePlayer(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile){
        int firstXtile = (int)(firstHitbox.x / Game.TILES_SIZE); //Gets the x tile location of the first hitbox
        int secondXtile = (int)(secondHitbox.x / Game.TILES_SIZE); //Gets the x tile location of the second hitbox

        if(firstXtile > secondXtile){
            return IsAllTilesClear(secondXtile, firstXtile, yTile, lvlData); //checks if tiles between two points are clear
        }
        else{
            return IsAllTilesClear(secondXtile, firstXtile, yTile, lvlData); //checks if tiles between two points are clear
        }
    }

    //this method checks all tiles at a specific y value
    public static boolean IsAllTilesClear(int xStart, int xEnd, int y, int[][] lvlData){ //checks to see if all tiles from one point to another are transparent (not solid)
        for(int i = 0; i < xEnd - xStart; i++) {
            if (IsTileSolid(xStart + i, y, lvlData)) {
                return false;
            }
        }
        return true;
    }

    //Checks if all tiles that are one tile below given y value and also between two x values are walkable by entities or not
    public static boolean IsAllTilesWalkable(int xStart, int xEnd, int y, int[][] lvlData){
        if(IsAllTilesClear(xStart, xEnd, y, lvlData)){ //checks if all tiles are clear first
            for(int i = 0; i < xEnd - xStart; i++){
                if(!IsTileSolid(xStart + i, y + 1, lvlData)){ //Checks if tile underneath previous checked tiles are not solid
                    return false; //if so, not all tiles are walkable, so method returns false
                }
            }
        }
        return true;
    }

    //reusable method that checks if there is a line of sight between two points
    public static boolean IsSightClear(int[][] lvlData, Rectangle2D.Float enemyBox, Rectangle2D.Float playerBox, int yTile){
        int firstXtile = (int)(enemyBox.x / Game.TILES_SIZE); //Gets the x tile location of the first hitbox
        int secondXtile; //Gets the x tile location of the second hitbox using the if conditions below

        if(IsSolid(playerBox.x, playerBox.y + playerBox.height + 1, lvlData)){ //checks bottom left corner of tile
            secondXtile = (int)(playerBox.x / Game.TILES_SIZE);
        }
        else{
            secondXtile = (int)((playerBox.x + playerBox.width) / Game.TILES_SIZE); //checks bottom right corner of tile
        }
        if(firstXtile > secondXtile){
            return IsAllTilesWalkable(secondXtile, firstXtile, yTile, lvlData);
        }
        else{
            return IsAllTilesWalkable(secondXtile, firstXtile, yTile, lvlData);
        }
    }


    /**
     * Returns the distance between two x-tiles as an integer
     */
    public static int distanceToFirstSolidTile(int xStart, int y, int direction, int[][] lvlData){
        int distance = 0;
        while (!IsTileSolid(xStart + distance * direction, y, lvlData)) {
            distance++;
        }
        return distance;
    }


}
