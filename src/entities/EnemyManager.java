package entities;

import gamestates.Playing;
import levels.Level;
import utils.LoadSave;
import static utils.Constants.EnemyConstants.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

//Similar role of LevelManager but this class manages all the enemies as well as the wizard npc in each level of the game
public class EnemyManager {

    private Playing playing;
    private BufferedImage[][] crowArr;
    private BufferedImage[][] darkFoxArr;
    private BufferedImage[][] golemArr;
    private BufferedImage[][] wizardArr;
    private ArrayList<PlagueCrow> plagueCrows = new ArrayList<>();
    private ArrayList<ParadoxGolem> paradoxGolems = new ArrayList<>();
    private ArrayList<Wizard> wizards = new ArrayList<>();




    public EnemyManager(Playing playing){
        this.playing = playing;
        loadEnemyImgs(); //Loads images for all enemies
    }

    public void loadEnemies(Level level) {
        plagueCrows = level.getCrows();
        paradoxGolems = level.getGolems();
        wizards = level.getWizards();

          //Used to debug
//        System.out.println("Size of crows: " + plagueCrows.size());
//        System.out.println("Size of dark foxes: " + darkFoxes.size());
//        System.out.println("Size of golems: " + paradoxGolems.size());
//        System.out.println("Size of wizards: " + wizards.size());


    }

    public void update(int[][] lvlData, Adventurer player){ //updates every crow in crows arrayList
        boolean isAnyActive = false;
        for(PlagueCrow crow: plagueCrows){
            if(crow.isActive()){
                crow.update(lvlData, playing); //passes along enemy data to each enemy
                isAnyActive = true;
            }
        }
        for(ParadoxGolem golems : paradoxGolems){
            if(golems.isActive()){
                golems.update(lvlData, playing);
                isAnyActive = true;
            }
        }
        for(Wizard wizard : wizards){
            if(wizard.isActive()){
                wizard.update(lvlData, playing);
            }
        }
        if(!isAnyActive){
            playing.setLevelCompleted(true);
        }
    }

    public void draw(Graphics g, int xLvlOffset){
        drawCrows(g, xLvlOffset);
        drawGolems(g, xLvlOffset);
        drawWizards(g, xLvlOffset);
    }

    private void drawCrows(Graphics g, int xLvlOffset) { //Draws each crow in arrayList of crows at different indexes depending on hitbox location
        for(PlagueCrow crow: plagueCrows){
            if(crow.isActive()){
                g.drawImage(crowArr[crow.getState()][crow.getAnimationIndex()],
                        (int)(crow.getHitbox().x - CROW_DRAWOFFSET_X - xLvlOffset) + crow.flipX(),
                        (int)(crow.getHitbox().y - CROW_DRAWOFFSET_Y), CROW_WIDTH * crow.flipW(),
                        CROW_HEIGHT, null);
                //crow.drawHitbox(g, xLvlOffset);
                //crow.drawAttackBox(g, xLvlOffset);
            }
        }
    }

    private void drawWizards(Graphics g, int xLvlOffset) { //Draws each crow in arrayList of crows at different indexes depending on hitbox location
        for(Wizard wizard: wizards){
            if(wizard.isActive()){
                g.drawImage(wizardArr[wizard.getState()][wizard.getAnimationIndex()],
                        (int)(wizard.getHitbox().x - WIZARD_DRAWOFFSET_X - xLvlOffset) + wizard.flipX(),
                        (int)(wizard.getHitbox().y + WIZARD_DRAWOFFSET_Y), WIZARD_WIDTH * wizard.flipW(),
                        WIZARD_HEIGHT, null);
                //wizard.drawHitbox(g, xLvlOffset);
                //wizard.drawAttackBox(g, xLvlOffset);
                //DRAWS THE GAME INSTRUCTIONS AT THE START OF THE FIRST LEVEL
                if(wizard.isCanSeePlayer() && playing.getLevelManager().getLvlIndex() == 0){
                    wizard.drawInstructionsDialogue(g);
                }
            }
        }
    }

    private void drawGolems(Graphics g, int xLvlOffset) { //Draws each crow in arrayList of crows at different indexes depending on hitbox location
        for(ParadoxGolem golem: paradoxGolems){
            if(golem.isActive()){
                g.drawImage(golemArr[golem.getState()][golem.getAnimationIndex()],
                        (int)(golem.getHitbox().x - GOLEM_DRAWOFFSET_X - xLvlOffset) + golem.flipX(),
                        (int)(golem.getHitbox().y - GOLEM_DRAWOFFSET_Y), GOLEM_WIDTH * golem.flipW(),
                        GOLEM_HEIGHT, null);
                //golem.drawHitbox(g, xLvlOffset);
                //golem.drawAttackBox(g, xLvlOffset);
                //golem.drawLaserAttackBox(g, xLvlOffset);
            }
        }
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox, Adventurer player){
        for(PlagueCrow c: plagueCrows){
            if(c.isActive()) { //if enemy is active
                if (c.getCurrentHealth() > 0) { //only if enemy has health left
                    if (attackBox.intersects(c.getHitbox())) {
                        c.hurt(player.getPlayerAtk()); //choose how much damage player can do to enemy
                        return;
                    }
                }
            }
        }
        for(ParadoxGolem golem: paradoxGolems){
            if(golem.isActive()) { //if enemy is active
                if (golem.getCurrentHealth() > 0) { //only if enemy has health left
                    if (attackBox.intersects(golem.getHitbox())) {
                        golem.bossHurt(player.getPlayerAtk()); //choose how much damage player can do to enemy
                        return;
                    }
                }
            }
        }
    }



    private void loadEnemyImgs() {
        crowArr = new BufferedImage[7][6]; //puts spritesheet into a 2d array
        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.PLAGUE_CROW_SPRITE);
        for(int j = 0; j <crowArr.length; j++){
            for(int i = 0; i < crowArr[j].length; i++){
                crowArr[j][i] = temp.getSubimage(i * CROW_WIDTH_DEFAULT, j * CROW_HEIGHT_DEFAULT, CROW_WIDTH_DEFAULT, CROW_HEIGHT_DEFAULT);
            }
        }
        golemArr = new BufferedImage[9][9];
        BufferedImage temp3 = LoadSave.getSpriteAtlas(LoadSave.BOSS_SPRITE);
        for(int j = 0; j < golemArr.length; j++){
            for(int i = 0; i < golemArr[j].length; i++){
                golemArr[j][i] = temp3.getSubimage(i * GOLEM_WIDTH_DEFAULT, j * GOLEM_HEIGHT_DEFAULT, GOLEM_WIDTH_DEFAULT, GOLEM_HEIGHT_DEFAULT);
            }
        }

        wizardArr = new BufferedImage[9][7];
        BufferedImage temp4 = LoadSave.getSpriteAtlas(LoadSave.WIZARD_IMAGE);
        for(int j = 0; j < wizardArr.length; j++){
            for(int i = 0; i < wizardArr[j].length; i++){
                wizardArr[j][i] = temp4.getSubimage(i * WIZARD_WIDTH_DEFAULT, j * WIZARD_HEIGHT_DEFAULT, WIZARD_WIDTH_DEFAULT, WIZARD_HEIGHT_DEFAULT);
            }
        }

    }

    public void resetAllEnemies(){
        for(PlagueCrow crow : plagueCrows){ //resets every crow in the crows arraylist
            crow.resetEnemy();
        }
        for(ParadoxGolem golem : paradoxGolems){
            golem.resetEnemy();
        }
        for(Wizard wizard : wizards){
            wizard.resetEnemy();
        }
    }

}
