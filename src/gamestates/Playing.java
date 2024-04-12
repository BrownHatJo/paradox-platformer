package gamestates;

//This class takes care of everything that is going on while the game is being played
//this includes taking care of keyboard and mouse inputs for player actions, updating the background, etc.

import entities.Adventurer;
import entities.EnemyManager;
import entities.Ghost;
import levels.LevelManager;
import main.Game;
import objects.ObjectManager;
import ui.GameCompletedOverlay;
import ui.GameOverOverlay;
import ui.LevelCompletedOverlay;
import ui.PauseOverlay;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static utils.Constants.Environment.*;

public class Playing extends State implements Statemethods{
    private Adventurer player; //Adventurer is player1
    private Ghost player2; //Ghost is player2
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private ObjectManager objectManager;
    private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverlay;
    private GameCompletedOverlay gameCompletedOverlay;
    private LevelCompletedOverlay levelCompletedOverlay;
    private static boolean isPaused = false; //Boolean to check if game is paused or not | set to false by default

    private int xLvlOffset; //Offset value used to draw everything to the left/right of player if player tries to move off screen
    private int leftBorder = (int)(0.2 * Game.GAME_WIDTH); //A line in which if player moves beyond, checks if player can move there
    private int rightBorder= (int)(0.8 * Game.GAME_WIDTH); //A line in which if player moves beyond, checks if player can move there
      private int maxLvlOffsetX; //max x offset

    private BufferedImage backgroundImg1, backgroundImg2, backgroundImg3;

    private boolean gameOver = false; //game over boolean set to false by default
    private boolean lvlCompleted;
    private boolean gameCompleted;
    private boolean playerDying = false;
    private boolean player2Dying;


    public Playing(Game game) {
        super(game);
        initClasses();

        backgroundImg1 = LoadSave.getSpriteAtlas(LoadSave.PLAYING_BACKGROUND_IMG1);
        backgroundImg2 = LoadSave.getSpriteAtlas(LoadSave.PLAYING_BACKGROUND_IMG2);
        backgroundImg3 = LoadSave.getSpriteAtlas(LoadSave.PLAYING_BACKGROUND_IMG3);

        calcLvlOffset(); //calculates max x level offset for start level
        loadStartLevel();
    }

    public void loadNextLevel(){
        levelManager.setLevelIndex(levelManager.getLvlIndex() + 1);
        levelManager.loadNextLevel();
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn()); //sets player spawn for next level
        player2.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
        resetAll(); //resets all booleans
    }

    private void loadStartLevel() { //loads enemies into current level as well as objects at the start level
        enemyManager.loadEnemies(levelManager.getCurrentLevel());
        objectManager.loadObjects(levelManager.getCurrentLevel());
    }

    private void calcLvlOffset() {
        maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffset();
    }


    private void initClasses() { //Initializes entities for now, other stuff later

        levelManager = new LevelManager(game); //Creates levelManager object which in turn creates all game levels
        enemyManager = new EnemyManager(this); //Creates enemyManager object which in turn creates all enemies of all game levels
        objectManager = new ObjectManager(this); //Creates objectManager object which in turn is used to create all the objects in every level that is used in the game

        player = new Adventurer((int)(200 * Game.SCALE), (int)(200 * Game.SCALE), (int)(50 * Game.SCALE), (int)(37 * Game.SCALE), this);
        //Multiplying spawn point of player by game scale fixed issue of player spawning in walls when scale was changed
        //The width in height for the player helps scale the player along with level

        player.loadLvlData(levelManager.getCurrentLevel().getLvlData());
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn()); //sets player spawn depending on current level

        //for player 2
        player2 = new Ghost((int)(200 * Game.SCALE), (int)(200 * Game.SCALE), (int)(32 * Game.SCALE), (int)(32 * Game.SCALE), this);
        player2.loadLvlData(levelManager.getCurrentLevel().getLvlData());
        player2.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());

        pauseOverlay = new PauseOverlay(this); //Creates the pause overlay screen object with playing class as parameters
        gameOverOverlay = new GameOverOverlay(this); //Creates the game over overlay screen object with playing class as parameters
        levelCompletedOverlay = new LevelCompletedOverlay(this);
        gameCompletedOverlay = new GameCompletedOverlay(this);

    }


    @Override
    public void update() {
        if(isPaused){
            pauseOverlay.update(); //Updates pause UI
        }
        else if(lvlCompleted){
            levelCompletedOverlay.update();
        }
        else if(gameCompleted){
            gameCompletedOverlay.update();
        }
        else if(gameOver){ //if the game is over and player has died
            gameOverOverlay.update();
        }
        else if(playerDying){ //else if player is currently dying
            player.update(); //updates player to dying animation
        }
        else if(player2Dying){
            player2.update();
        }
        else{ //as long as game isnt over
            levelManager.update(); //Updates levels in levelManger
            objectManager.update(levelManager.getCurrentLevel().getLvlData(), player, player2);
            enemyManager.update(levelManager.getCurrentLevel().getLvlData(), player); //in turn updates all enemies
            if(levelManager.getLvlIndex() == 0 || levelManager.getLvlIndex() == 2 || levelManager.getLvlIndex() == 4){
                if(levelManager.getLvlIndex() == 4 || levelManager.getLvlIndex() == 5){
                    player2.update();
                }
                player.update(); //Updates player
                checkCloseToBorder();
            }
            else if(levelManager.getLvlIndex() == 1 || levelManager.getLvlIndex() == 3){
                P2checkCloseToBorder(); //fix this to work
                player2.update();
            }
            else{
                player.update(); //Updates player
                player2.update();
                checkCloseToBorder();
            }
        }
    }


    private void checkCloseToBorder() { //Checks if player is close to left or right border
        int playerX = (int)player.getHitbox().x; //Gets current player x value
        int diff = playerX - xLvlOffset; //difference between player x value and valid offset

        if(diff > rightBorder){ //if diff is greater than right border
            xLvlOffset += diff - rightBorder; //difference of diff and right border is added to x offset
        }
        else if(diff < leftBorder){ //also checks left border
            xLvlOffset += diff - leftBorder; //difference of diff and right border is added to x offset
        }

        if(xLvlOffset > maxLvlOffsetX){ //Makes sure offset value doesnt get too high and go over the actual amount possible
            xLvlOffset = maxLvlOffsetX;
        }
        else if(xLvlOffset < 0){ //Makes sure offset value doesnt get too low and go below 0
            xLvlOffset = 0;
        }

    }

    private void P2checkCloseToBorder() { //Checks if player is close to left or right border
        int player2X = (int)player2.getHitbox().x; //Gets current player x value
        int diff = player2X - xLvlOffset; //difference between player x value and valid offset

        if(diff > rightBorder){ //if diff is greater than right border
            xLvlOffset += diff - rightBorder; //difference of diff and right border is added to x offset
        }
        else if(diff < leftBorder){ //also checks left border
            xLvlOffset += diff - leftBorder; //difference of diff and right border is added to x offset
        }

        if(xLvlOffset > maxLvlOffsetX){ //Makes sure offset value doesnt get too high and go over the actual amount possible
            xLvlOffset = maxLvlOffsetX;
        }
        else if(xLvlOffset < 0){ //Makes sure offset value doesnt get too low and go below 0
            xLvlOffset = 0;
        }

    }

    @Override
    public void draw(Graphics g) {
        for(int i = 0; i < 3; i++){ //Increase for loop dependent on how long your level is (this loop redraws background to add a level of depth to the games environment)
            g.drawImage(backgroundImg1, i * CAVE_WIDTH - (int)(xLvlOffset * 0.25), 0, CAVE_WIDTH, CAVE_HEIGHT, null); //draws first part of playing background
            g.drawImage(backgroundImg2, i * CAVE_WIDTH - (int)(xLvlOffset * 0.35), 0, CAVE_WIDTH, CAVE_HEIGHT, null); //draws second part of playing background
            g.drawImage(backgroundImg3, i * CAVE_WIDTH - (int)(xLvlOffset * 0.5), 0, CAVE_WIDTH, CAVE_HEIGHT, null); //draws third and final layer of playing background
        }



        levelManager.draw(g, xLvlOffset); //Draws whatever level is required depending on manager
        if(levelManager.getLvlIndex() == 0 || levelManager.getLvlIndex() == 2 || levelManager.getLvlIndex() == 4 ){
            player.render(g, xLvlOffset); //Draws player1
        }
        if(levelManager.getLvlIndex() == 1 || levelManager.getLvlIndex() == 3 || levelManager.getLvlIndex() == 4){
            player2.render(g, xLvlOffset); //Draws player1
        }
        if(levelManager.getLvlIndex() == 5){
            player.render(g, xLvlOffset);
            player2.render(g, xLvlOffset);
        }
        //player2.render(g, xLvlOffset);
        enemyManager.draw(g, xLvlOffset); //Draws all enemies in respective levels
        objectManager.draw(g, xLvlOffset); //draws all objects in respective levels
        if(isPaused){
            g.setColor(new Color(0, 0, 0, 150)); //Creates a black background with an alpha value so its not completely opaque when game is paused
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            pauseOverlay.draw(g); //Draws pause overlay if game is paused
        }
        else if(gameOver){ //draws the game over overlay
            objectManager.setShardsCollected(0);
            gameOverOverlay.draw(g);
        }
        else if(lvlCompleted){ //draws level complete overlay
            levelCompletedOverlay.draw(g);
        }
        else if(gameCompleted){
            gameCompletedOverlay.draw(g);
        }
    }

    public void setGameCompleted() {
        gameCompleted = true;
    }

    public void resetGameCompleted() {
        gameCompleted = false;
    }

    public void resetAll(){
        objectManager.setShardsCollected(0);
        gameOver = false; //sets game over to false when resetting game
        isPaused = false;
        lvlCompleted = false;
        playerDying = false;
        player2Dying = false;
        player.resetAll();
        player2.resetAll();
        enemyManager.resetAllEnemies();
        objectManager.resetAllObjects();
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void checkObjectHit(Rectangle2D.Float attackBox) {
        objectManager.checkObjectHit(attackBox); //checks if players attackbox hit an objects hitbox using method from objectmanager
    } //used in player class

    public void checkEnemyHit(Rectangle2D.Float attackBox){
        enemyManager.checkEnemyHit(attackBox, player);
    }

    public void checkSpikesTouched(Adventurer p) { //checks if player touched a spike
        objectManager.checkSpikesTouched(p);
    } //used in player class

    public void checkSpikesTouched(Ghost g) { //checks if player touched a spike
        objectManager.checkSpikesTouched(g);
    } //used in player class

    public void checkPotionTouched(Rectangle2D.Float hitbox){
        objectManager.checkPotionTouched(hitbox); //uses objectManager method to check if player touched a potion
    } //used in player class

    public void checkShardTouched(Rectangle2D.Float hitbox){
        objectManager.checkShardTouched(hitbox); //uses objectManager method to check if player touched a shard
    } //used in player class
    public void mouseDragged(MouseEvent e){
        if(!gameOver){
            if(isPaused){ //If game is paused (pause overlay is displayed)
                pauseOverlay.mouseDragged(e); //mouseDragged method from pauseOverlay is called
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(!gameOver){ //as long as game isnt over, player can attack
            if(e.getButton() == MouseEvent.BUTTON1){
                player.setAttacking(true);
            }
            else if(e.getButton() == MouseEvent.BUTTON3){
                player.powerAttack();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (gameOver)
            gameOverOverlay.mousePressed(e);
        else if (isPaused)
            pauseOverlay.mousePressed(e);
        else if (lvlCompleted)
            levelCompletedOverlay.mousePressed(e);
        else if (gameCompleted)
            gameCompletedOverlay.mousePressed(e);

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (gameOver)
            gameOverOverlay.mouseReleased(e);
        else if (isPaused)
            pauseOverlay.mouseReleased(e);
        else if (lvlCompleted)
            levelCompletedOverlay.mouseReleased(e);
        else if (gameCompleted)
            gameCompletedOverlay.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(!gameOver){
            if(gameOver){
                pauseOverlay.mouseMoved(e);
            }
            else if(isPaused){
                gameOverOverlay.mouseMoved(e);
            }
            else if(lvlCompleted){
                levelCompletedOverlay.mouseMoved(e);
            }
            else if(gameCompleted){
                gameCompletedOverlay.mouseMoved(e);
            }
        }

    }


    @Override
    public void keyPressed(KeyEvent e) {
        if(gameOver){
            gameOverOverlay.keyPressed(e); //if game is over the only key allowed to be pressed is esc
        }
        else{
            switch(e.getKeyCode()){
                case KeyEvent.VK_A:
                    player.setLeft(true);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(true);
                    break;
                case KeyEvent.VK_W:
                    player.setJump(true);
                    break;
                case KeyEvent.VK_ESCAPE:
                    isPaused = !isPaused; //toggles pause boolean
                    break;
                case KeyEvent.VK_LEFT:
                    player2.setLeft(true);
                    break;
                case KeyEvent.VK_RIGHT:
                    player2.setRight(true);
                    break;
                case KeyEvent.VK_UP:
                    player2.setJump(true);
                    break;
                case KeyEvent.VK_NUMPAD0:
                    player2.setAttacking(true);
                    break;
                case KeyEvent.VK_NUMPAD1:
                    player2.toggleCloak();
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(!gameOver){
            switch(e.getKeyCode()){
                case KeyEvent.VK_A:
                    player.setLeft(false);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(false);
                    break;
                case KeyEvent.VK_W:
                    player.setJump(false);
                    break;
                case KeyEvent.VK_LEFT:
                    player2.setLeft(false);
                    break;
                case KeyEvent.VK_RIGHT:
                    player2.setRight(false);
                    break;
                case KeyEvent.VK_UP:
                    player2.setJump(false);
                    break;
            }
        }

    }

    public void setLevelCompleted(boolean levelCompleted) {
        game.getAudioPlayer().lvlCompleted();
        if (levelManager.getLvlIndex() + 1 >= levelManager.getAmountOfLevels()) {
            // No more levels
            gameCompleted = true;
            levelManager.setLevelIndex(0);
            levelManager.loadNextLevel();
            resetAll();

            return;
        }
        //TODO: add an if condition here to check if all paradox shards were collected in levels 2 and 4
        if(objectManager.allShardsCollected()){
            this.lvlCompleted = levelCompleted;
        }
    }

    public void setMaxLvlOffset(int lvlOffset){
        this.maxLvlOffsetX = lvlOffset;
    }

    public void unpauseGame(){
        isPaused = false;
    }


    public void windowFocusLost() {
        player.resetDirectionalBooleans();
        player2.resetDirectionalBooleans();
    }

    public Adventurer getPlayer(){
        return player;
    }

    public Ghost getPlayer2(){
        return player2;
    }

    public EnemyManager getEnemyManager(){
        return enemyManager;
    }

    public ObjectManager getObjectManager(){
        return objectManager;
    }

    public LevelManager getLevelManager(){
        return levelManager;
    }

    public void setPlayerDying(boolean playerDying) { //this playerDying is only for the adventurer
        this.playerDying = playerDying;
    }

    public void setPlayer2Dying(boolean player2Dying) { //this playerDying is only for the adventurer
        this.player2Dying = player2Dying;
    }




}
