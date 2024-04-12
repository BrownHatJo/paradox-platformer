package gamestates;

//This class takes care of everything to do with the main menu of the game and its overlay

import main.Game;
import ui.MenuButton;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Menu extends State implements Statemethods{

    private MenuButton[] buttons = new MenuButton[3]; //Might change from 3, but to begin with it is 3
    private BufferedImage backgroundImg, backgroundImgCave;
    private int menuX, menuY, menuWidth, menuHeight; //Do not have constants for these, so they are declared here so that scaling isnt an issue

    public Menu(Game game) {
        super(game);
        loadButtons(); //Loads buttons into menu
        loadBackground(); //Loads menu background
        backgroundImgCave = LoadSave.getSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
    }

    private void loadBackground() {
        backgroundImg = LoadSave.getSpriteAtlas(LoadSave.MENU_BACKGROUND);
        menuWidth = (int)(backgroundImg.getWidth() * Game.SCALE); //Gets a scaled width and height for background
        menuHeight = (int)(backgroundImg.getHeight() * Game.SCALE);
        menuX = Game.GAME_WIDTH / 2 - menuWidth / 2; //Centers background and takes away half the width so that its symmetrical
        menuY = (int)(45 * Game.SCALE);
        //45 is a predicted position that can be changed if necessary
    }

    private void loadButtons() {
        //XPos will always be in the middle of the window, YPos will change depending on the other buttons (+70 per new button)
        //rowIndex is used to pick which button to be displayed from the array
        //Last variable sets game state depending on button
        buttons[0] = new MenuButton(Game.GAME_WIDTH / 2, (int)(150 * Game.SCALE), 0, Gamestate.PLAYING);
        buttons[1] = new MenuButton(Game.GAME_WIDTH / 2, (int)(220 * Game.SCALE), 1, Gamestate.OPTIONS);
        buttons[2] = new MenuButton(Game.GAME_WIDTH / 2, (int)(290 * Game.SCALE), 2, Gamestate.QUIT);
    }

    @Override
    public void update() {
        for(MenuButton mb: buttons){
            mb.update(); //Updates each button in the array
        }
    }

    @Override
    public void draw(Graphics g) {

        g.setColor(new Color(0, 0, 0, 150)); //Creates a black background with an alpha value so its not completely opaque when game is paused
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
        g.drawImage(backgroundImgCave, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight, null); //Draws menu background
        for(MenuButton mb: buttons){
            mb.draw(g); //Draws all buttons in array
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        for(MenuButton mb: buttons){
            if(isIn(e, mb)){
                mb.setMousePressed(true);
                break; //break because only one button can be pressed at a time
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for(MenuButton mb: buttons){
            if(isIn(e, mb)){
                if(mb.isMousePressed()){ //Checks if the button was actually pressed or not (To make sure mouse wasnt clicked and then simply dragged into the button
                    mb.applyGamestate(); //^ In which case button wasnt technically clicked on
                } //Applies gamestate if button was actually pressed
                if(mb.getState() == Gamestate.PLAYING){
                    game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLvlIndex()); //plays level song when gamestate is set to playing from menu
                }
                break;
            }
        }
        resetButtons(); //Resets any buttons that werent already reset
    }

    private void resetButtons() {
        for(MenuButton mb : buttons){
            mb.resetBooleans(); //Resets all buttons
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for(MenuButton mb : buttons){ //Makes sure after each update, mouseOver boolean is set to false for all buttons
            mb.setMouseOver(false); //Otherwise user might move away from a button
        }

        for(MenuButton mb : buttons){ //If mouse is moved into a button, sets mouseOver to true and breaks
            if(isIn(e, mb)){
                mb.setMouseOver(true);
                break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            Gamestate.state = Gamestate.PLAYING; //When ehter is clicked on menu, gamestate is changed to playing
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
