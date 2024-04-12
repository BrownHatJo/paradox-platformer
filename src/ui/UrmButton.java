package ui;

/*This file takes care of creating and updating the home, restart and play buttons to be used in the game*/

import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utils.Constants.UI.URMButtons.*;

public class UrmButton extends PauseButton{

    private BufferedImage[] imgs;
    private int rowIndex, index;
    private boolean mouseOver, mousePressed;

    //Row index to use to get proper button type dependent on image
    public UrmButton(int x, int y, int width, int height, int rowIndex) {
        super(x, y, width, height);
        this.rowIndex = rowIndex;
        loadImgs();
    }

    private void loadImgs() {
        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.URM_BUTTONS); //Temp sprite atlas image
        imgs = new BufferedImage[3];
        for(int i = 0; i < imgs.length; i++){
            imgs[i] = temp.getSubimage(i * URM_DEFAULT_SIZE, rowIndex * URM_DEFAULT_SIZE, URM_DEFAULT_SIZE, URM_DEFAULT_SIZE);
        }
    }

    public void update(){

        index = 0; //Index is set to 0 as default

        if(mouseOver){
            index = 1; //Checks if mouse is hovering over a button and if so sets index to 1
        }
        if(mousePressed){
            index = 2; //Checks if mouse pressed a button and if so sets index to 2
        }
    }

    public void draw(Graphics g){
        g.drawImage(imgs[index], x, y, URM_SIZE, URM_SIZE, null);
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
}
