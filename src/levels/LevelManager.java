package levels;

//This class manages all the levels in the game and creates them to be played and completed

import gamestates.Gamestate;
import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class LevelManager {

    private Game game;
    private BufferedImage[] levelSprite; //sprite atlas of tiles used to build levels
    private BufferedImage[] waterSprite; //sprite atlas for lava used to build levels
    private ArrayList<Level> levels;
    private int lvlIndex = 0, aniTick, aniIndex;
    public LevelManager(Game game){ //creates levelManager object and imports all the tile sprites required to make levels
        this.game = game;
        importOutsideSprites();
        createWater();
        levels = new ArrayList<>();
        buildAllLevels(); //builds all 6 levels to be played using tiles
    }

    private void createWater() { //creates water objects where necessary depending on level png
        waterSprite = new BufferedImage[5];
        BufferedImage img = LoadSave.getSpriteAtlas(LoadSave.LAVA_TOP);
        for (int i = 0; i < 4; i++)
            waterSprite[i] = img.getSubimage(i * 32, 0, 32, 32);
        waterSprite[4] = LoadSave.getSpriteAtlas(LoadSave.LAVA_BOTTOM);
    }

    public void loadNextLevel() {
        //lvlIndex++; //lvlIndex goes to next level
        if(lvlIndex >= levels.size()){
            lvlIndex = 0; //when all levels are complete, level index is set back to 0
            System.out.println("no more levels! Game completed!");
            Gamestate.state = Gamestate.MENU;
        }

        Level newLevel = levels.get(lvlIndex); //sets newLevel to next level if game isnt over
        game.getPlaying().getEnemyManager().loadEnemies(newLevel); //loads enemies of new Level
        game.getPlaying().getPlayer().loadLvlData(newLevel.getLvlData()); //Loads level data of new level for player
        game.getPlaying().getPlayer2().loadLvlData(newLevel.getLvlData());
        game.getPlaying().setMaxLvlOffset(newLevel.getLvlOffset()); //sets max level offset as the new levels levelOffset
        game.getPlaying().getObjectManager().loadObjects(newLevel);
    }

    private void buildAllLevels() { //Creates all levels from getAllLevels method and adds them to levels list
        BufferedImage[] allLevels = LoadSave.getAllLevels();
        for(BufferedImage img : allLevels){
            levels.add(new Level(img));
        }
    }

    private void importOutsideSprites() { //puts all the tile sprites into an arrayList to be used to make levels

        BufferedImage img = LoadSave.getSpriteAtlas(LoadSave.LEVEL_ATLAS);

        levelSprite = new BufferedImage[48]; //4 in height x 12 in width of tiles
        for(int j = 0; j < 4; j++){
            for(int i = 0; i < 12; i++){
                int index = j*12 + i;
                levelSprite[index] = img.getSubimage(i* 32, j*32, 32, 32); //Tiles are 32x32 pixels
            }
        }

    }

    public void draw(Graphics g, int xLvlOffset){
        for(int j = 0; j < Game.TILES_IN_HEIGHT; j++){
            for(int i = 0; i < levels.get(lvlIndex).getLvlData()[0].length; i++){ //Uses levelOnes total width value instead of only visible width value
                int index = levels.get(lvlIndex).getSpriteIndex(i, j); //i is width and j is height in this nested loop
                int x = Game.TILES_SIZE * i - xLvlOffset;
                int y = Game.TILES_SIZE * j;
                if (index == 48) {
                    g.drawImage(waterSprite[aniIndex], x, y, Game.TILES_SIZE, Game.TILES_SIZE, null);
                }
                else if (index == 49) {
                    g.drawImage(waterSprite[4], x, y, Game.TILES_SIZE, Game.TILES_SIZE, null);
                }
                else{
                    g.drawImage(levelSprite[index], Game.TILES_SIZE * i - xLvlOffset, Game.TILES_SIZE * j, Game.TILES_SIZE, Game.TILES_SIZE, null); //x and y are multiples of i and j respectively, multiplied by size of each tile, multiplied by the scale of the game

                }
            }
        }
    }

    public void update(){ //updates levels (not including objects and entities) only environment
        updateWaterAnimation();
    }

    private void updateWaterAnimation() { //updates water animation
        aniTick++;
        if (aniTick >= 40) {
            aniTick = 0;
            aniIndex++;

            if (aniIndex >= 4)
                aniIndex = 0;
        }

    }

    public Level getCurrentLevel(){
        return levels.get(lvlIndex); //Returns current level, for now its just level 1
    }

    public int getAmountOfLevels(){
        return levels.size();
    }

    public int getLvlIndex(){
        return lvlIndex;
    }

    public void setLevelIndex(int lvlIndex) {
        this.lvlIndex = lvlIndex;
    }
}
