package objects;

//this class creates the paradox shards that need to be collected in the game

import main.Game;

//This object is used for the paradox shard items in the game
public class ParadoxShards extends GameObject{
    public ParadoxShards(int x, int y, int objType) {
        super(x, y, objType);
        doAnimation = true; //animation plays by default
        initHitbox(7, 14); //subject to change
        xDrawOffset = (int)(3 * Game.SCALE); //gets an offset value between drawn image and hitbox of potion
        yDrawOffset = (int)(2 * Game.SCALE); //SUBJECT TO CHANGE

    }

    public void update(){
        updateAnimationTick();
    }
}
