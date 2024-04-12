package entities;

//this class creates a plague crow enemy
//This is an enemy found in all 6 levels which is hostile to the player and attacks when they see a player

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utils.Constants.Directions.LEFT;
import static utils.Constants.Directions.RIGHT;
import static utils.Constants.EnemyConstants.*;
import static utils.HelpMethods.IsFloor;

import gamestates.Playing;


public class PlagueCrow extends Enemy {
    private int attackBoxOffsetX;

    //Width, Height and EnemyType are always constant
    public PlagueCrow(float x, float y) {
        super(x, y, (int)(CROW_WIDTH * Game.SCALE), (int)(CROW_HEIGHT * Game.SCALE), PLAGUE_CROW);
        initHitbox(20, 23); //width and height subject to change
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int)(50 * Game.SCALE), (int)(23 * Game.SCALE));
        attackBoxOffsetX = (int)(Game.SCALE * 25);

    }

    public void update(int[][] lvlData, Playing playing){
        updateBehavior(lvlData, playing);
        updateAnimationTick();
        updateAttackBox();
    }


    private void updateAttackBox(){
        if(walkDir == RIGHT){
            attackBox.x = hitbox.x; //subject to change
        }
        else{
            attackBox.x = hitbox.x - attackBoxOffsetX; //subject to change
            attackBox.y = hitbox.y;

        }



    }

    private void updateBehavior(int[][] lvlData, Playing playing){
        if(firstUpdate){ //Checks if this is the first time enemies are updated
            firstUpdateCheck(lvlData);
        }
        if(inAir){ //if enemy is in the air
            inAirChecks(lvlData);
        }
        else{ //if enemy isnt in air
            switch (state){
                case IDLE:
                    if (IsFloor(hitbox, lvlData))
                        newState(RUNNING);
                    else
                        inAir = true;
                    break;
                case RUNNING:
                    if(canSeePlayer(lvlData, playing.getPlayer())){ //if player is in line of sight
                        turnToPlayer(playing.getPlayer()); //turn towards player
                        if(isPlayerCloseForAttack(playing.getPlayer())){ //check if player is close enough for an attack
                            newState(ATTACK); //If so, change state to attack
                        }
                    }
                    else if(canSeePlayer(lvlData, playing.getPlayer2()) && !playing.getPlayer2().isCloakActive()){ //if player is in line of sight
                        turnToPlayer(playing.getPlayer2()); //turn towards player
                        if(isPlayerCloseForAttack(playing.getPlayer2())){ //check if player is close enough for an attack
                            newState(ATTACK); //If so, change state to attack
                        }
                    }
                    move(lvlData);
                    break;
                case ATTACK:
                    if(animationIndex == 0){ //everytime animation is restarted, a check will be required
                        attackChecked = false;
                    }

                    if(animationIndex == 1 && !attackChecked){ //attackChecked makes sure theres only 1 check per animation
                        checkPlayerHit(attackBox, playing.getPlayer()); //checks if enemy hit player1 or 2
                        checkPlayerHit(attackBox, playing.getPlayer2());
                    }
                    break;
                case HIT:
                    if (animationIndex <= getSpriteAmount(enemyType, state) - 2)
                        pushBack(pushBackDir, lvlData, 2f);
                    updatePushBackDrawOffset();
                    break;
            }
        }
    }
    public int flipX(){ //Checks if enemy is facing right, in which case flipX is set to enemy width
        if(walkDir == LEFT){
            return CROW_WIDTH;
        }
        else{
            return 0;
        }
    }

    public int flipW(){ //checks if enemy is facing left, in which case flipW is set to -1 so width is flipped
        if(walkDir == LEFT){
            return -1;
        }
        else{
            return 1;
        }
    }

}
