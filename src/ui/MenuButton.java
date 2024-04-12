package ui;

/*This file takes care of creating and updating the menu buttons to be used in the game*/

import gamestates.Gamestate;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utils.Constants.UI.Buttons.*;

public class MenuButton {

//rowIndex is used to specifically select a button from a row in the image atlas
//Gamestate state because when a menu button is pressed, gamestate has to be changed

    private int xPos, yPos, rowIndex, index;
    private int xOffsetCenter= B_WIDTH / 2; //Calculates an XOffset value so that when button is drawn with XPos - XOffsetCenter, the buttons are centered
    private Gamestate state;
    private BufferedImage[] imgs;
    private boolean mouseOver, mousePressed;
    private Rectangle bounds; //Creates a hitbox for button to use to check if button is hovered over/pressed


    public MenuButton(int xPos, int yPos, int rowIndex, Gamestate state){
        this.xPos = xPos;
        this.yPos = yPos;
        this.rowIndex = rowIndex;
        this.state = state;
        loadImgs(); //Loads images of buttons
        initBounds(); //Initializes bounds hitbox for method

    }

    private void initBounds() {
        bounds = new Rectangle(xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT); //Same size and same position as buttons
    }

    private void loadImgs() {
        imgs = new BufferedImage[3];
        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.MENU_BUTTONS); //Temp image used to put all sub images into array
        for(int i = 0; i < imgs.length; i++){
            imgs[i] = temp.getSubimage(i * B_WIDTH_DEFAULT, rowIndex * B_HEIGHT_DEFAULT, B_WIDTH_DEFAULT, B_HEIGHT_DEFAULT);
            //Puts each button image into an array
        }
    }

    public void draw(Graphics g){ //Draws buttons
        g.drawImage(imgs[index], xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT, null);
    }

    public void update(){
        index = 0; //Sets index to 0 by default

        if(mouseOver){
            index = 1;
        } //Checks if mouse is hovering over button
        if(mousePressed){
            index = 2;
        } //Checks if mouse pressed on button
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

    public Rectangle getBounds(){
        return bounds;
    }

    public void applyGamestate(){
        Gamestate.state = state; //Gamestate is changed to whatever state is stored in this variable at the time
    }

    public void resetBooleans(){ //Resets mouse action booleans whenever user leaves menu gamestate
        mouseOver = false;
        mousePressed = false;
    }

    public Gamestate getState(){ //returns gamestate
        return state;
    }


}
