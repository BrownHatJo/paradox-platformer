package ui;

/*This file takes care of creating and updating the game over overlay to be used in the game*/

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utils.Constants.UI.URMButtons.URM_SIZE;

public class GameOverOverlay {

    private Playing playing;
    private BufferedImage img; //image for the game over overlay
    private int imgX, imgY, imgW, imgH; //values for image size and coords
    private UrmButton menu, play; //menu and play again buttons
    public GameOverOverlay(Playing playing){
        this.playing = playing;
        createImg(); //creates image of gameOver overlay
        createButtons(); //creates buttons for gameOver menu
    }

    private void createButtons() {
        int menuX = (int)(335 * Game.SCALE); //menu button x coord
        int playX = (int)(440 * Game.SCALE); //next level button x coord
        int y = (int)(195 * Game.SCALE); //common y coord for both buttons
        play = new UrmButton(playX, y, URM_SIZE, URM_SIZE, 0); //creates buttons
        menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);
    }

    private void createImg() {
        img = LoadSave.getSpriteAtlas(LoadSave.DEATH_SCREEN);
        imgW = (int)(img.getWidth() * Game.SCALE);
        imgH = (int)(img.getHeight() * Game.SCALE);
        imgX = Game.GAME_WIDTH / 2 - imgW / 2;
        imgY = (int)(100 * Game.SCALE);
    }

    public void draw(Graphics g){
        g.setColor(new Color(0, 0, 0, 200)); //darkens background when game is over
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.drawImage(img, imgX, imgY, imgW, imgH, null);

        menu.draw(g); //draws play and menu buttons
        play.draw(g);


//        g.setColor(Color.white);
//        g.drawString("GAME OVER", Game.GAME_WIDTH/2, 150);
//        g.drawString("Press esc to enter main menu", Game.GAME_WIDTH/2, 300);
    }

    public void update(){
        menu.update();
        play.update();
    }

    public void keyPressed(KeyEvent e){

    }

    private boolean isIn(UrmButton b, MouseEvent e){
        return b.getBounds().contains(e.getX(), e.getY());
    }

    public void mouseMoved(MouseEvent e){
        play.setMouseOver(false); //sets mouse over to false as default
        menu.setMouseOver(false);

        if(isIn(menu, e)){
            menu.setMouseOver(true);
        }
        else if(isIn(play, e)){
            play.setMouseOver(true);
        }
    }

    public void mouseReleased(MouseEvent e){
        if(isIn(menu, e)){
            if(menu.isMousePressed()){
                playing.resetAll();
                playing.setGameState(Gamestate.MENU);
            }
        }
        else if(isIn(play, e)){
            if(play.isMousePressed()){
                playing.resetAll(); //restarts level
                playing.getGame().getAudioPlayer().setLevelSong(playing.getLevelManager().getLvlIndex()); //if player wants to play again, play next levels song
            }
        }
        menu.resetBooleans(); //when released boolean values are automatically reset
        play.resetBooleans();
    }

    public void mousePressed(MouseEvent e){
        if(isIn(menu, e)){
            menu.setMousePressed(true);
        }
        else if(isIn(play, e)){
            play.setMousePressed(true);
        }
    }
}
