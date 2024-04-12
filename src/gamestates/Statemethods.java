package gamestates;

import java.awt.*;
import java.awt.event.*;

//all gamestates implement this class
public interface Statemethods { //Every class that implements this interface will NEED TO have the methods below to use
    //Every class extending this interface must have these methods called even if they all dont use them all
    void update();
    void draw(Graphics g);
    void mouseClicked(MouseEvent e);
    void mousePressed(MouseEvent e);
    void mouseReleased(MouseEvent e);
    void mouseMoved(MouseEvent e);
    void keyPressed(KeyEvent e);
    void keyReleased(KeyEvent e);
}
