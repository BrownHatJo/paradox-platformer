package entities;

import gamestates.Playing;
import main.Game;

import java.awt.*;

import static utils.Constants.EnemyConstants.*;

public class Wizard extends Enemy{

    private boolean canSeePlayer;
    public Wizard(float x, float y) {
        super(x, y, (int)(WIZARD_WIDTH * Game.SCALE), (int)(WIZARD_HEIGHT *Game.SCALE), WIZARD);
        initHitbox(16, 30);
    }

    public void update(int[][] lvlData, Playing playing){
        updateBehavior(lvlData, playing);
        updateAnimationTick();
        //System.out.println(canSeePlayer);
    }

    private void updateBehavior(int[][] lvlData, Playing playing){
        if(inAir){ //if enemy is in the air
            inAirChecks(lvlData);
            if(canSeePlayer(lvlData, playing.getPlayer())){
                turnToPlayer(playing.getPlayer());
                canSeePlayer = true;
            }
            else{
                canSeePlayer = false;
            }
        }
        else{
            if(isPlayerInRange(playing.getPlayer())){
                turnToPlayer(playing.getPlayer());
                canSeePlayer = true;
            }
            else{
                canSeePlayer = false;
            }
        }
    }

    public void drawInstructionsDialogue(Graphics g){
        g.setColor(Color.WHITE);
        g.drawString("You and your partner must collect the paradox shard at the end of your respective levels", (int)(hitbox.x - 55), (int)(hitbox.y - 195));
        g.drawString("to complete levels 1 and 2, then you must complete levels 3 and 4 by collecting more shards and defeating enemies", (int)(hitbox.x - 55), (int)(hitbox.y - 180));
        g.drawString("to collide paradoxes. Finally, work together to collect 10 final shards and defeat enemies to reach the final level", (int)(hitbox.x - 55), (int)(hitbox.y - 165));
        g.drawString("and attempt to defeat the Paradox Golem", (int)(hitbox.x - 55), (int)(hitbox.y - 150));
        g.drawString("You will see me in each level, I am but an NPC to guide you to victory", (int)(hitbox.x - 55), (int)(hitbox.y - 135));
        g.drawString("You must defeat the Golem yourselves if you wish to win the game...", (int)(hitbox.x - 55), (int)(hitbox.y - 120));
        g.drawString("Be wary of its deadly laser attack... good luck players...", (int)(hitbox.x - 55), (int)(hitbox.y - 105));
        g.drawString("|", (int)(hitbox.x), (int)(hitbox.y - 90));
        g.drawString("|", (int)(hitbox.x), (int)(hitbox.y - 75));
        g.drawString("|", (int)(hitbox.x), (int)(hitbox.y - 60));
        g.drawString("|", (int)(hitbox.x), (int)(hitbox.y - 45));
        g.drawString("|", (int)(hitbox.x), (int)(hitbox.y - 30));
        g.drawString("|", (int)(hitbox.x), (int)(hitbox.y - 15));
    }

    public boolean isCanSeePlayer(){
        return canSeePlayer;
    }


}