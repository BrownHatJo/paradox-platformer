package objects;

//This class creates the cannon ball projectiles shot out by th cannons in the game

import main.Game;

import java.awt.geom.Rectangle2D;
import static utils.Constants.Projectiles.*;

public class Projectile {
    private Rectangle2D.Float hitbox;
    private int direction; //either to the left or the right (1 or -1)
    private boolean active = true; //checks if projectile is active set true by default


    //Projectile takes an x and y coordinate as well as direction it is going
    public Projectile(int x, int y, int direction){
        int xOffset = (int)(-3 * Game.SCALE); //default offset for when facing to the left (changes when facing right)
        int yOffset = (int)(5 * Game.SCALE); //default yOffset for facing left and right projectiles

        if(direction == 1){
            xOffset = (int)(29 * Game.SCALE); //changes offset to 29 so that it looks like projectile is coming out of the cannon
        }
        hitbox = new Rectangle2D.Float(x + xOffset, y + yOffset, CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT);
        this.direction = direction;
    }

    public void updatePosition(){
        hitbox.x += direction * SPEED; //updates position of projectile and its hitbox
    }

    public void setPos(int x, int y){
        hitbox.x = x;
        hitbox.y = y;
    }

    public Rectangle2D.Float getHitbox(){
        return hitbox;
    }

    public void setActive(boolean active){
        this.active = active;
    }

    public boolean isActive(){
        return active;
    }
}
