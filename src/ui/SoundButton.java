package ui;

/*This file takes care of creating and updating the sound buttons to be used in the game*/


import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import static utils.Constants.UI.PauseButtons.*;

//SoundButton class creates all the sound buttons used in pause ui
public class SoundButton extends PauseButton{

    private BufferedImage[][] soundImgs;
    private boolean mouseOver, mousePressed; //Checks to see if mouse is over a sound button or mouse pressed sound button respectively
    private boolean muted; //Determines what row we are on in sound_button.png
    //If true, then that means second row (where button is muted) If false, first row (Where button is unmuted)
    private int rowIndex, colIndex; //Row index is dependent on status of muted boolean
    //ColIndex is dependent on if mouse is over a button, pressed a button, or not (dependent on the specific booleans)

    public SoundButton(int x, int y, int width, int height) {
        super(x, y, width, height);

        loadSoundImgs();
    }

    private void loadSoundImgs() {
        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.SOUND_BUTTONS); //Temp image to load sprite atlas
        soundImgs = new BufferedImage[2][3];
        for(int j = 0; j < soundImgs.length; j++){
            for(int i = 0; i < soundImgs[j].length; i++){
                soundImgs[j][i] = temp.getSubimage(i * SOUND_SIZE_DEFAULT, j * SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT); //Depending on size of each button image,
                //Doesnt have to be gamescaled because they are gamescaled when created
            }
        }
    }

    public void update(){
        if(muted){
            rowIndex = 1;
        }
        else{
            rowIndex = 0;
        }

        colIndex = 0; //Default set to 0 which means mouse is not over or pressing a button

        if(mouseOver){
            colIndex = 1; //If mouse is over a button, colIndex is set to 1
        }
        if(mousePressed){
            colIndex = 2; //If mouse is pressing a button, colIndex is set to 2
        }
    }

    public void resetBooleans(){ //Resets booleans (when button is released)
        mouseOver = false;
        mousePressed = false;
    }

    public void draw(Graphics g){
        g.drawImage(soundImgs[rowIndex][colIndex], x, y, width, height, null);
        //Rows and Cols are dependent on if mouse is over or pressing a button or not
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }
}
