package objects;

import main.Game;

import java.awt.geom.Rectangle2D;
import static utils.Constants.Projectiles.*;

public class GolemProjectile {
    private Rectangle2D.Float hitbox;
    private int direction; //either to the left or the right (1 or -1)
    private boolean active = true; //checks if projectile is active set true by default


    //Projectile takes an x and y coordinate as well as direction it is going
    public GolemProjectile(int x, int y, int direction){
        int xOffset = (int)(-3 * Game.SCALE); //default offset for when facing to the left (changes when facing right)
        int yOffset = (int)(5 * Game.SCALE); //default yOffset for facing left and right projectiles

        if(direction == -1){
            xOffset = (int)(29 * Game.SCALE); //changes offset to 29 so that it looks like projectile is coming out of the cannon
        }
        hitbox = new Rectangle2D.Float(x - xOffset, y + yOffset, GOLEM_PROJECTILE_WIDTH, GOLEM_PROJECTILE_HEIGHT);
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

    public int getDirection(){
        return direction;
    }
}
