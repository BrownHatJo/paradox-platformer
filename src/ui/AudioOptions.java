package ui;

/*This file takes care of creating and updating the audio options buttons and sliders to be used in the game*/

import gamestates.Gamestate;
import main.Game;

import java.awt.*;
import java.awt.event.MouseEvent;

import static utils.Constants.UI.PauseButtons.SOUND_SIZE;
import static utils.Constants.UI.VolumeButtons.SLIDER_WIDTH;
import static utils.Constants.UI.VolumeButtons.VOLUME_HEIGHT;

public class AudioOptions {

    private VolumeButton volumeButton; //volume button
    private SoundButton musicButton, sfxButton;

    private Game game;
    public AudioOptions(Game game){
        this.game = game;
        createSoundButtons(); //Creates sound buttons
        createVolumeButton();
    }

    private void createVolumeButton() {
        int vX = (int)(309 * Game.SCALE); //x coordinate of volume button
        int vY = (int)(278 * Game.SCALE); //y coord
        volumeButton = new VolumeButton(vX, vY, SLIDER_WIDTH, VOLUME_HEIGHT); //Creates the volume button
    }

    private void createSoundButtons() { //takes a y offset value only actually used in pause overlay
        int soundX = (int)(450 * Game.SCALE); //All buttons will have same x but different y, both being gamescaled
        int musicY = (int)(140 * Game.SCALE); //Different y for musicY and sfxY
        int sfxY = (int)(186 * Game.SCALE); //46 pixel offset between the buttons

        musicButton = new SoundButton(soundX, musicY, SOUND_SIZE, SOUND_SIZE); //Width and Height are in constant class
        sfxButton = new SoundButton(soundX, sfxY, SOUND_SIZE, SOUND_SIZE);
    }

    public void update(){
        musicButton.update();
        sfxButton.update();
        volumeButton.update();
    }

    public void draw(Graphics g){
        //Sound buttons
        musicButton.draw(g);
        sfxButton.draw(g);

        //Volume slider
        volumeButton.draw(g);
    }

    public void mouseDragged(MouseEvent e){
        //Doesnt matter if mouse is not over slider after it is already pressed as it should still drag even if that happens
        if(volumeButton.isMousePressed()){ //Checks if volume button was clicked first
            float valueBefore = volumeButton.getFloatValue(); //gets float value of what the volume was before mouseDragged
            volumeButton.changeX(e.getX()); //Changes x value of volume button using changeX method
            float valueAfter = volumeButton.getFloatValue(); //gets float value to use as volume after mouse is dragged and x value is changed
            if(valueBefore != valueAfter){ //if values had changed
                game.getAudioPlayer().setVolume(valueAfter); //then change volume to newly set value
            }
        }
    }

    //other 3 mouse events are used for the sound buttons
    public void mousePressed(MouseEvent e) {
        if(isIn(e, musicButton)){ //If mouse is in music buttons bounds and mouse is pressed
            musicButton.setMousePressed(true); //set musicButton mouse pressed boolean to true
        }
        else if(isIn(e, sfxButton)){ //else check if its in sfx buttons bounds
            sfxButton.setMousePressed(true); //In which case set sfxButton mouse pressed boolean to true
        }
        else if(isIn(e, volumeButton)){ //else checks if its in volume button bounds
            volumeButton.setMousePressed(true); //In which case volume button mose pressed is set to true
        }
    }

    public void mouseReleased(MouseEvent e) {
        if(isIn(e, musicButton)){ //If mouse is in music buttons bounds and mouse is released
            if(musicButton.isMousePressed()){
                musicButton.setMuted(!musicButton.isMuted());//If musicButton was pressed as well, musicButton is toggled
                game.getAudioPlayer().toggleSongMute(); //toggles mute when mute button is pressed
            }
        }
        else if(isIn(e, sfxButton)){ //else check if its in sfx buttons bounds
            if(sfxButton.isMousePressed()){
                sfxButton.setMuted(!sfxButton.isMuted());//If sfxButton was pressed as well as pressed, sfxButton is toggled
                game.getAudioPlayer().toggleEffectMute(); //toggles mute when mute button is pressed
            }
        }

        musicButton.resetBooleans(); //Resets booleans when mouse is released for both music and sfx buttons
        sfxButton.resetBooleans();
        volumeButton.resetBooleans();
    }

    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false); //Resets mouse over booleans by default
        sfxButton.setMouseOver(false);
        volumeButton.setMouseOver(false);

        if(isIn(e, musicButton)){ //If mouse is in music buttons bounds
            musicButton.setMouseOver(true); //set musicButton mouse over boolean to true
        }
        else if(isIn(e, sfxButton)){ //else check if its in sfx buttons bounds
            sfxButton.setMouseOver(true); //In which case set sfxButton mouse pressed boolean to true
        }
        else if(isIn(e, volumeButton)){ //else check if its in volume button buttons bounds
            volumeButton.setMouseOver(true); //In which case set volume button mouse pressed boolean to true
        }

    }

    //Similar to the method in state class, but the same one cannot be used here because PauseOverlay is not a gamestate
    private boolean isIn(MouseEvent e, PauseButton b){
        return b.getBounds().contains(e.getX(), e.getY()); //Returns true if mouse is inside a button/sliders hitbox, otherwise returns false
    }
}
