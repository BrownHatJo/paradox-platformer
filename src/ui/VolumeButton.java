package ui;
/*This file takes care of displaying and updating the volume buttons ui while playing the game*/

import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utils.Constants.UI.VolumeButtons.*;

public class VolumeButton extends PauseButton{

    private BufferedImage[] imgs;
    private BufferedImage slider;
    private int index = 0;
    private boolean mouseOver, mousePressed;
    private int buttonX, minX, maxX; //minX - minimum value slider can reach | maxX - maximum value slider can reach (Xcoord)
    //buttonX is default value of where slider button is, before it is edited by mouse activity
    private float floatValue = 0f; //float value used to set volume of game using slider
    public VolumeButton(int x, int y, int width, int height) {
        //X is set so that slider button appears in the middle of slider
        //This hitbox (bounds) should only be for the button, not the entire slider
        super(x + width / 2, y, VOLUME_WIDTH, height);
        bounds.x -= VOLUME_WIDTH / 2; //The bounds is set to be moved to the left by volume width / 2 just like the visual image is when it is drawn in draw() method
        buttonX = x + width/2;
        this.x = x; //Gives a new x and width value for the actual slider
        this.width = width;
        minX = x + VOLUME_WIDTH / 2; //Start of slider position plus width of slider divided by 2
        maxX = x + width - VOLUME_WIDTH / 2; //Start of slider position plus its width minus width of slider divided by 2
        loadImgs();
    }

    private void loadImgs() {
        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.VOLUME_BUTTONS); //Temp sprite atlas image
        imgs = new BufferedImage[3];
        for(int i = 0; i < imgs.length; i++){
            imgs[i] = temp.getSubimage(i * VOLUME_DEFAULT_WIDTH, 0, VOLUME_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);
        }
        slider = temp.getSubimage(3 * VOLUME_DEFAULT_WIDTH, 0, SLIDER_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT); //Temp variable after being used in for loop is used to store slider
        //3 * VOLUME+DEFAULT_WIDTH because there are 3 slider buttons that need to be ignored to get the slider image

    }

    public void update(){
        index = 0;

        if(mouseOver){
            index = 1;
        }
        if(mousePressed){
            index = 2;
        }

    }

    public void draw(Graphics g){

        g.drawImage(slider, x, y, width, height, null);
        g.drawImage(imgs[index], buttonX - VOLUME_WIDTH / 2, y, VOLUME_WIDTH, height, null); //Height is always same for button as it is for slider
        //buttonX has the same value that sets slider button to center of slider by default, but this one will be changed depending on mouse movements
    }

    public void changeX(int x){
        if(x < minX){ //If x is dragged to a lower value than its allowed to go
            buttonX = minX; //buttonX is set to min x value of slider
        }
        else if(x > maxX){ //If x is dragged to a higher value than its allowed to go
            buttonX = maxX; //buttonX is set to max x value of slider
        }
        else{
            buttonX = x; //Otherwise buttonX is allowed to be set to x value
        }

        updateFloatValue();
        bounds.x = buttonX - VOLUME_WIDTH / 2; //Fixes bounds (hitbox) of slider button to follow the x value of the image
    }

    private void updateFloatValue() { //updates float value depending on x coordinate of volume slider
        float range = maxX - minX; //gets a range x coordinate for the volume slider
        float value = buttonX - minX; //float value of slider depending on where it is on the slider
        floatValue = value/range; //value dividing by range gives the floatValue used as volume
    }

    public void resetBooleans(){
        mouseOver = false;
        mousePressed = false;
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

    public float getFloatValue(){
        return floatValue;
    }
}
