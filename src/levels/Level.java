package levels;

//This class creates each individual level and then scans through the level and places the proper tiles, enemies, objects
//depending on the RGB values of each tile in the png

import entities.*;
import main.Game;
import objects.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.ArrayList;

import static utils.Constants.EnemyConstants.*;

import static utils.Constants.ObjectConstants.*;

//import all entities here
import entities.PlagueCrow;
public class Level {

    private BufferedImage img;
    private int[][] lvlData;
    private ArrayList<PlagueCrow> crows = new ArrayList<>();
    private ArrayList<ParadoxGolem> golems = new ArrayList<>();

    private ArrayList<Potion> potions = new ArrayList<>();
    private ArrayList<GameContainer> containers = new ArrayList<>();
    private ArrayList<ParadoxShards> shards = new ArrayList<>();
    private ArrayList<Spikes> spikes = new ArrayList<>();
    private ArrayList<Cannon> cannons = new ArrayList<>();
    private int lvlTilesWide; //Returns width of level
    private int maxTilesOffset; //Offset of how many tiles can be seen (total amount of tiles - tiles that can be seen)
    private int maxLvlOffsetX; //max x offset
    private Point playerSpawn; //spawnpoint of player
    private Point player2Spawn;
    private ArrayList<Wizard> wizards = new ArrayList<>();


    public Level(BufferedImage img){
        this.img = img;
        lvlData = new int[img.getHeight() + 1][img.getWidth() + 1];
        loadLevel();
        calcLvlOffsets(); //calculates level offsets depending on level size

    }

    private void loadLevel() {

        // Looping through the image colors just once. Instead of one per
        // object/enemy/etc..
        // Removed many methods in HelpMethods class.

        for (int y = 0; y < img.getHeight(); y++)
            for (int x = 0; x < img.getWidth(); x++) {
                Color c = new Color(img.getRGB(x, y));
                int red = c.getRed();
                int green = c.getGreen();
                int blue = c.getBlue();

                loadLevelData(red, x, y);
                loadEntities(green, x, y);
                loadObjects(blue, x, y);
            }
    }

    private void loadLevelData(int redValue, int x, int y) {
        if (redValue >= 50) //if red value is a value that is not a tile in the game (might actually only be 48)
            lvlData[y][x] = 0;
        else
            lvlData[y][x] = redValue;
    }

    private void loadEntities(int greenValue, int x, int y) {
        switch (greenValue) {
            case PLAGUE_CROW -> crows.add(new PlagueCrow(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
            case PARADOX_GOLEM -> golems.add(new ParadoxGolem(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
            case 100 -> playerSpawn = new Point(x * Game.TILES_SIZE, y * Game.TILES_SIZE);
            //case 101 -> player2Spawn = new Point(x * Game.TILES_SIZE, y * Game.TILES_SIZE);
            case WIZARD -> wizards.add(new Wizard(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
        }
    }

    private void loadObjects(int blueValue, int x, int y) {
        switch (blueValue) {
            case NIGHTBORNE_POTION, GHOST_POTION -> potions.add(new Potion(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
            case PARADOX_SHARD -> shards.add(new ParadoxShards(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
            case BOX, BARREL -> containers.add(new GameContainer(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
            case SPIKE -> spikes.add(new Spikes(x * Game.TILES_SIZE, y * Game.TILES_SIZE, SPIKE));
            case CANNON_LEFT, CANNON_RIGHT -> cannons.add(new Cannon(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
        }
    }

    private void calcLvlOffsets() {
        lvlTilesWide = img.getWidth(); //Gets number of tiles wide that the level is
        maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH; //Gets maximum offset of each tile
        maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset; //gets maximum lvl offset of x value in game
    }

    public int getSpriteIndex(int x, int y){
        return lvlData[y][x]; //Returns using int y for height then x for width, so its backwards
    }

    public int[][] getLvlData(){
        return lvlData;
    }

    public int getLvlOffset(){
        return maxLvlOffsetX;
    }

    public ArrayList<PlagueCrow> getCrows(){
        return crows;
    }

    public ArrayList<ParadoxGolem> getGolems(){
        return golems;
    }

    public ArrayList<Wizard> getWizards(){
        return wizards;
    }


    public Point getPlayerSpawn(){
        return playerSpawn;
    }

    public Point getPlayer2Spawn(){
        return player2Spawn;
    }

    public ArrayList<Potion> getPotions(){
        return potions;
    }

    public ArrayList<GameContainer> getContainers(){
        return containers;
    }

    public ArrayList<ParadoxShards> getShards(){
        return shards;
    }

    public ArrayList<Spikes> getSpikes(){
        return spikes;
    }

    public ArrayList<Cannon> getCannons(){
        return cannons;
    }
}
