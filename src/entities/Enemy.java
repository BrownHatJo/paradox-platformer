package entities;

import main.Game;

import java.awt.geom.Rectangle2D;

import static utils.Constants.*;
import static utils.Constants.EnemyConstants.*;
import static utils.Constants.PlayerConstants.DEAD;
import static utils.HelpMethods.*;
import static utils.Constants.Directions.*;
import gamestates.Playing;

//Main class of all enemies
//abstract means enemy class cannot be created, only extensions of it
public class Enemy extends Entity{

    protected int enemyType; //index of animation | state of what enemy is doing (running, idle, etc.) | type of enemy
    protected int animationSpeed = 25; //animation ticks | speed of animation
    protected boolean firstUpdate = true; //true by default
    protected int walkDir = LEFT; //left by default
    protected int tileY;
    protected float attackDistance = Game.TILES_SIZE; //Attack distance is the size of a tile
    protected boolean active = true;
    protected boolean attackChecked;

    protected long cooldownTime = 0;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        maxHealth = getMaxHealth(enemyType); //method used to check different type of enemies
        currentHealth = maxHealth;
        walkSpeed = Game.SCALE * 0.25f;
    }

    protected void firstUpdateCheck(int[][] lvlData){
        if(!isEntityOnFloor(hitbox, lvlData)){ //Checks if entity isnt on floor
            inAir = true;
        }
        firstUpdate = false;
    }

    protected void inAirChecks(int[][] lvlData) {
        if (state != HIT && state != DEAD) {
            updateInAir(lvlData);
            if (IsEntityInWater(hitbox, lvlData))
                hurt(maxHealth);
        }
    }

    protected void updateInAir(int[][] lvlData){
        if(canMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)){ //Checks if enemy can move into this position in air
            hitbox.y += airSpeed; //Changes enemy y pos by fallSpeed
            airSpeed += GRAVITY; //fallSpeed is then affected by gravity value
        }
        else{ //if enemy cannot move here
            inAir = false;
            hitbox.y = getEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
            tileY = (int)(hitbox.y / Game.TILES_SIZE);
        }
    }

    protected void move(int[][] lvlData){
        float xSpeed = 0;
        if(walkDir == LEFT){ //if enemy is walking left
            xSpeed = -walkSpeed;
        }
        else{ //else enemy is going right
            xSpeed = walkSpeed;
        }
        //tests if enemy predicted position can be moved to
        if(canMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)){
            //then checks if predicted position is a floor
            if(isFloor(hitbox, xSpeed, lvlData)){
                hitbox.x += xSpeed;
                return;
            }
        }
        changeWalkDir();
    }

    protected void turnToPlayer(Adventurer player){ //Depending on what side of the enemy the player is in (and if enemy can see player) turns direction of enemy to walk towards player
        if(player.hitbox.x > hitbox.x){
            walkDir = RIGHT;
        }
        else{
            walkDir = LEFT;
        }
    }

    protected void turnToPlayer(Ghost ghost){ //Depending on what side of the enemy the player is in (and if enemy can see player) turns direction of enemy to walk towards player
        if(ghost.hitbox.x > hitbox.x){
            walkDir = RIGHT;
        }
        else{
            walkDir = LEFT;
        }
    }

    protected boolean canSeePlayer(int[][] lvlData, Adventurer player){
        int playerTileY = (int)(player.getHitbox().y / Game.TILES_SIZE); //Gets y coord of player
        if(playerTileY == tileY){ //Checks if player and enemy are on the same y coordinate
            if(isPlayerInRange(player)){ //If so, checks to see if player is in range of enemies vision
                if(IsSightClear(lvlData, hitbox, player.hitbox, tileY)){ //Checks to see if player is in LINE OF SIGHT (nothing blocking enemy from reaching player)
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean canSeePlayer(int[][] lvlData, Ghost ghost){
        int playerTileY = (int)(ghost.getHitbox().y / Game.TILES_SIZE); //Gets y coord of player
        if(playerTileY == tileY){ //Checks if player and enemy are on the same y coordinate
            if(isPlayerInRange(ghost)){ //If so, checks to see if player is in range of enemies vision
                if(IsSightClear(lvlData, hitbox, ghost.hitbox, tileY)){ //Checks to see if player is in LINE OF SIGHT (nothing blocking enemy from reaching player)
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean isPlayerInRange(Adventurer player) {
        int absValue = (int)(Math.abs(player.hitbox.x - hitbox.x)); //Returns absolute distance between players hitbox and enemies hitbox
        //int oppAbsValue = (int)(Math.abs(hitbox.x - player.x))

        return absValue <= attackDistance * 5; //subject to change
        //returns if abs value is less than or equal to distance as true otherwise false aka not in range
    }

    protected boolean isPlayerInRange(Ghost ghost) {
        int absValue = (int)(Math.abs(ghost.hitbox.x - hitbox.x)); //Returns absolute distance between players hitbox and enemies hitbox

        return absValue <= attackDistance * 5; //subject to change
        //returns if abs value is less than or equal to distance as true otherwise false aka not in range
    }

    protected boolean isPlayerCloseForAttack(Adventurer player){
        int absValue = (int)(Math.abs(player.hitbox.x - hitbox.x)); //Returns absolute distance between players hitbox and enemies hitbox

        // golem is walking LEFT
        if (walkDir == 0) {
            return absValue <= attackDistance; //subject to change
        }
        // golem is walking RIGHT
        else {
            return absValue <= attackDistance * 2; //subject to change
        }
        //checks to see if player is in range for an attack
    }


    protected boolean isPlayerCloseForAttack(Ghost ghost){
        int absValue = (int)(Math.abs(ghost.hitbox.x - hitbox.x)); //Returns absolute distance between players hitbox and enemies hitbox

        // golem is walking LEFT
        if (walkDir == 0) {
            return absValue <= attackDistance; //subject to change
        }
        // golem is walking RIGHT
        else {
            return absValue <= attackDistance * 2; //subject to change
        }
        //checks to see if player is in range for an attack
    }

    protected void newState(int enemyState){ //Resets animations when state changes
        this.state = enemyState;
        animationTick = 0;
        animationIndex = 0;
    }

    public void hurt(int amount) {
        currentHealth -= amount;
        if (currentHealth <= 0)
            newState(DEATH_1);
        else {
            newState(HIT);
            if (walkDir == LEFT)
                pushBackDir = RIGHT;
            else
                pushBackDir = LEFT;
            pushBackOffsetDir = UP;
            pushDrawOffset = 0;
        }
    }

    public void bossHurt(int amount) {

        currentHealth -= amount;
        if (currentHealth <= 0)
            newState(DEATH_1);
        else {

            //newState(BOSS_HIT);
            if (walkDir == LEFT)
                pushBackDir = RIGHT;
            else
                pushBackDir = LEFT;
            pushBackOffsetDir = UP;
            pushDrawOffset = 0;
        }
    }

    protected void checkPlayerHit(Rectangle2D.Float attackBox, Adventurer player){
        if(attackBox.intersects(player.hitbox)){ //if enemies attackBox intersects players hitbox
            player.changeHealth(-getEnemyDmg(enemyType)); //decrements player health depending on damage of given enemy type
        }
        attackChecked = true; //once check is complete, set check variable to true
    }

    protected void checkPlayerHit(Rectangle2D.Float attackBox, Ghost ghost){
        if(attackBox.intersects(ghost.hitbox)){ //if enemies attackBox intersects players hitbox
            ghost.changeHealth(-getEnemyDmg(enemyType)); //decrements player health depending on damage of given enemy type
        }
        attackChecked = true; //once check is complete, set check variable to true
    }

    protected void updateAnimationTick() {
        animationTick++;
        if (animationTick >= ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;
            if(state == BOSS_HIT){
                //newState(IDLE);
            }
            if (animationIndex >= getSpriteAmount(enemyType, state)) {
//                if (enemyType == PLAGUE_CROW) {
                animationIndex = 0;

                if (enemyType == PARADOX_GOLEM) {
                    if(state == BOSS_HIT){
                        newState(BOSS_ACTIVE);
                    }
                    switch (state) {
                        case SHOOTING_LASER -> {
                            animationIndex = getSpriteAmount(PARADOX_GOLEM, SHOOTING_LASER) - 1;

                            if(cooldownTime == 0){
                                cooldownTime = System.currentTimeMillis();
                            }
                            else if(System.currentTimeMillis() - cooldownTime >= 2000){
                                state = IDLE;
                                cooldownTime = 0;
                            }
                        }
                        case ATTACK, HIT, BOSS_HIT -> state = BOSS_ACTIVE;
                        case DEATH_1 -> active = false;
                        case BOSS_DEATH_1, BOSS_DEATH_2 -> active = false;
                    }
                }
                else {
                    switch (state) {
                        case ATTACK, HIT, BOSS_HIT -> state = IDLE;
                        case DEATH_1 -> active = false;
                        case BOSS_DEATH_1, BOSS_DEATH_2 -> active = false;
                    }
                }



            }
//                }
        }
     }

    protected void updateNPCAnimationTick() {
        animationTick++;
        if (animationTick >= ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= getSpriteAmount(enemyType, state)) {
//                if (enemyType == PLAGUE_CROW) {
                animationIndex = 0;

                switch (state) {
                    case ATTACK, HIT, BOSS_HIT -> state = IDLE;
                    case DEATH_1 -> active = false;
                    case BOSS_DEATH_1, BOSS_DEATH_2 -> active = false;


                }
//                }
            }
        }
    }

    protected void changeWalkDir() {
        if(walkDir == LEFT){
            walkDir = RIGHT;
        }
        else{
            walkDir = LEFT;
        }
    }

    public void resetEnemy() {
        hitbox.x = x; //x and y are never used so they can be used as reset values for player position
        hitbox.y = y;
        firstUpdate = true; //first update is set back to true
        currentHealth = maxHealth; //health is reset
        newState(IDLE); //enemy state is set back to idle
        active = true; //sets enemy back to being active in the level
        airSpeed = 0; //fallSpeed is reset to 0
    }

    public int flipX() {
        if (walkDir == RIGHT)
            return width;
        else
            return 0;
    }

    public int flipW() {
        if (walkDir == RIGHT)
            return -1;
        else
            return 1;
    }

    public boolean isActive(){
        return active;
    }

    public float getPushDrawOffset() {
        return pushDrawOffset;
    }


}
