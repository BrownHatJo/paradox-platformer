package main;

public class Driver {
    public static void main(String[] args){
        new Game(); //runs game

        //Prints game controls in the console
        System.out.println("Player 1 Controls:");
        System.out.println("W, A, D to move");
        System.out.println("LEFT CLICK: Sword attack");
        System.out.println("RIGHT CLICK: Dash attack");
        System.out.println("Player 2 Controls:");
        System.out.println("Arrow Keys to move");
        System.out.println("Numpad 0: Slash attack");
        System.out.println("Numpad 1: Cloak attack");
        System.out.println("Hit esc to pause game");

    }
}