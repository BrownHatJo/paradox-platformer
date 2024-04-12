package main;

//This class creates the GamePanel in which the game is played

import inputs.KeyboardInputs;
import inputs.MouseInputs;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;
import static utils.Constants.PlayerConstants.*;
import static utils.Constants.Directions.*;

public class GamePanel extends JPanel {

    private MouseInputs mouseInputs;
    private Game game;

    public GamePanel(Game game){

        mouseInputs = new MouseInputs(this); //Object used to take mouse inputs
        this.game = game;



        setPanelSize();
        addKeyListener(new KeyboardInputs(this)); //Object used to take keyboard inputs
        addMouseListener(mouseInputs); //game panel uses inputs from mouseListener
        addMouseMotionListener(mouseInputs); //as well as motionListener
    }





    private void setPanelSize() { //Sets size of window to make 32x32 tileset
        Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
        setPreferredSize(size);
        System.out.println("size : " + GAME_WIDTH + " : " + GAME_HEIGHT);
    }



    public void updateGame(){

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        game.render(g);

    }

    public Game getGame(){
        return game;
    }



}
