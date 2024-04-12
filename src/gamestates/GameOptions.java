package gamestates;

//this class takes care of everything that has to do with the gameOptions overlay when the game is paused

import main.Game;
import ui.AudioOptions;
import ui.PauseButton;
import ui.UrmButton;
import utils.Constants;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utils.Constants.UI.URMButtons.*;

public class GameOptions extends State implements Statemethods{

    private AudioOptions audioOptions;
    private BufferedImage backgroundImg, optionsBackgroundImg; //one is background for the entire window, the other is background for just pause screen (option screen)
    private int bgX, bgY,bgW, bgH; //coordinates and size for optionsBackgroundImg to use
    private UrmButton menuB; //menu button used in option menu

    public GameOptions(Game game) {
        super(game);
        loadImgs();
        loadButton();
        audioOptions = game.getAudioOptions(); //gets same audioOptions as in pause overlay
    }

    private void loadButton() {
        int menuX = (int)(387 * Game.SCALE); //gets x coord value for menu button to be inside options UI
        int menuY = (int)(325 * Game.SCALE); //gets y coord value
        menuB = new UrmButton(menuX, menuY, URM_SIZE, URM_SIZE, 2); //creates button of second index from arrayList (home button)
    }

    private void loadImgs() {
        backgroundImg = LoadSave.getSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
        optionsBackgroundImg = LoadSave.getSpriteAtlas(LoadSave.OPTIONS_MENU);

        //sets coordinates and sizes for options menu UI
        bgW = (int)(optionsBackgroundImg.getWidth() * Game.SCALE);
        bgH = (int)(optionsBackgroundImg.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2; //centers x pos
        bgY = (int)(33 * Game.SCALE);
    }

    @Override
    public void update() {
        menuB.update();
        audioOptions.update();
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200)); //darkens background when opening options menu
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.drawImage(backgroundImg, 0, 0, game.GAME_WIDTH, game.GAME_HEIGHT, null);
        g.drawImage(optionsBackgroundImg, bgX, bgY, bgW, bgH, null);

        //draws menu button and audio buttons
        menuB.draw(g);
        audioOptions.draw(g);
    }

    //used for volume slider
    public void mouseDragged(MouseEvent e){
        audioOptions.mouseDragged(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(isIn(e, menuB)){
            menuB.setMousePressed(true);
        }
        else{
            audioOptions.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(isIn(e, menuB)){
            if(menuB.isMousePressed()) {
                Gamestate.state = Gamestate.MENU;
            }
        }
        else{
            audioOptions.mouseReleased(e);
        }
        menuB.resetBooleans(); //resets booleans when mouse is released
    }

    @Override
    public void mouseMoved(MouseEvent e) {

        menuB.setMouseOver(false); //sets mouseOver to false by default before checking if mouse is over

        if(isIn(e, menuB)){
            menuB.setMouseOver(true);
        }
        else{
            audioOptions.mouseMoved(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){ //checks if escape is clicked to go back to menu
            Gamestate.state = Gamestate.MENU;
        }

    }


    //These two methods below are unused as of now
    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }
    ///////////////////////////////////////////////

    //Similar to the method in state class, but the same one cannot be used here because PauseOverlay is not a gamestate
    private boolean isIn(MouseEvent e, PauseButton b){
        return b.getBounds().contains(e.getX(), e.getY()); //Returns true if mouse is inside a button/sliders hitbox, otherwise returns false
    }
}
