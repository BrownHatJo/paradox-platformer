package objects;

//This class manages all the different objects in the game
//The main use of this class is to draw and update all objects used in the game
//this includes potions, spikes, projectiles, etc.

import entities.Adventurer;
import entities.Enemy;
import entities.Ghost;
import entities.ParadoxGolem;
import gamestates.Playing;
import levels.Level;
import main.Game;
import utils.HelpMethods;
import utils.LoadSave;


import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import static utils.Constants.ObjectConstants.*;
import static utils.Constants.Projectiles.*;

public class ObjectManager {

    private Playing playing;
    private BufferedImage[][] itemImgs, containerImgs; //potions imgs | container imgs
    private BufferedImage[] cannonImgs; //cannon imgs
    private BufferedImage spikeImg; //spike img
    private BufferedImage cannonBallImg; //cannon ball img
    private BufferedImage golemSpikeImg; //golem spike img
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;
    private ArrayList<ParadoxShards> shards;
    private int shardsCollected = 0;
    private ArrayList<Spikes> spikes;
    private ArrayList<Cannon> cannons;
    private ArrayList<Projectile> projectiles = new ArrayList<>(); //every time a projectile is shot, it is added to this array
    private static ArrayList<GolemProjectile> golemProjectiles = new ArrayList<>(); //every time a projectile is shot, it is added to this array
    private Level currentLevel;

    private BufferedImage[] laserImgs;
    static ArrayList<Laser> lasers = new ArrayList<>();



    public ObjectManager(Playing playing) { //creates object manager to be used in playing class
        this.playing = playing;
        currentLevel = playing.getLevelManager().getCurrentLevel();
        loadImgs();
    }

    //checks if player touches any spikes in a level, if so player dies
    public void checkSpikesTouched(Adventurer p){
        for(Spikes s : spikes){
            if(s.getHitbox().intersects(p.getHitbox())){ //checks if spike hitbox is touching player hitbox
                p.kill();
            }
        }
    }

    public void checkSpikesTouched(Ghost g){
        for(Spikes s : spikes){
            if(s.getHitbox().intersects(g.getHitbox())){ //checks if spike hitbox is touching player hitbox
                g.kill();
            }
        }
    }

    public void checkPotionTouched(Rectangle2D.Float hitbox){ //Checks if players hitbox overlaps a potion/shards hitbox
        for(Potion p : potions){
            if(p.isActive()){
                if(hitbox.intersects(p.getHitbox())){ //if potion hitbox intersects player hitbos
                    p.setActive(false);
                    applyEffectToPlayer(p); //applies effect to player (subject to change)
                    applyEffectToPlayer2(p); //THIS NEEDS TO CHANGE BECAUSE CURRENTLY BOTH PLAYERS ARE GETTING EFFECTS APPLIED TO THEM
                }
            }
        }
    }

    public void checkShardTouched(Rectangle2D.Float hitbox){ //Checks if players hitbox overlaps a potion/shards hitbox
        for(ParadoxShards s : shards){
            if(s.isActive()){
                if(hitbox.intersects(s.getHitbox())){ //if potion hitbox intersects player hitbos
                    s.setActive(false);
                    shardsCollected++;
                }
            }
        }
    }

    public boolean allShardsCollected(){
        if(shardsCollected >= shards.size()){
            shardsCollected = 0;
            return true;
        }
        return false;
    }

    //WILL NEED TO BE CHANGED WHEN NIGHTBORNE MECHANICS ARE IMPLEMENTED
    public void applyEffectToPlayer(Potion p){ //applies an effect to player depending on potion
        if(p.getObjType() == NIGHTBORNE_POTION){
            playing.getPlayer().changeHealth(NIGHTBORNE_POTION_VALUE);
        }
        else{ //ELSE if ghost potion was applied
            playing.getPlayer().changePower(GHOST_POTION_VALUE);
        }
    }

    public void applyEffectToPlayer2(Potion p){ //applies an effect to player depending on potion
        if(p.getObjType() == NIGHTBORNE_POTION){
            playing.getPlayer2().changeHealth(NIGHTBORNE_POTION_VALUE);
        }
        else{ //ELSE if ghost potion was applied
            playing.getPlayer2().changePower(GHOST_POTION_VALUE);
        }
    }

    public void checkObjectHit(Rectangle2D.Float attackBox){ //checks if object was hit by player
        for(GameContainer gc: containers){
            if(gc.isActive() && !gc.doAnimation){ //only if container is active and not already undergoing a destroy
                if(gc.getHitbox().intersects(attackBox)){ //if players attackbox intersects the containers hitbox
                    gc.setAnimation(true); //when it is hit, play the destroy animation
                    int type = 0; //default nightborne potion (spawns inside box)
                    if(gc.getObjType() == BARREL){ //if obj is a barrel
                        type = 1; //ghost potion spawns inside barrels
                    }
                    potions.add(new Potion((int)(gc.getHitbox().x + gc.getHitbox().width / 2), (int)(gc.getHitbox().y - gc.getHitbox().height / 2), type));
                    //creates a potion at the center of the container
                    return;
                }
            }
        }
    }

    public void loadObjects(Level newLevel) { //loads all game objects into a level
        potions = new ArrayList<>(newLevel.getPotions()); //remakes new ArrayLists for only the levels potions instead of all the potions in the game
        containers = new ArrayList<>(newLevel.getContainers());//remakes new ArrayLists for only the levels containers instead of all the containers in the game
        shards = new ArrayList<>(newLevel.getShards());//remakes new ArrayLists for only the levels shards instead of all the shards in the game
        spikes = newLevel.getSpikes(); //remakes are not necessary as spikes/cannons are never reset (or destroyed) for each level
        cannons = newLevel.getCannons();
        projectiles.clear(); //clears projectiles arraylist everytime a new level is started
        golemProjectiles.clear();
    }

    private void loadImgs() { //loads all the images for all the objects that are used in the game
        BufferedImage potionSprite = LoadSave.getSpriteAtlas(LoadSave.ITEM_ATLAS);
        itemImgs = new BufferedImage[3][7]; //THIS INCLUDES THE PARADOX SHARDS

        for(int j = 0; j < itemImgs.length; j++){
            for(int i = 0; i < itemImgs[j].length; i++) {
                itemImgs[j][i] = potionSprite.getSubimage(12 * i, 16 * j, 12, 16);
            }
        } //This creates a 2d array of images from ITEM_ATLAS to be used to animate potions and paradox shards

        BufferedImage containerSprite = LoadSave.getSpriteAtlas(LoadSave.OBJECT_ATLAS);
        containerImgs = new BufferedImage[2][8]; //only barrels and boxes

        for(int j = 0; j < containerImgs.length; j++){
            for(int i = 0; i < containerImgs[j].length; i++) {
                containerImgs[j][i] = containerSprite.getSubimage(40 * i, 30 * j, 40, 30);
            }
        } //this creates a 2d array of images from OBJECT_ATLAS to be used to animate containers such as barrels and boxes

        spikeImg = LoadSave.getSpriteAtlas(LoadSave.TRAP_ATLAS);

        cannonImgs = new BufferedImage[7];
        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.CANNON_ATLAS);
        for(int i = 0; i < cannonImgs.length; i++){
            cannonImgs[i] = temp.getSubimage(i * 40, 0, 40, 26);
        }

        cannonBallImg = LoadSave.getSpriteAtlas(LoadSave.CANNON_BALL); //single cannon ball image is loaded

        golemSpikeImg = LoadSave.getSpriteAtlas(LoadSave.GOLEM_PROJECTILE); //single golem projectile image is loaded

        laserImgs = new BufferedImage[13];
        BufferedImage temp2 = LoadSave.getSpriteAtlas(LoadSave.LASER);
        for (int y = 0; y < laserImgs.length; y++) {
            laserImgs[y] = temp2.getSubimage(0, 100 * y, 1000, 100);
        }

    }

    public void update(int[][] lvlData, Adventurer player, Ghost ghost){ //checks and updates all active objects in each level
        for(Potion p : potions){ //checks and updates all active potions in a level
            if(p.isActive()){
                p.update();
            }
        }
        for(GameContainer gc: containers){ //checks and updates all active containers in a level
            if(gc.isActive()){
                gc.update();
            }
        }
        for(ParadoxShards s : shards){
            if(s.isActive()){
                s.update();
            }
        }
        for (Laser laser : lasers) {
            laser.update(laser);
        }

        updateCannons(lvlData, player, ghost);
        updateProjectiles(lvlData, player, ghost);

    }

    private void updateProjectiles(int[][] lvlData, Adventurer player, Ghost ghost) { //updates cannon balls and checks if they hit a player or solid tile
        for(Projectile p : projectiles){
            if(p.isActive()){
                p.updatePosition();
                if(p.getHitbox().intersects(player.getHitbox())){ //if projectile hit player
                    player.changeHealth(-30); //player takes big damage
                    p.setActive(false); //sets projectile as inactive after hitting player
                }
                else if(p.getHitbox().intersects(ghost.getHitbox())){
                    ghost.changeHealth(-30);
                    p.setActive(false);
                }
                else if(HelpMethods.IsProjectileHittingLevel(p, lvlData)){ //helpmethod that checks if projectile is hitting a tile in the level
                    p.setActive(false); //makes projectile inactive
                }
            }
        }
        for(GolemProjectile p : golemProjectiles){
            if(p.isActive()){
                p.updatePosition();
                if(p.getHitbox().intersects(player.getHitbox())){ //if projectile hit player
                    player.changeHealth(-100); //player takes big damage
                    p.setActive(false); //sets projectile as inactive after hitting player
                }
                else if(HelpMethods.IsProjectileHittingLevel(p, lvlData)){ //helpmethod that checks if projectile is hitting a tile in the level
                    p.setActive(false); //makes projectile inactive
                }
            }
        }
    }

    private boolean isPlayerInRange(Cannon c, Adventurer player) {
        int absValue = (int)Math.abs(player.getHitbox().x - c.getHitbox().x);
        return absValue <= Game.TILES_SIZE * 5; //if player is within 5 tiles, player is in range
    }

    private boolean isGhostInRange(Cannon c, Ghost ghost) {
        int absValue = (int)Math.abs(ghost.getHitbox().x - c.getHitbox().x);
        return absValue <= Game.TILES_SIZE * 5; //if player is within 5 tiles, player is in range
    }

    private boolean ifPlayerInFrontOfCannon(Cannon c, Adventurer player) {
        if(c.getObjType() == CANNON_LEFT){ //if cannon is pointing left
            if(c.getHitbox().x > player.getHitbox().x){ //and if player is in front of the cannon
                return true;
            }
        }
        else if(c.getHitbox().x < player.getHitbox().x){ //else cannon is pointing left, therefore checks if player is in front of right facing cannon
            return true;
        }
        return false;
    }

    private boolean ifGhostInFrontOfCannon(Cannon c, Ghost ghost) {
        if(c.getObjType() == CANNON_LEFT){ //if cannon is pointing left
            if(c.getHitbox().x > ghost.getHitbox().x){ //and if player is in front of the cannon
                return true;
            }
        }
        else if(c.getHitbox().x < ghost.getHitbox().x){ //else cannon is pointing left, therefore checks if player is in front of right facing cannon
            return true;
        }
        return false;
    }

    private void updateCannons(int[][] lvlData, Adventurer player, Ghost ghost) {
        for(Cannon c : cannons){
            if(!c.doAnimation){ //if cannon is not already animationg
                if(c.getTileY() == player.getTileY()){
                    if(isPlayerInRange(c, player)){ //if player is in range of cannon
                        if(ifPlayerInFrontOfCannon(c, player)){ //checks if player is in front of cannon
                            if(HelpMethods.CanCannonSeePlayer(lvlData, player.getHitbox(), c.getHitbox(), c.getTileY())){ //helpMethod method that checks if cannon can see player
                                c.setAnimation(true); //sets animation to true when cannon sees player
                            }
                        }
                    }
                }
                if(c.getTileY() == ghost.getTileY()){
                    if(isGhostInRange(c, ghost)){ //if player is in range of cannon
                        if(ifGhostInFrontOfCannon(c, ghost) && !ghost.isCloakActive()){ //checks if player is in front of cannon
                            if(HelpMethods.CanCannonSeePlayer(lvlData, ghost.getHitbox(), c.getHitbox(), c.getTileY())){ //helpMethod method that checks if cannon can see player
                                c.setAnimation(true); //sets animation to true when cannon sees player
                            }
                        }
                    }
                }
            }

            c.update();
            if(c.getAnimationIndex() == 4 && c.getAnimationTick() == 0){
                shootCannon(c);
            }
        }
    }

    private void shootCannon(Cannon c) { //creates a new cannonball fired in a specific direcition depending on cannon direction
        int dir = 1;
        if(c.getObjType() == CANNON_LEFT){
            dir = -1;
        }
        projectiles.add(new Projectile((int)(c.getHitbox().x), (int)(c.getHitbox().y), dir));
    }

    public static void golemShoot(ParadoxGolem p){ //fires golem projectile depending on direction golem is facing
        int dir = 1;
        if(p.flipW() == -1){
            dir = -1;
        }
        golemProjectiles.add(new GolemProjectile((int)(p.getHitbox().x), (int)(p.getHitbox().y), dir));
    }

    public static void laserAttack(ParadoxGolem p, int[][] lvlData, Playing playing){ //creates a new laser that fires all the way until a solid tile is hit
        int xTile = (int) (p.getHitbox().x / Game.TILES_SIZE);
        int yTile = (int) (p.getHitbox().y / Game.TILES_SIZE);
        int right = 1;
        int left = -1;
        int distanceRight = HelpMethods.distanceToFirstSolidTile(xTile, yTile, right, lvlData);
        int distanceLeft = HelpMethods.distanceToFirstSolidTile(xTile, yTile, left, lvlData);

        if (p.flipW() == right) {
            lasers.add(new Laser((int) (p.getHitbox().x), (int)(p.getHitbox().y - 20), right, distanceRight, playing));
        } else if (p.flipW() == left) {
            lasers.add(new Laser((int) (p.getHitbox().x), (int)(p.getHitbox().y - 20), left, distanceLeft, playing));
        }
    }

    public void draw(Graphics g, int xLvlOffset){
        drawPotions(g, xLvlOffset);
        drawContainers(g, xLvlOffset);
        drawShards(g, xLvlOffset);
        drawTraps(g, xLvlOffset);
        drawCannons(g, xLvlOffset);
        drawProjectiles(g, xLvlOffset);
        drawGolemProjectiles(g, xLvlOffset);
        drawLasers(g, xLvlOffset);
    }

    private void drawProjectiles(Graphics g, int xLvlOffset) { //draws cannon balls
        for(Projectile p : projectiles){
            if(p.isActive()){
                g.drawImage(cannonBallImg, (int)(p.getHitbox().x - xLvlOffset), (int)(p.getHitbox().y), CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT, null);
            }
        }
    }

    private void drawGolemProjectiles(Graphics g, int xLvlOffset) { //draws golem projectiles
        for(GolemProjectile p : golemProjectiles){
            int x = (int)(p.getHitbox().x - xLvlOffset);
            int width = GOLEM_PROJECTILE_WIDTH;
            if(p.getDirection() == -1){
                x += width; //adds width to x coordinate
                width *= -1; //flips width
            }
            if(p.isActive()){
                g.drawImage(golemSpikeImg, x, (int)(p.getHitbox().y), width, GOLEM_PROJECTILE_HEIGHT, null);
            }
        }
    }

    private void drawCannons(Graphics g, int xLvlOffset) {
        for(Cannon c : cannons){
            int x = (int)(c.getHitbox().x - xLvlOffset); //these x and width integers are used to flip cannon image when its facing right
            int width = CANNON_WIDTH;
            if(c.getObjType() == CANNON_RIGHT){ //if cannon is facing right, sprite needs to be flipped
                x += width; //adds width to x coordinate
                width *= -1; //flips width
            }
            g.drawImage(cannonImgs[c.getAnimationIndex()], x, (int)(c.getHitbox().y), width, CANNON_HEIGHT, null);
        }
    }

    private void drawTraps(Graphics g, int xLvlOffset) { //draws traps
        for(Spikes s: spikes){
            g.drawImage(spikeImg, (int)(s.getHitbox().x - xLvlOffset), (int)(s.getHitbox().y - s.getyDrawOffset()), SPIKE_WIDTH, SPIKE_HEIGHT, null);
        }
    }

    private void drawShards(Graphics g, int xLvlOffset) {
        for(ParadoxShards s: shards){ //checks and updates all active containers in a level
            if(s.isActive()){
                g.drawImage(itemImgs[2][s.getAnimationIndex()], (int)(s.getHitbox().x - s.getxDrawOffset() - xLvlOffset), (int)(s.getHitbox().y - s.getyDrawOffset()), ITEM_WIDTH, ITEM_HEIGHT,  null);
                //Shards hitbox subtracted by its respective image offset as well as the actual level offset | also Constant width and height of Shard
            }
        }
    }

    private void drawContainers(Graphics g, int xLvlOffset) {
        for(GameContainer gc: containers){ //checks and updates all active containers in a level
            if(gc.isActive()){
                int type = 0; //used to see if container is a box or barrel set to 0 (box value) by default and checked if it is a barrel instead
                if(gc.getObjType() == BARREL){
                    type = 1;
                }

                g.drawImage(containerImgs[type][gc.getAnimationIndex()], (int)(gc.getHitbox().x - gc.getxDrawOffset() - xLvlOffset), (int)(gc.getHitbox().y - gc.getyDrawOffset()), CONTAINER_WIDTH, CONTAINER_HEIGHT,  null);
                //containers hitbox subtracted by its respective image offset as well as the actual level offset | also Constant width and height of container
            }
        }
    }

    private void drawPotions(Graphics g, int xLvlOffset) {
        for(Potion p : potions){ //checks and updates all active potions in a level
            if(p.isActive()){
                int type = 0; //type is set to nightborne potion by default
                if(p.getObjType() == GHOST_POTION){ //Checked to see if type is ghost potion instead
                    type = 1;
                }

                g.drawImage(itemImgs[type][p.getAnimationIndex()], (int)(p.getHitbox().x - p.getxDrawOffset() - xLvlOffset), (int)(p.getHitbox().y - p.getyDrawOffset()), ITEM_WIDTH, ITEM_HEIGHT,  null);

            }
        }
    }

    //draws laser depending on how far it can reach before hitting a solid tile, and also dependent on golem direction
    private void drawLasers(Graphics g, int xLvlOffset) {
        for (Laser laser : lasers) {
            if (laser.isActive()) {
                int i = laser.getAnimationIndex();
                int x = (int) (laser.getLaserAttackBox().x - xLvlOffset);
                int y = (int) (laser.getLaserAttackBox().y);

                // calculate length to draw laser beam
                //USED FOR DEBUGGING
                //System.out.println("distance! " + laser.getDistanceToClosestTile());

                int width = (int) (LASER_W * 1.0);
                int laserDistance = (int) (laser.getDistanceToClosestTile() * Game.TILES_SIZE * 1.0);
                int widthFlip = laserDistance * -1;

                //laser.drawHitbox(g, xLvlOffset);

                if (laser.getDirection() == 1) {
                    g.drawImage(laserImgs[i], x, y, laserDistance, LASER_H, null);
                }
                else if (laser.getDirection() == -1) {
                    g.drawImage(laserImgs[i], x + width, y, widthFlip, LASER_H, null);
                }
            }
        }
    }

    public void resetAllObjects(){ //resets all individual potions, gamecontainers and shards
        //System.out.println("Size of array: " + potions.size() + " - " + containers.size() + " - " + shards.size());
        loadObjects(playing.getLevelManager().getCurrentLevel()); //loads all objects necessary for a specific level that needs to be reset
        //this ensures number of potions in the arrayList doesnt add up continously and is reset per level
        for(Potion p : potions){
            p.reset();
        }
        for(GameContainer gc : containers){
            gc.reset();
        }
        for(ParadoxShards s : shards){
            s.reset();
        }
        for(Cannon c : cannons){
            c.reset();
        }
        for(Laser l : lasers){
            l.reset();
        }

    }

    public void setShardsCollected(int value){
        shardsCollected = value;
    }

    public ArrayList<GolemProjectile> getGolemProjectiles(){
        return golemProjectiles;
    }

}
