package objects;

import main.Game;

import static utils.Constants.ObjectConstants.*;

public class GameContainer extends GameObject{

    public GameContainer(int x, int y, int objType) {
        super(x, y, objType);
        createHitbox(); //method to create hitbox because size of barrel and box are different
    }

    private void createHitbox() {
        if(objType == BOX){ //object hitbox as well as offset values are different for a box and barrel
            initHitbox(25, 18); //hitbox is not the same size as the tile

            xDrawOffset = (int)(7 * Game.SCALE);
            yDrawOffset = (int)(12 * Game.SCALE);
        }
        else{ //else if obj type is a barrel
            initHitbox(23, 25);

            xDrawOffset = (int)(8 * Game.SCALE);
            yDrawOffset = (int)(5 * Game.SCALE);
        }

        hitbox.y += yDrawOffset + (int)(Game.SCALE * 2); //Drops container by a value of yDrawOffset + two so that it looks like its hitting the floor
        hitbox.x += xDrawOffset / 2; //centers the object to the middle of a tile
    }

    public void update(){
        if(doAnimation){ //only if object needs to be animated (when broken by player)
            updateAnimationTick();
        }
    }


}
