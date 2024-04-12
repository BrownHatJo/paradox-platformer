package utils;

//This class simply loads all the pngs used in the game using public constant int values to be used in other classes

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

public class LoadSave {

    public static final String PLAYER2_ATLAS = "ghostPlayer_animations.png";
    public static final String PLAYER_ATLAS = "adventurerPlayer_animations.png";
    public static final String LEVEL_ATLAS = "levelTerrain_sprites.png";

    public static final String MENU_BUTTONS = "button_atlas.png";
    public static final String MENU_BACKGROUND = "menu_background.png";
    public static final String PAUSE_BACKGROUND = "pause_UI.png";
    public static final String SOUND_BUTTONS = "sound_button.png";
    public static final String URM_BUTTONS = "urm_buttons.png";
    public static final String VOLUME_BUTTONS = "volume_buttons.png";
    public static final String MENU_BACKGROUND_IMG = "background_menu.png";
    public static final String PLAYING_BACKGROUND_IMG1 = "background1.png";
    public static final String PLAYING_BACKGROUND_IMG2  = "background2.png";
    public static final String PLAYING_BACKGROUND_IMG3  = "background3.png";
    public static final String PLAGUE_CROW_SPRITE  = "crow_spritesheet.png";
    public static final String DARK_FOX_SPRITE  = "dark_fox_spritesheet.png";
    public static final String BOSS_SPRITE  = "paradoxGolem_spritesheet.png";
    public static final String STATUS_BAR  = "health_power_bar.png";
    public static final String COMPLETED_IMAGE  = "completed_sprite.png"; //lvl complete image

    public static final String ITEM_ATLAS  = "items_spritesheet.png";
    public static final String OBJECT_ATLAS  = "objects_sprites.png";
    public static final String TRAP_ATLAS  = "trap_atlas.png";
    public static final String CANNON_ATLAS  = "cannon_atlas.png";
    public static final String CANNON_BALL  = "ball.png";
    public static final String DEATH_SCREEN  = "death_screen.png";
    public static final String OPTIONS_MENU  = "options_background.png";
    public static final String LAVA_TOP = "water_animation.png";
    public static final String LAVA_BOTTOM = "water.png";
    public static final String GAME_COMPLETED = "game_completed.png";
    public static final String GOLEM_PROJECTILE = "arm_projectile.png";
    public static final String WIZARD_IMAGE = "wizard_npc_spritesheet.png";
    public static final String LASER = "Laser_sheet.png";


    //DECLARE ALL SPRITE ATLAS NAMES AS STRINGS HERE SO THAT GETSPRITEATLAS METHOD CAN BE RE-USED^^^

    public static BufferedImage getSpriteAtlas(String fileName){

        BufferedImage img = null; //Declared just in case so that even if try fails, img can be returned

        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName); //InputStreams r usually named "is"
        //The forward slash before the name indicates to the computer that the png can be found within a folder in the project
        try {
            img = ImageIO.read(is);

        } catch (IOException e) {
            e.printStackTrace();
            //System.out.println("Image not found");
        } finally{
            try{
                is.close(); //Closes input stream after image is imported
            } catch(IOException e){
                e.printStackTrace(); //This finally try catch statement is just used to free up resources and avoid unecessary problems
            }
        }

        return img;

    }

    public static BufferedImage[] getAllLevels(){ //Returns all levels
        URL url = LoadSave.class.getResource("/lvls/"); //gets location of lvls folder where url is the identifier of the resource
        File file = null;

        try {
            file = new File(url.toURI()); //url is location of folder, toURI gets the actual resource (folder)
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        File[] files = file.listFiles(); //lists files from folder
        Arrays.sort(files); //sorts array

        BufferedImage[] imgs = new BufferedImage[files.length];

        for(int i = 0; i < imgs.length; i++){ //reads all level files
            try {
                imgs[i] = ImageIO.read(files[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return imgs;
    }

}
