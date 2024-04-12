package objects;

import gamestates.Playing;
import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utils.Constants.*;
import static utils.Constants.EnemyConstants.PARADOX_GOLEM;
import static utils.Constants.EnemyConstants.getEnemyDmg;
import static utils.Constants.Projectiles.*;

public class Laser {

    private int animationIndex, animationTick;
    private Rectangle2D.Float hitbox;
    private int direction;
    private int distanceToClosestTile;
    private boolean active = true;
    private boolean isFired = false;
    private Rectangle2D.Float laserAttackBox;
    private int laserAttackBoxOffsetX;
    private Playing playing;
    private long lastLaserShot;
    private boolean canGolemMove;

    /**
     * Constructs a new laser,
     * at x and y position,
     * with direction -1 or 1 (left and right, respectively)
     */
    public Laser(int x, int y, int direction, int distanceToClosestTile, Playing playing) {
        this.playing = playing;
        int xOffset = (int)(-15 * Game.SCALE); //default offset for when facing to the left (changes when facing right)
        int yOffset = (int)(5 * Game.SCALE); //default yOffset for facing left and right projectiles
        this.direction = direction;
        this.distanceToClosestTile = distanceToClosestTile;

        laserAttackBoxOffsetX = (int)(Game.SCALE * 20);
        int width = (int) (LASER_W * 1.0);
        int laserDistance = (int) (getDistanceToClosestTile() * Game.TILES_SIZE * 1.0);
        int widthFlip = laserDistance * -1;

        laserAttackBox = new Rectangle2D.Float(x + xOffset, y + yOffset, laserDistance, 20);

        if (direction == -1){
            laserAttackBox = new Rectangle2D.Float(x - xOffset - LASER_W, y + yOffset, laserDistance, 20);
        }
    }

    protected void checkLaserHitP1(Rectangle2D.Float laserAttackBox){
        if(laserAttackBox.intersects(playing.getPlayer().getHitbox())){ //if enemies attackBox intersects players hitbox
            playing.getPlayer().changeHealth(-getEnemyDmg(PARADOX_GOLEM)); //decrements player health depending on damage of given enemy type
        }
    }

    protected void checkLaserHitP2(Rectangle2D.Float laserAttackBox){
        if(laserAttackBox.intersects(playing.getPlayer2().getHitbox())){ //if enemies attackBox intersects players hitbox
            playing.getPlayer2().changeHealth(-getEnemyDmg(PARADOX_GOLEM)); //decrements player health depending on damage of given enemy type
        }
    }
    protected void drawHitbox(Graphics g, int levelOffsetX) {
        if (animationIndex >= 9) {
            isFired = true;

            int width = (int) (LASER_W * 1.0);
            int laserDistance = (int) (getDistanceToClosestTile() * Game.TILES_SIZE * 1.0);
            int widthFlip = laserDistance * -1;

            if (direction == 1) {
                g.setColor(Color.red);
                g.drawRect((int)(laserAttackBox.x), (int)(laserAttackBox.y), laserDistance, (int)(laserAttackBox.height));
            }
            else if (direction == -1) {
                g.setColor(Color.red);
                g.drawRect((int)(laserAttackBox.x + levelOffsetX), (int)(laserAttackBox.y), laserDistance, (int)(laserAttackBox.height));
                //g.drawRect((int)(laserAttackBox.x + width), (int)(laserAttackBox.y), widthFlip, (int)(laserAttackBox.height));
            }
        }
    }

    public int flipX() {
        if (direction == -1)
            return LASER_W;
        else
            return 0;
    }

    public int flipW() {
        if (direction == -1)
            return -1;
        else
            return 1;
    }
    /**
     * Main update method for a single laser beam
     * One parameter: the laser being shot
     * Goes through all 15 animations, then sets active to false,
     * which leads to the laser no longer being updated/drawn
     */
    public void update(Laser laser) {
        canGolemMove = System.currentTimeMillis() >= lastLaserShot + 1000;

        if (active) {
//            System.out.println(isFired);
            if(animationIndex >= 9){
                if (animationIndex >= 13) {
                    laser.active = false;
                    laser.lastLaserShot = System.currentTimeMillis();
                }
                if(isFired){
                    checkLaserHitP1(laserAttackBox);
                    if(!playing.getPlayer2().isCloakActive()){
                        checkLaserHitP2(laserAttackBox);
                    }
                }
            }
            else{
                isFired = false;
            }
            animationTick++;
            if (animationTick >= ANIMATION_SPEED) {
                animationTick = 0;
                animationIndex++;
            }
        }
    }

    // ----- Getters & Setters ------

    public int getDistanceToClosestTile() {
        return distanceToClosestTile;
    }

    public Rectangle2D.Float getLaserAttackBox(){
        return laserAttackBox;
    }

    public boolean isActive(){
        return active;
    }

    public int getAnimationIndex() {
        return animationIndex;
    }

    public int getDirection(){
        return direction;
    }
    public void reset() { //used when level is reset so all objects are reset
        animationTick = 0;
        animationIndex = 0;
        active = false;
    }
}