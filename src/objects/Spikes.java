package objects;

//This class creates the spike objects which are used in the game as traps for the players

import main.Game;

public class Spikes extends GameObject{
    //obj type value for spikes is 5
    public Spikes(int x, int y, int objType) {
        super(x, y, objType);
        initHitbox(32,16); //half height of entire sprite (rest is transparent)
        xDrawOffset = 0;
        yDrawOffset = (int)(Game.SCALE * 16); //offset value that offsets spikes from actual size of image
        hitbox.y += yDrawOffset; //makes sure spikes fit perfectly on bottom of floor
    }

}
