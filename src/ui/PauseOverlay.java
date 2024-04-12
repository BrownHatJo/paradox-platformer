package ui;

/*This file takes care of creating and updating the pause overlay image to be used in the game*/


import gamestates.Gamestate;
import gamestates.Playing;
import jdk.swing.interop.DispatcherWrapper;
import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utils.Constants.UI.PauseButtons.*;
import static utils.Constants.UI.URMButtons.*;
import static utils.Constants.UI.VolumeButtons.*;

public class PauseOverlay {

    private Playing playing;
    private BufferedImage backgroundImg; //BufferedImage variable for the pause screen background
    private int bgX, bgY, bgW, bgH;
    //Background x, y, width and height
    private UrmButton menuB, replayB, unpauseB; //menu button, replay button, unpause button
    private AudioOptions audioOptions;


    public PauseOverlay(Playing playing){
        this.playing = playing;
        loadBackground(); //Loads pause ui
        audioOptions = playing.getGame().getAudioOptions();
        createUrmButtons(); //Creates menu, replay and unpause buttons
    }

    private void createUrmButtons() { //Y Coords will always be same
        int menuX = (int)(313 * Game.SCALE); //Menu x coord, offsets button to its right (replay) by 74
        int replayX = (int)(387 * Game.SCALE); //Replay x coord, offsets button to its right (unpause) by 75
        int unpauseX = (int)(462 * Game.SCALE);
        int buttonY = (int)(325 * Game.SCALE);

        menuB = new UrmButton(menuX, buttonY, URM_SIZE, URM_SIZE, 2); //Row index for menu button is 2
        replayB = new UrmButton(replayX, buttonY, URM_SIZE, URM_SIZE, 1); //Row index for menu button is 2
        unpauseB = new UrmButton(unpauseX, buttonY, URM_SIZE, URM_SIZE, 0); //Row index for menu button is 2
    }

    private void loadBackground() {
        backgroundImg = LoadSave.getSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        bgW = (int)(backgroundImg.getWidth() * Game.SCALE);
        bgH = (int)(backgroundImg.getHeight() * Game.SCALE); //Sets image with width and height relative to game scale
        bgX = Game.GAME_WIDTH/2 - bgW / 2; //In the center - width/2
        bgY = (int)(25 * Game.SCALE); //Y dependent on scale


    }

    public void update(){

        menuB.update(); //Updates menu button
        replayB.update(); //Updates replay button
        unpauseB.update(); //Updates unpause button

        audioOptions.update(); //updates audio option buttons

    }

    public void draw(Graphics g){
        //Pause ui
        g.drawImage(backgroundImg, bgX, bgY, bgW, bgH, null); //draws image

        //Urm buttons
        menuB.draw(g); //draws menu button
        replayB.draw(g); //draws replay button
        unpauseB.draw(g); //draws unpause button

        audioOptions.draw(g); //draws all audio option buttons
    }

    //mouseDragged is only used for volume slider
    public void mouseDragged(MouseEvent e){
        audioOptions.mouseDragged(e);
    }

    //other 3 mouse events are used for the sound buttons
    public void mousePressed(MouseEvent e) {
        if(isIn(e, menuB)){ //else checks if its in menu button bounds
            menuB.setMousePressed(true); //In which case menu button mose pressed is set to true
        }
        else if(isIn(e, replayB)){ //else checks if its in replay button bounds
            replayB.setMousePressed(true); //In which case replay button mose pressed is set to true
        }
        else if(isIn(e, unpauseB)){ //else checks if its in unpause button bounds
            unpauseB.setMousePressed(true); //In which case unpause button mose pressed is set to true
        }
        else{
            audioOptions.mousePressed(e);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if(isIn(e, menuB)){ //else check if its in menu buttons bounds
            if(menuB.isMousePressed()){
                playing.resetAll(); //resets game and all entities
                playing.setGameState(Gamestate.MENU);//If so, gamestate is set to menu
                playing.unpauseGame(); //When we go back into game after leaving from menu, game should start unpaused
            }
        }
        else if(isIn(e, replayB)){ //else check if its in replay buttons bounds
            if(replayB.isMousePressed()){
                playing.resetAll(); //resets game and all entities
                playing.unpauseGame(); //unpauses game
            }
        }
        else if(isIn(e, unpauseB)){ //else check if its in unpause buttons bounds
            if(unpauseB.isMousePressed()){
                playing.unpauseGame();
            }
        }
        else{
            audioOptions.mouseReleased(e);
        }

        menuB.resetBooleans();
        replayB.resetBooleans();
        unpauseB.resetBooleans();
    }

    public void mouseMoved(MouseEvent e) {
        menuB.setMouseOver(false);
        replayB.setMouseOver(false);
        unpauseB.setMouseOver(false);

        if(isIn(e, menuB)){ //else check if its in menu buttons bounds
            menuB.setMouseOver(true); //In which case set menu  mouse pressed boolean to true
        }
        else if(isIn(e, replayB)){ //else check if its in replay buttons bounds
            replayB.setMouseOver(true); //In which case set replay mouse pressed boolean to true
        }
        else if(isIn(e, unpauseB)){ //else check if its in unpause buttons bounds
            unpauseB.setMouseOver(true); //In which case set unpause mouse pressed boolean to true
        }
        else{
            audioOptions.mouseMoved(e);
        }

    }

    //Similar to the method in state class, but the same one cannot be used here because PauseOverlay is not a gamestate
    private boolean isIn(MouseEvent e, PauseButton b){
        return b.getBounds().contains(e.getX(), e.getY()); //Returns true if mouse is inside a button/sliders hitbox, otherwise returns false
    }



}
