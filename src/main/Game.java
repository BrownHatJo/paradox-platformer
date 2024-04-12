package main;

//This class runs the entire game using a delta time game loop

import audio.AudioPlayer;
import gamestates.GameOptions;
import gamestates.Gamestate;
import gamestates.Playing;
import gamestates.Menu;
import ui.AudioOptions;
import utils.LoadSave;

import java.awt.*;

//runnable is a functional interface that has a run method which can be used as a game loop
public class Game implements Runnable{

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 120; //Final int to set fps of game to 120
    private final int UPS_SET = 200; //Final int to set updates per second of game to 200

    private Playing playing;
    private Menu menu;
    private GameOptions gameOptions;
    private AudioOptions audioOptions;
    private AudioPlayer audioPlayer;


    //These final variables for tiles are used to set the size of the tiles in the game
    public final static int TILES_DEFAULT_SIZE = 32; //32 pixels
    public final static float SCALE = 1.5f; //Try to keep scale always a round number
    public final static int TILES_IN_WIDTH = 26; //Number of visible tiles in width of game
    public final static int TILES_IN_HEIGHT = 14; //Number of visible tiles in height of game
    public final static int TILES_SIZE = (int)(TILES_DEFAULT_SIZE * SCALE); //Size of tiles dependent on scale
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH; //Width of tiles in the game window of game
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT; //Height of tiles in the game window of game



    public Game(){
        initClasses(); //Initializes Adventurer and its handlers

        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.setFocusable(true); //Refocuses gamePanel to ensure that all keyboard/mouse inputs work after the use of another input
        gamePanel.requestFocusInWindow(); //Makes sure gamePanel is focused on keyboard and mouse inputs


        startGameLoop();
    }

    private void initClasses() { //Initializes both menu and playing gamestates for now
        audioOptions = new AudioOptions(this);
        audioPlayer = new AudioPlayer();
        menu = new Menu(this);
        playing = new Playing(this);
        gameOptions = new GameOptions(this); //creates a new gameOptions class
    }

    private void startGameLoop(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    //Depending on which game state is active, this method will continuously update those game states assets
    public void update(){

        switch(Gamestate.state){
        case MENU:
            menu.update();
            break;
        case PLAYING:
            playing.update();
            break;
        case OPTIONS:
            gameOptions.update(); //updates game options
            break;
        case QUIT:
        default:
            System.exit(0); //Terminates program for now
            break;

        }
    }

    //Depending on what game state is active, those assets will be drawn through this method
    public void render(Graphics g){

        switch(Gamestate.state){
            case MENU:
                menu.draw(g);
                break;
            case PLAYING:
                playing.draw(g);
                break;
            case OPTIONS:
                gameOptions.draw(g);
                break;
            default:
                break;

        }


    }

    //Game loop that runs the game
    @Override
    public void run() { //game loop
        double timePerFrame = 1000000000.0 / FPS_SET; //Time per frame second
        double timePerUpdate = 1000000000.0 / UPS_SET; //time per update second

        long previousTime = System.nanoTime();

        int frames = 0; //frames indicator
        int updates = 0; //updates indicator
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0; //used to make up for lost time when updating game
        double deltaF = 0; //used to make up for lost frames when updating game

        while(true){
            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate; //deltaU will be 1.0 or more when the duration since last update is equal to or more than timePerUpdate
           deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;
            if(deltaU >= 1){
                update();
                updates++;
                deltaU--; //subtracts 1 so that the time wasted this update can be used when the next update is required
            }

            if(deltaF >= 1){
                gamePanel.repaint(); //repaints the frame
                frames++;
                deltaF--;
            }


            if(System.currentTimeMillis() - lastCheck >= 1000){
                lastCheck = System.currentTimeMillis();
//                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0; //resets frames after each iteration
                updates = 0; //resets updates after each iteration
            }


        }

    }

    public void windowFocusLost() {
        if(Gamestate.state == Gamestate.PLAYING){ //Doesnt matter if focus is lost when in the menu, only when playing
            playing.getPlayer().resetDirectionalBooleans(); //Resets player movement so they dont run off
        }
    }

    public Menu getMenu(){
        return menu;
    }

    public Playing getPlaying(){
        return playing;
    }


//    public Ghost getPlayer2(){ player 2
//        return player2;
//    }

    public AudioOptions getAudioOptions(){
        return audioOptions;
    }

    public GameOptions getGameOptions(){
        return gameOptions;
    }

    public AudioPlayer getAudioPlayer(){
        return audioPlayer;
    }
}
