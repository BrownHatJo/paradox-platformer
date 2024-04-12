package ui;

/*This file takes care of creating and updating the level completed overlay to be used in the game*/

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import static utils.Constants.UI.URMButtons.*;

public class LevelCompletedOverlay {

    Playing playing;
    private UrmButton menu, next; //menu button and next level button
    private BufferedImage img; //actual img of overlay
    private int bgX, bgY, bgW, bgH;


    public LevelCompletedOverlay(Playing playing){
        this.playing = playing;
        initImg();
        initButtons();
    }

    private void initButtons() {
        int menuX = (int)(330 * Game.SCALE); //menu button x coord
        int nextX = (int)(445 * Game.SCALE); //next level button x coord
        int y = (int)(195 * Game.SCALE); //common y coord for both buttons
        next = new UrmButton(nextX, y, URM_SIZE, URM_SIZE, 0); //creates buttons
        menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);
    }

    private void initImg() {
        img = LoadSave.getSpriteAtlas(LoadSave.COMPLETED_IMAGE);
        bgW = (int)(img.getWidth() * Game.SCALE);
        bgH = (int)(img.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int)(75 * Game.SCALE);
    }

    public void draw(Graphics g){
        g.drawImage(img, bgX, bgY, bgW, bgH, null);
        next.draw(g);
        menu.draw(g);
    }

    public void update(){
        next.update();
        menu.update();
    }

    private boolean isIn(UrmButton b, MouseEvent e){
        return b.getBounds().contains(e.getX(), e.getY());
    }

    public void mouseMoved(MouseEvent e){
        next.setMouseOver(false); //sets mouse over to false as default
        menu.setMouseOver(false);

        if(isIn(menu, e)){
            menu.setMouseOver(true);
        }
        else if(isIn(next, e)){
            next.setMouseOver(true);
        }
    }

    public void mouseReleased(MouseEvent e){
        if(isIn(menu, e)){
            if(menu.isMousePressed()){
                playing.resetAll();
                playing.setGameState(Gamestate.MENU);
            }
        }
        else if(isIn(next, e)){
            if(next.isMousePressed()){
                playing.loadNextLevel();
                playing.getGame().getAudioPlayer().setLevelSong(playing.getLevelManager().getLvlIndex()); //if player moves to next level, play next levels song
            }
        }
        menu.resetBooleans(); //when released boolean values are automatically reset
        next.resetBooleans();
    }

    public void mousePressed(MouseEvent e){
        if(isIn(menu, e)){
            menu.setMousePressed(true);
        }
        else if(isIn(next, e)){
            next.setMousePressed(true);
        }
    }
}
