package objects;

//This class creates the two different types of potions used in the game

import main.Game;

public class Potion extends GameObject{

    private float hoverOffset; //current offset value of hovering potion (speed)
    private int maxHoverOffset, hoverDirection = 1; //Maximum offset potion can hover up to | -1 for up     1 for down (direction)
    public Potion(int x, int y, int objType) {
        super(x, y, objType);
        doAnimation = true; //potions constantly animate by default
        initHitbox(7, 14);
        xDrawOffset = (int)(3 * Game.SCALE); //gets an offset value between drawn image and hitbox of potion
        yDrawOffset = (int)(2 * Game.SCALE);

        maxHoverOffset = (int)(10 * Game.SCALE); //maximum offset when hovering is 10 (for a small hover effect)
    }

    public void update(){
        updateAnimationTick();
        updateHover(); //updates hovering of potion
    }

    private void updateHover() {
        hoverOffset += (0.07f * Game.SCALE * hoverDirection); //hover speed | dependent on hover direction as well

        if(hoverOffset >= maxHoverOffset){ //when hoverOffset reaches max offset value
            hoverDirection = -1; //reverse direction downwards
        }
        else if(hoverOffset < 0){ //when hoverOffset reaches a value of 0
            hoverDirection = 1; //set value back up to 1 (increasing)
        }
        hitbox.y = y + hoverOffset; //y value from gameObject class | hitbox is relative to this y value
    }
}
