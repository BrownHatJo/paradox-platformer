package entities;


//This class is used to create the second player, the ghost and has similar properties to player 1 but is a different character altogether


import audio.AudioPlayer;
import gamestates.Playing;
import main.Game;
import utils.LoadSave;

import static utils.Constants.*;
import static utils.Constants.Directions.*;
import static utils.HelpMethods.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static utils.Constants.GhostConstants.*;


public class Ghost extends Entity{

    private BufferedImage[][] animations;
    private int animationSpeed = 40; //Speed takes care of speed of animation
    //Index goes through animation array to go through all animation frames
    //Tick maintains animation rate dependent on speed
    private boolean left, right, jump; //THese variables check if these movement keys are being held down
    //Jump boolean used so player can hold down space bar to continuously jump
    private boolean isMoving = false, attacking = false; //Variable used to check if player is moving or not
    private int[][] lvlData; //Stores level data for player class to use to make a working hitbox
    private float xDrawOffset = 15 * Game.SCALE; //15 might need to be changed
    private float yDrawOffset = 4 * Game.SCALE; //4 might also need to be changed
    private float jumpSpeed = -2.75f * Game.SCALE; //Controls speed of player jump
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE; //Controls speed of player jump if they hit something

    //status bar UI
    private BufferedImage statusBarImg;

    //Actual coordinates and sizes of the entire status bar
    private int statusBarWidth = (int)(192 * Game.SCALE);
    private int statusBarHeight = (int)(58 * Game.SCALE);
    private int statusBarX = (int)(210 * Game.SCALE);
    private int statusBarY = (int)(10 * Game.SCALE);

    //coordinates and sizes of the health bar itself
    private int healthBarWidth = (int)(158 * Game.SCALE);
    private int healthBarHeight = (int)(4 * Game.SCALE);
    private int healthBarXStart = (int)(30 * Game.SCALE); //Starting x value of hp bar
    private int healthBarYStart = (int)(14 * Game.SCALE); //Starting y value of hp bar
    private int healthWidth = healthBarWidth; //Shrinks when player loses health

    private int powerBarWidth = (int)(104 * Game.SCALE);
    private int powerBarHeight = (int)(2 * Game.SCALE);
    private int powerBarXStart = (int)(44 * Game.SCALE);
    private int powerBarYStart = (int)(34 * Game.SCALE);
    private int powerWidth = powerBarWidth;
    private int powerMaxValue = 200;
    private int powerValue = powerMaxValue; //current power value


    //these flip variables help flip player animations when player turns left
    private int flipX = 0;
    private int flipW = 1;

    private boolean attackChecked = false;
    private Playing playing;

    private int playerAtk = 25;
    private int tileY = 0;
    private boolean powerAttackActive; //checks if power attack is active
    private int powerAttackTick;
    private int powerGrowSpeed = 15; //integer to slowly increase players power
    private int powerGrowTick; //ticker value for powerGrowSpeed
    private boolean hasEnergy = true; //checks to see if player has enough energy for power attack
    private boolean cloakActive = false;
    private boolean skipDecrement = false;

    public Ghost(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;
        this.state = IDLE; //sets player state to idle as soon as player is spawned
        this.maxHealth = 100; //sets players max health to 100
        this.currentHealth = 100; //sets current player health to max health of player
        this.walkSpeed = Game.SCALE * 1.0f;
        loadAnimations(); //loads all character animations
        //this init method uses the positions of hitbox not entity
        initHitbox(15,28); //initializes players hitbox
        //Recasted to ints because hitbox width and height should not be a decimal value
        initAttackBox();
    }

    public void setSpawn(Point spawn){ //immediately sets player spawn point within a level depending on level png
        this.x = spawn.x; //draws player in spawnpoint
        this.y = spawn.y;
        hitbox.x = x; //hitbox follows where player is spawned
        hitbox.y = y;
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int)(15 * Game.SCALE), (int)(20 * Game.SCALE)); //creates an attackBox for the players attacks
        //not yet offset (offset will be applied in update method)
        resetAttackBox(); //sets attackBox to proper position in front of player
    }

    public void update(){ //This method is necessary for all entities to be updated
        updateHealthBar();
        updatePowerBar();

        skipDecrement = !skipDecrement;

        if(cloakActive && skipDecrement){
            changePower(-1);
        }
        if(powerValue <= 0){
            toggleCloak();
        }

        if(currentHealth <= 0){
            if(state != DIE){
                state = DIE; //if player state has not changed to dead yet when players health is 0, set state to dead
                animationTick = 0; //resets animations
                animationIndex = 0;
                playing.setPlayer2Dying(true); //this method stops everything else in the game from updating when the player dies
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.DIE);
                // Check if player died in air
                if (!isEntityOnFloor(hitbox, lvlData)) {
                    inAir = true;
                    airSpeed = 0;
                }
            }
            //-4 was set to -1 before it was changed when debugging
            else if(animationIndex == getSpriteAmount(DIE) -4 && animationTick >= ANIMATION_SPEED -4){ //checks if animation is on its last index checks if animation tick is at the last tick of the animation
                playing.setGameOver(true); //if so, game is over
                playing.getGame().getAudioPlayer().stopSong(); //stop level song
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.GAMEOVER); //and instead play gameOver audio
            }
            else{
                updateAnimationTick(); //if none of the previous conditions are true, animation tick is simply updated normally

                // Fall if in air
                if (inAir)
                    if (canMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                        hitbox.y += airSpeed;
                        airSpeed += GRAVITY;
                    } else {
                        inAir = false;
                    }
            }

            return; //no more updates as player is dead
        }

        updateAttackBox();

        if (state == BLINK) {
            if (animationIndex <= getSpriteAmount(state) - 3)
                pushBack(pushBackDir, lvlData, 1.25f);
            updatePushBackDrawOffset();
        } else
            updatePosition(); //Updates player position dependent on if theyre running

        if(isMoving){
            checkPotionTouched(); //checks if player touched a potion while moving
            checkShardTouched(); //checks if player touched a shard while moving
            checkSpikesTouched(); //checks if player touched a spike while moving
            checkInsideWater(); //checks if player is inside water
            tileY = (int)(hitbox.y / Game.TILES_SIZE); //sets tileY value to the y value that the player is currently on when moving
            if(powerAttackActive){ //if power attack is active then it is updated
                powerAttackTick++;
                if(powerAttackTick >= 35){ //stops animation and attack after tick reaches 35
                    powerAttackTick = 0;
                    powerAttackActive = false;
                }
            }
        }
        if(attacking || powerAttackActive){ //if updated animation is an attack animation, check if attack does anything to enemies
            checkAttack();
        }
        updateAnimationTick(); //Goes through the animation
        setAnimation(); //Sets animation to idle or running
    }

    private void checkInsideWater() {
        if (IsEntityInWater(hitbox, playing.getLevelManager().getCurrentLevel().getLvlData())) {
            currentHealth = 0;
        }
    }
    private void checkSpikesTouched() {
        playing.checkSpikesTouched(this); //checks if this player touched a spike object
    }

    private void checkPotionTouched() {
        playing.checkPotionTouched(hitbox); //checks if player is touched a potion
    }

    private void checkShardTouched(){
        playing.checkShardTouched(hitbox); //checks if player touched a shard
    }

    private void checkAttack() {
        if(attackChecked || animationIndex != 2){
            return;
        }
        attackChecked = true; //If attack is yet to have been checked, it is set to true then it is checked below

        if(powerAttackActive){ //makes sure every update checks for a new attack so that power attack can do collateral damage
            attackChecked = false;
        }

        playing.checkEnemyHit(attackBox);
        playing.checkObjectHit(attackBox);
        playing.getGame().getAudioPlayer().playAttackSound();
    }

    private void updateAttackBox() {
        if(right && left){
            if(flipW == 1){
                attackBox.x = hitbox.x + hitbox.width + (int)(Game.SCALE * 5);
            }
            else{
                attackBox.x = hitbox.x - hitbox.width - (int)(Game.SCALE * 5);
            }
        }
        else if(right || powerAttackActive && flipW == 1){ //If player is running right, hitbox should be on the players right
            attackBox.x = hitbox.x + hitbox.width + (int)(Game.SCALE * 5); //attackbox follows player hitbox with some offset
        }
        else if(left || powerAttackActive && flipW == -1){ //If player is running left, hitbox should be on the players left
            attackBox.x = hitbox.x - hitbox.width - (int)(Game.SCALE * 5);
        }
        attackBox.y = hitbox.y + (Game.SCALE * 10); //moves the attack hitbox a little bit lower than the player
        //^maybe better off not having this
    }

    private void updateHealthBar() {
        //one value has to be set to float
        healthWidth = (int)((currentHealth / (float)maxHealth) * healthBarWidth); //takes currentHealth divided by maxHealth then multiples it by original healthbar size
    }

    private void updatePowerBar(){
        powerWidth = (int)(powerValue / (float)(powerMaxValue) * powerBarWidth); //powerBar width shrinks along with powerValue integer

        powerGrowTick++;
        if(powerGrowTick >= powerGrowSpeed){ //if tick is greater than speed, reset tick
            powerGrowTick = 0;
            changePower(1); //increases power by 1
        }
    }

    public void render(Graphics g, int xLvlOffset){ //This method is necessary for all entities to be rendered
        //when player is drawn, adds flipX which would be equal to width if player was facing left otherwise 0, also multiples width by -1 if facing left otherwise multiplies width by 1 (used to flip player animations)
        g.drawImage(animations[state][animationIndex], (int)(hitbox.x - xDrawOffset) - xLvlOffset + flipX, (int)(hitbox.y - yDrawOffset), width * flipW, height, null); //Draws player character relative to subImg
        //drawHitbox(g, xLvlOffset); //Draws hitbox right after player is drawn USED FOR DEBUGGING
        //drawAttackBox(g, xLvlOffset);
        drawUI(g); //Draws status bar UI
    }

    public void drawUI(Graphics g){
        //background UI
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null); //Draws status bar
        //Health UI
        g.setColor(Color.RED);
        g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
        //addition of statusBar coordinates fixes offset value so that hp bar appears where its supposed to in the UI
        //Power Bar
        g.setColor(Color.YELLOW);
        g.fillRect(powerBarXStart + statusBarX, powerBarYStart + statusBarY, powerWidth, powerBarHeight);

    }






    private void updateAnimationTick() {

        animationTick++;

        if(animationTick >= animationSpeed){
            animationTick = 0;
            animationIndex++;
            if(animationIndex >= getSpriteAmount(state)){ //this condition needs to change according to how many frames r needed
                animationIndex = 0; //CAUSES RUN ANIMATION TO HAVE 1 CROUCH FRAME
                attacking = false; //Just in case its true, it will be set to false and attacking animation will stop
                attackChecked = false; //after an attack animation, next attack animation would not have been checked so it is set to false
                if (state == BLINK) {
                    newState(IDLE);
                    airSpeed = 0f;
                    if (!isFloor(hitbox, 0, lvlData))
                        inAir = true;
                }
            }
        }
    }

    private void setAnimation() { //Sets animation to moving if player is moving otherwise sets animation to IDLE

        animationSpeed = 40; //Defaults animation speed back to 40

        int startAnimation = state;

        if(isMoving){
            state = WALK;
        }
        else{
            state = IDLE;
        }

        if(inAir){
            if(airSpeed < 0){ //Going up
                state = JUMP;
            }
            else{ //Going down
                state = IDLE;
            }
        }

        if(cloakActive){ //power attack freezes attack onto one frame
            state = CLOAK;
            animationIndex = 2;
            animationTick = 0;
            return;
        }

        if(attacking){
            animationSpeed = 20;
            state = ATTACK;
            if(startAnimation != ATTACK){ //Checks if attack is a new attack (was not attacking when entering this method)
                animationIndex = 2; //attack animation speeds up
                animationTick = 0;
                return; //returns so that start animation when run again is set to attack
            }
        }

        if(startAnimation != state){ //If startAnimation is not equal to the playerAction after the checks above
            resetAnimationTick();
        }


    }

    private void resetAnimationTick() {
        animationTick = 0;
        animationIndex = 0; //CAUSES RUN ANIMATION TO HAVE 1 CROUCH FRAME
    }

    private void updatePosition() { //Updates player position if they're moving

        isMoving = false; //Sets isMoving to false by default

        if(jump){
            jump();
        }
//        if(!left && !right && !inAir){ //When player isnt in air or moving left or right, they are standing still
//            return;
//        }

        if(!inAir){
            if(!powerAttackActive) {
                if ((!left && !right) || (right && left)) { //checks if both left and right are false OR right and left are true
                    return;
                }
            }
        }

        float xSpeed = 0; //Temporary speed of player by x

        if(left && !right){

            xSpeed -= walkSpeed; //Moves player by negative playerSpeed
            flipX = width; //flips x by width
            flipW = -1; //sets flipwidth to -1
        }
        if(right && !left){
            xSpeed += walkSpeed; //Moves player by playerSpeed
            flipX = 0; //resets flipX and flipW to default
            flipW = 1;
        }

        if(powerAttackActive){
            if((!left && !right) || (left && right)){
                if(flipW == -1){
                    //xSpeed = -walkSpeed; //makes xSpeed equal opposite of walkSpeed so player moves left
                }
                else{
                    //xSpeed = walkSpeed; //otherwise player continues to the right
                }
            }

            xSpeed *= 3; //boosts player speed if player is stationary in a power attack
        }

        if(!inAir){
            if(!isEntityOnFloor(hitbox, lvlData)){ //If player isnt on floor, then they are in the air
                inAir = true; //This condition is basically just used to make sure when player walks off a platform, they fall down
            }
        }

        if(inAir && !powerAttackActive){
            if(canMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)){
                hitbox.y += airSpeed; //Moves player and hitbox through the air using airspeed
                airSpeed += GRAVITY; //Gravity makes speed in air gradually inc or dec depending on direction
                updateXPosition(xSpeed); //This method already checks if we can move left or right
            }
            else{
                hitbox.y = getEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                if(airSpeed > 0){ //If player moved downwards and hit something...
                    resetInAir(); //...Then this method is called
                }
                else{ //This condition means player hit roof instead of moving downwards
                    airSpeed = fallSpeedAfterCollision; //airspeed becomes the value of fallspeedaftercollision, which will always be less than airSpeed
                    updateXPosition(xSpeed);
                }

            }

        }
        else{
            updateXPosition(xSpeed);
        }

        isMoving = true; //if first condition doesnt take us out of this method, that means player is moving


    }

    private void jump() {
        if(inAir){
            return;
        }
        playing.getGame().getAudioPlayer().playEffect(AudioPlayer.JUMP);
        inAir = true;
        airSpeed = jumpSpeed; //Initial speed of player in air when they first jump

    }

    private void resetInAir() { //resets air values including being in air to false and speed of air movement back to 0
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPosition(float xSpeed) {
        if(canMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)){
            hitbox.x += xSpeed; //If player can move there, players coordinates are moved there
        }
        else{
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
            if(powerAttackActive){ //as soon as player hits something while in a power attack
                powerAttackActive = false;
                powerAttackTick = 0; //resets powerAttack timing
            }
        }
    }

//    public void changeHealth(int value){
//        currentHealth += value;
//
//        if(currentHealth <= 0){ //Checks if current health is below or equal to 0
//            currentHealth = 0; //sets current health to 0, just in case health drops down to negative values
//            //gameOver();
//        }
//        else if(currentHealth >= maxHealth){ //Checks if current health exceeds max health
//            currentHealth = maxHealth; //makes sure health doesnt exceed max amount
//        }
//    }

    public void changeHealth(int value) {
        if (value < 0) {
            if (state == BLINK)
                return;
            else
                newState(BLINK);
        }
        if(value > 100){
            value = 100;
        }

        currentHealth += value;
        currentHealth = Math.max(Math.min(currentHealth, maxHealth), 0);
    }

    public void changeHealth(int value, Enemy e) {
        if (state == BLINK)
            return;
        changeHealth(value);
        pushBackOffsetDir = UP;
        pushDrawOffset = 0;

        if (e.getHitbox().x < hitbox.x)
            pushBackDir = RIGHT;
        else
            pushBackDir = LEFT;
    }

    public void setHealth(int value){
        currentHealth = value;
    }

    public void kill() {
        currentHealth = 0; //sets player health to 0
    }



    public void changePower(int value){ //probably useless when ghost potion effects are added
        powerValue += value;
        if(powerValue >= powerMaxValue){
            powerValue = powerMaxValue;
        }
        else if(powerValue <= 0){
            powerValue = 0;
        }
    }

    private void loadAnimations() {

        //Get rid of this array as well as starting index when animations png is fixed
        BufferedImage img = LoadSave.getSpriteAtlas(LoadSave.PLAYER2_ATLAS); //Loads player atlas image from LoadSave class in utils folder using final string for file name

        animations = new BufferedImage[9][8]; //Puts all subimages in atlas into a 2d array to traverse through animations

        for(int j = 0; j < animations.length; j++){
            for(int i = 0; i < animations[j].length; i++){
                animations[j][i] = img.getSubimage(i * 32, j * 32, 32, 32);
            }
        }

        statusBarImg = LoadSave.getSpriteAtlas(LoadSave.STATUS_BAR);

    }

    public void loadLvlData(int[][] lvlData){
        this.lvlData = lvlData;
        if(!isEntityOnFloor(hitbox, lvlData)){ //Checks if player is on floor when they spawn in
            inAir = true;
        }
    }

    public void resetDirectionalBooleans() {
        left = false;
        right = false;
    }

    public void setAttacking(boolean attacking){
        this.attacking = attacking;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setJump(boolean jump){
        this.jump = jump;
    }

    public int getPlayerAtk(){
        return playerAtk;
    }


    public void resetAll() { //resets everything related to player
        resetDirectionalBooleans(); //when player is completely resetm directional bools r reset
        inAir = false; //sets all moving and animation booleans to false
        attacking = false;
        isMoving = false;
        airSpeed = 0f;
        state = IDLE; //sets player action back to idle
        currentHealth = maxHealth; //resets player health

        hitbox.x = x; //x and y are never used so they can be used as reset values for player position
        hitbox.y = y;

        resetAttackBox(); //resets attackbox coordinates

        if (!isEntityOnFloor(hitbox, lvlData)) {
            inAir = true;
        }

    }

    private void resetAttackBox(){
        if(flipW == 1){
            attackBox.x = hitbox.x + hitbox.width + (int)(Game.SCALE * 5);
        }
        else{
            attackBox.x = hitbox.x - hitbox.width - (int)(Game.SCALE * 5);
        }
    }

    public int getTileY(){
        return tileY;
    }

    public void powerAttack() {

        if(powerAttackActive){ //checks to make sure power attack isnt already active
            return;
        }
        if(powerValue >= 60){ //only if player has at least 60 power | SUBJECT TO CHANGE
            powerAttackActive = true;
            toggleCloak();
            //changePower(-60); //subtracts 60 power | SUBJECT TO CHANGE
        }
        else{
            hasEnergy = false;
        }
    }

    public void toggleCloak() {

        if(cloakActive){ //checks to make sure power attack isnt already active
            cloakActive = false;
            return;
        }
        if(powerValue >= 0){ //only if player has at least 60 power | SUBJECT TO CHANGE
            cloakActive = true;
        }
        else{
            hasEnergy = false;
        }
    }

    public boolean isCloakActive(){
        return cloakActive;
    }

}
