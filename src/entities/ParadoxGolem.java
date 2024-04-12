package entities;

//This class creates the final boss of the game which can be found and defeated in level 6 to win

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import static utils.Constants.Directions.LEFT;
import static utils.Constants.Directions.RIGHT;
import static utils.Constants.EnemyConstants.*;
import static utils.Constants.Projectiles.LASER_H;
import static utils.Constants.Projectiles.LASER_W;
import static utils.HelpMethods.IsFloor;

import gamestates.Playing;
import objects.GolemProjectile;
import objects.Laser;
import objects.ObjectManager;



public class ParadoxGolem extends Enemy {
    private int attackBoxOffsetX;
    private int laserAttackBoxOffsetX;

    private ArrayList<GolemProjectile> golemProjectiles;
    private Rectangle2D.Float laserAttackBox;
    //Width, Height and EnemyType are always constant
    public ParadoxGolem(float x, float y) {
        super(x, y, (int)(GOLEM_WIDTH * Game.SCALE), (int)(GOLEM_HEIGHT * Game.SCALE), PARADOX_GOLEM);
        initHitbox(60, 30); //width and height subject to change
        initAttackBox();
        golemProjectiles = new ArrayList<>();
    }

    public void update(int[][] lvlData, Playing playing){
        updateAttackBox();
        updateBehavior(lvlData, playing);
        updateAnimationTick();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int)(50 * Game.SCALE), (int)(23 * Game.SCALE));
        attackBoxOffsetX = (int)(Game.SCALE * 25);

        int xOffset = (int)(-3 * Game.SCALE); //default offset for when facing to the left (changes when facing right)
        int yOffset = (int)(5 * Game.SCALE); //default yOffset for facing left and right projectiles

        laserAttackBox = new Rectangle2D.Float(x + xOffset, y + yOffset, LASER_W, 20);
        laserAttackBoxOffsetX = (int)(Game.SCALE * 20);
    }

    protected void drawLaserAttackBox(Graphics g, int xLvlOffset) { //Draws attackBox
        g.setColor(Color.red);
        //g.drawRect((int)(laserAttackBox.x - xLvlOffset), (int)(laserAttackBox.y), (int)(laserAttackBox.width), (int)(laserAttackBox.height));
    }

    private void updateAttackBox(){ //maybe make this range of spike attack
        if(walkDir == RIGHT){
            attackBox.x = hitbox.x + attackBoxOffsetX; //subject to change

            laserAttackBox.x = hitbox.x + attackBoxOffsetX + laserAttackBoxOffsetX;
        }
        else{
            attackBox.x = hitbox.x - attackBoxOffsetX; //subject to change
            attackBox.y = hitbox.y;

            laserAttackBox.x = hitbox.x - laserAttackBox.width;
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
                        newState(BOSS_ACTIVE);
                    else
                        inAir = true;
                    break;
                case BOSS_ACTIVE:
                    if(canSeePlayer(lvlData, playing.getPlayer())){ //if player is in line of sight
                        turnToPlayer(playing.getPlayer()); //turn towards player
                        if(isPlayerCloseForAttack(playing.getPlayer())){ //check if player is close enough for an attack
                            newState(ATTACK); //If so, change state to attack
                        }
                        else{
                            newState(SHOOTING_LASER);
                        }
                    }
                    else if(canSeePlayer(lvlData, playing.getPlayer2()) && !playing.getPlayer2().isCloakActive()){ //if player is in line of sight
                        turnToPlayer(playing.getPlayer2()); //turn towards player
                        if(isPlayerCloseForAttack(playing.getPlayer2())){ //check if player is close enough for an attack
                            newState(ATTACK); //If so, change state to attack
                        }
                        else{
                            newState(SHOOTING_LASER);
                        }
                    }
                    move(lvlData);
                    break;
                case ATTACK:
                    if(animationIndex == 0){ //everytime animation is restarted, a check will be required
                        attackChecked = false;
                    }

                    if(animationIndex == 8 && !attackChecked){ //attackChecked makes sure theres only 1 check per animation
                          ObjectManager.golemShoot(this);
                          attackChecked = true;
                          checkPlayerHit(attackBox, playing.getPlayer()); //checks if enemy hit player1 or 2
                        if(!playing.getPlayer2().isCloakActive()){
                            checkPlayerHit(attackBox, playing.getPlayer2());
                        }
                    }
                    break;
                case SHOOTING_LASER:
                    if (animationIndex == 0) {
                        attackChecked = false;
                    }
                    if (animationIndex == 5 && !attackChecked) {
                        ObjectManager.laserAttack(this, lvlData, playing);
                        attackChecked = true;
                    }
                    break;
                case BOSS_HIT:
                    //newState(IDLE);
//                    if (animationIndex <= getSpriteAmount(enemyType, state) - 2)
//                        pushBack(pushBackDir, lvlData, 1f);
//                    updatePushBackDrawOffset();
//                    break;
            }
        }
    }



    public int flipX(){ //Checks if enemy is facing right, in which case flipX is set to enemy width
        if(walkDir == LEFT){
            return GOLEM_WIDTH;
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
