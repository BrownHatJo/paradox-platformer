package entities;

//Parent class used by all entities in the game including players, enemies and npcs

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utils.Constants.Directions.UP;
import static utils.Constants.Directions.DOWN;
import static utils.Constants.Directions.LEFT;
import static utils.HelpMethods.canMoveHere;

public abstract class Entity { //Abstract class so that an entity object cannot be created
    protected float x, y; //protected so that subclasses of entity can use these variables
    protected  int width, height; //Width and Height of entities
    protected Rectangle2D.Float hitbox;
    protected int animationTick, animationIndex;
    protected int state;
    protected float airSpeed; //Controls the speed of player in air. (when jumping and falling)
    protected boolean inAir = false;
    protected int maxHealth; //starting health of player when game starts
    protected int currentHealth; //current health of player, that always starts at max health;
    protected Rectangle2D.Float attackBox; //attackbox of entities
    protected float walkSpeed = 1.0f * Game.SCALE;

    protected int pushBackDir;
    protected float pushDrawOffset;
    protected int pushBackOffsetDir = UP;

    public Entity(float x, float y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void updatePushBackDrawOffset() {
        float speed = 0.95f;
        float limit = -30f;

        if (pushBackOffsetDir == UP) {
            pushDrawOffset -= speed;
            if (pushDrawOffset <= limit)
                pushBackOffsetDir = DOWN;
        } else {
            pushDrawOffset += speed;
            if (pushDrawOffset >= 0)
                pushDrawOffset = 0;
        }
    }

    protected void pushBack(int pushBackDir, int[][] lvlData, float speedMulti) {
        float xSpeed = 0;
        if (pushBackDir == LEFT)
            xSpeed = -walkSpeed;
        else
            xSpeed = walkSpeed;

        if (canMoveHere(hitbox.x + xSpeed * speedMulti, hitbox.y, hitbox.width, hitbox.height, lvlData))
            hitbox.x += xSpeed * speedMulti;
    }

    protected void drawAttackBox(Graphics g, int xLvlOffset) { //Draws attackBox
        g.setColor(Color.red);
        g.drawRect((int)attackBox.x - xLvlOffset, (int)attackBox.y, (int)attackBox.width, (int)attackBox.height);
    }

    protected void drawHitbox(Graphics g, int levelOffsetX){
        //for debugging the hitbox
        g.setColor(Color.PINK);
        g.drawRect((int)hitbox.x - levelOffsetX, (int)hitbox.y, (int)hitbox.width, (int)hitbox.height);
    }

    protected void initHitbox(int width, int height) { //Initializes entities hitbox
        hitbox = new Rectangle2D.Float(x, y, (int)(width * Game.SCALE), (int)(height * Game.SCALE)); //Creates a hitbox around the entire entity image
    }

//    protected void updateHitbox(){ //Dont need to update width and height as they are always constant
//        hitbox.x = (int)x; //Allows only CLASSES extending entity to use this method
//        hitbox.y = (int)y;
//    }

    public Rectangle2D.Float getHitbox(){
        return hitbox;
    }

    public int getState(){
        return state;
    }

    public int getAnimationIndex(){
        return animationIndex;
    }

    public int getCurrentHealth(){
        return currentHealth;
    }

    protected void newState(int state) {
        this.state = state;
        animationTick = 0;
        animationIndex = 0;
    }


}
