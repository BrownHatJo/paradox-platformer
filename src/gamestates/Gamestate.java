package gamestates;

//This Gamestate enum has all the constant gamestates as well as a variable which sets the active gamesate to menu
//by default
public enum Gamestate {

    PLAYING, MENU, OPTIONS, QUIT;

    public static Gamestate state = MENU;

}
