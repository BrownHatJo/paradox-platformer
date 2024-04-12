package main;

//This class creates the game window in which the game is played

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class GameWindow {

    private JFrame jframe;

    public GameWindow(GamePanel gamePanel){

        jframe = new JFrame(); //creates the JFrame in which the game is run

        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //instantly terminates program when window is closed
        jframe.add(gamePanel);
        jframe.setResizable(false); //Denies ability to resize window
        jframe.pack(); //Sizes window to fit all preferred sizes of jframes subcomponents
        jframe.setLocationRelativeTo(null); //makes sure location of window is not relative to anything
        jframe.setVisible(true); //sets window visible on screen
        jframe.addWindowFocusListener(new WindowFocusListener() {

            //Gives game window focus when clicked on
            @Override
            public void windowGainedFocus(WindowEvent e) {

            }

            //Loses game window focus when clicked off the game window
            @Override
            public void windowLostFocus(WindowEvent e) {
                gamePanel.getGame().windowFocusLost();
            }
        });

    }
}
