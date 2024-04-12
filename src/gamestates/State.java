package gamestates;

import audio.AudioPlayer;
import main.Game;
import ui.MenuButton;

import java.awt.event.*;

//This State class is the superclass for all gamestates
//All gamestates extend this class
public class State {

    protected Game game;

    public State(Game game){
        this.game = game;
    }

    //Checks if player is hovering over button
    public boolean isIn(MouseEvent e, MenuButton mb){
        return mb.getBounds().contains(e.getX(), e.getY()); //Returns true if mouse is inside buttons hitbox, and false otherwise
    }

    public Game getGame(){
        return game;
    }

    public void setGameState(Gamestate state){
        switch (state){
            case MENU -> game.getAudioPlayer().playSong(AudioPlayer.MENU_1); //plays menu song when going to menu
            case PLAYING -> game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLvlIndex()); //plays level song depending on level index
        }

        Gamestate.state = state; //changes gamestate to new state that is set 
    }

}
