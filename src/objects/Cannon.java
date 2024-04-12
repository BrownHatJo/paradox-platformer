package objects;

import main.Game;

public class Cannon extends GameObject{

    private int tileY; //used to check if player and cannon are on the same y tile


    public Cannon(int x, int y, int objType) {
        super(x, y, objType);
        tileY = y / Game.TILES_SIZE;
        initHitbox(40, 26); //initializes hitbox of the cannon
        hitbox.x -= (int)(4 * Game.SCALE); //centers cannon to tile
        hitbox.y += (int)(6 * Game.SCALE);
    }

    public void update(){
        if(doAnimation){
            updateAnimationTick();
        }
    }

    public int getTileY(){
        return tileY;
    }
}
