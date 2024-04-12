package objects;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import static utils.Constants.*;

import static utils.Constants.ObjectConstants.*;

public class GameObject {

    protected int x, y, objType;
    protected Rectangle2D.Float hitbox;
    protected boolean doAnimation, active = true; //Boolean to check if animation should run or not | Booolean to check if item is active (in game) or not
    protected int animationTick, animationIndex;
    protected int xDrawOffset, yDrawOffset; //x and y offset values for drawing objects

    public GameObject(int x, int y, int objType){
        this.x = x;
        this.y = y;
        this.objType = objType;
    }

    protected void updateAnimationTick(){
        animationTick++;

        if(animationTick >= ANIMATION_SPEED){
            animationTick = 0;
            animationIndex++;
            if(animationIndex >= GetSpriteAmount(objType)){
                animationIndex = 0;
                if(objType == BARREL || objType == BOX){ //If object being animated is barrel or box...
                    doAnimation = false; //then set animations to false after one animation as object should only be destroyed once
                    active = false;
                }
                else if(objType == CANNON_LEFT || objType == CANNON_RIGHT){ //if object is a cannon
                    doAnimation = false; //after one animation, animation is set to false
                }
            }
        }
    }

    public void reset(){ //used when level is reset so all objects are reset
        animationTick = 0;
        animationIndex = 0;
        active = true;

        if(objType == BARREL || objType == BOX || objType == CANNON_LEFT || objType == CANNON_RIGHT){ //Checks if object type being reset is a barrel/box/cannon, in which case animation should not occur
            doAnimation = false; //Because barrels and boxes only animate when theyre destroyed
        }
        else{
            doAnimation = true; //else animation occurs for the other items
        }
    }

    protected void initHitbox(int width, int height) { //Initializes object hitbox
        hitbox = new Rectangle2D.Float(x, y, (int)(width * Game.SCALE), (int)(height * Game.SCALE)); //Creates a hitbox around the entire entity image
    }

    public void drawHitbox(Graphics g, int levelOffsetX){
        //for debugging the hitbox
        g.setColor(Color.PINK);
        g.drawRect((int)hitbox.x - levelOffsetX, (int)hitbox.y, (int)hitbox.width, (int)hitbox.height);
    }

    public int getxDrawOffset() {
        return xDrawOffset;
    }

    public int getyDrawOffset() {
        return yDrawOffset;
    }

    public int getObjType() {
        return objType;
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active){
        this.active = active;
    }

    public void setAnimation(boolean doAnimation){
        this.doAnimation = doAnimation;
    }

    public int getAnimationIndex(){
        return animationIndex;
    }

    public int getAnimationTick(){
        return animationTick;
    }
}
