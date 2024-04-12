package utils;

//this file stores all the constant values used to animate and display the different entities and objects in the game

import main.Game;

public class Constants {

    public static final int ANIMATION_SPEED = 25; //animation speed constant only currently used in objects classes
    public static final float GRAVITY = 0.04f * Game.SCALE; //A constant gravity value for both players and enemies

    public static class Projectiles{
        //width and height of cannon ball projectiles
        public static final int CANNON_BALL_DEFAULT_WIDTH = 15;
        public static final int CANNON_BALL_DEFAULT_HEIGHT = 15;
        public static final int CANNON_BALL_WIDTH = (int)(Game.SCALE * CANNON_BALL_DEFAULT_WIDTH);
        public static final int CANNON_BALL_HEIGHT = (int)(Game.SCALE * CANNON_BALL_DEFAULT_HEIGHT);
        public static final float SPEED = 0.5f * Game.SCALE; //subject to change
        //speed of projectile every update

        public static final int GOLEM_PROJECTILE_DEFAULT_WIDTH = 35;
        public static final int GOLEM_PROJECTILE_DEFAULT_HEIGHT = 14;
        public static final int GOLEM_PROJECTILE_WIDTH = (int)(Game.SCALE * GOLEM_PROJECTILE_DEFAULT_WIDTH);
        public static final int GOLEM_PROJECTILE_HEIGHT = (int)(Game.SCALE * GOLEM_PROJECTILE_DEFAULT_HEIGHT);

        public static final int LASER_DEFAULT_WIDTH = 1000;
        public static final int LASER_DEFAULT_HEIGHT = 100;
        public static final int LASER_W = (int)(Game.SCALE * LASER_DEFAULT_WIDTH);
        public static final int LASER_H = (int)(Game.SCALE * LASER_DEFAULT_HEIGHT);



    }
    public static class ObjectConstants{ //Constant used for all objects in games
        //index values for all 5 objects
        public static final int NIGHTBORNE_POTION = 0;
        public static final int GHOST_POTION = 1;
        public static final int PARADOX_SHARD = 2;
        public static final int BARREL = 3;
        public static final int BOX = 4;
        public static final int SPIKE = 5;
        public static final int CANNON_LEFT = 6;
        public static final int CANNON_RIGHT = 7;

        public static final int NIGHTBORNE_POTION_VALUE = 150;
        public static final int GHOST_POTION_VALUE = 125;

        //Width and height of both types of containers
        public static final int CONTAINER_WIDTH_DEFAULT = 40;
        public static final int CONTAINER_HEIGHT_DEFAULT = 30;
        public static final int CONTAINER_WIDTH = (int)(Game.SCALE * CONTAINER_WIDTH_DEFAULT);
        public static final int CONTAINER_HEIGHT = (int)(Game.SCALE * CONTAINER_HEIGHT_DEFAULT);

        //Width and Height of all 3 items
        public static final int ITEM_WIDTH_DEFAULT = 12;
        public static final int ITEM_HEIGHT_DEFAULT = 16;
        public static final int ITEM_WIDTH = (int)(Game.SCALE * ITEM_WIDTH_DEFAULT);
        public static final int ITEM_HEIGHT = (int)(Game.SCALE * ITEM_HEIGHT_DEFAULT);

        //Width and Height of spikes
        public static final int SPIKE_WIDTH_DEFAULT = 32;
        public static final int SPIKE_HEIGHT_DEFAULT = 32;
        public static final int SPIKE_WIDTH = (int)(Game.SCALE * SPIKE_WIDTH_DEFAULT);
        public static final int SPIKE_HEIGHT = (int)(Game.SCALE * SPIKE_HEIGHT_DEFAULT);

        //width and height of cannons
        public static final int CANNON_WIDTH_DEFAULT = 40;
        public static final int CANNON_HEIGHT_DEFAULT = 26;
        public static final int CANNON_WIDTH = (int)(CANNON_WIDTH_DEFAULT * Game.SCALE);
        public static final int CANNON_HEIGHT = (int)(CANNON_HEIGHT_DEFAULT * Game.SCALE);

        public static int GetSpriteAmount(int object_type){ //returns number of frames required depending on object
            switch(object_type){
                case NIGHTBORNE_POTION, GHOST_POTION:
                    return 7;
                case PARADOX_SHARD:
                    return 4;
                case BARREL, BOX:
                    return 8;
                case CANNON_LEFT, CANNON_RIGHT:
                    return 7; //there are 7 animation sprites in cannon array
            }
            return 1;
        }

    }
    public static class EnemyConstants{ //constants used by the enemies in the game
        public static final int PLAGUE_CROW = 0;
        public static final int WIZARD = 1;
        public static final int PARADOX_GOLEM = 10;

        //^ Up here goes all the different enemy types and their constants

        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int ATTACK = 2;
        public static final int HIT = 3;
        public static final int DEATH_1 = 4;
        public static final int DEATH_2 = 5;
        public static final int JUMPING = 6;

        //BOSS ANIMATION CONSTANTS
        //public static final int BOSS_IDLE = 0;
        public static final int BOSS_ACTIVE = 1;
        //public static final int BOSS_ATTACK = 2;
        public static final int BOSS_SHIELD = 3;
        public static final int BOSS_ARM_LOST = 4; //probably going to be unused
        public static final int SHOOTING_LASER = 5;
        public static final int BOSS_HIT = 6;
        public static final int BOSS_DEATH_1 = 7;
        public static final int BOSS_DEATH_2 = 8;

        public static final int CROW_WIDTH_DEFAULT = 64;
        public static final int CROW_HEIGHT_DEFAULT = 64;

        public static final int CROW_WIDTH = (int)(CROW_WIDTH_DEFAULT * Game.SCALE);
        public static final int CROW_HEIGHT = (int)(CROW_HEIGHT_DEFAULT * Game.SCALE);

        public static final int CROW_DRAWOFFSET_X = (int)(24 * Game.SCALE);
        public static final int CROW_DRAWOFFSET_Y = (int)(24 * Game.SCALE);

        public static final int GOLEM_WIDTH_DEFAULT = 100;
        public static final int GOLEM_HEIGHT_DEFAULT = 100;

        public static final int GOLEM_WIDTH = (int)(GOLEM_WIDTH_DEFAULT * Game.SCALE);
        public static final int GOLEM_HEIGHT = (int)(GOLEM_HEIGHT_DEFAULT * Game.SCALE);

        public static final int GOLEM_DRAWOFFSET_X = (int)(12 * Game.SCALE);
        public static final int GOLEM_DRAWOFFSET_Y = (int)(40 * Game.SCALE);

        public static final int WIZARD_WIDTH_DEFAULT = 32;
        public static final int WIZARD_HEIGHT_DEFAULT = 28;

        public static final int WIZARD_WIDTH = (int)(WIZARD_WIDTH_DEFAULT * Game.SCALE);
        public static final int WIZARD_HEIGHT = (int)(WIZARD_HEIGHT_DEFAULT * Game.SCALE);

        public static final int WIZARD_DRAWOFFSET_X = (int)(12 * Game.SCALE);
        public static final int WIZARD_DRAWOFFSET_Y = (int)(6 * Game.SCALE);

        public static int getSpriteAmount (int enemy_type, int enemy_state){
            switch(enemy_type){
                case PLAGUE_CROW:
                    switch(enemy_state){
                        case IDLE:
                        case RUNNING:
                            return 4;
                        case ATTACK:
                        case DEATH_2:
                            return 5;
                        case HIT:
                            return 3;
                        case DEATH_1:
                        case JUMPING:
                            return 6;
                    }
                case PARADOX_GOLEM:
                    switch ((enemy_state)){
                        case IDLE:
                        case BOSS_DEATH_2:
                            return 4;
                        case BOSS_ACTIVE:
                        case BOSS_SHIELD:
                            return 8;
                        case BOSS_ARM_LOST:
                        case BOSS_HIT:
                            return 7;
                        case ATTACK:
                            return 9;
                        case SHOOTING_LASER:
                            return 6;
                        case BOSS_DEATH_1:
                            return 10;
                        default:
                            return 1;
                    }
                case WIZARD:
                    switch (enemy_state){
                        case IDLE:
                            return 2;
                    }
            }
            return 0;
        }

        public static int getMaxHealth(int enemy_type){
            switch(enemy_type){
                case PLAGUE_CROW:
                    return 50;
                case PARADOX_GOLEM:
                    return 2500;
                default:
                    return 1;
            }
        }

        public static int getEnemyDmg(int enemy_type){
            switch(enemy_type){
                case PLAGUE_CROW:
                    return 10;
                case PARADOX_GOLEM:
                    return 100;
                default:
                    return 0;
            }

        }
    }

    public static class Environment{
        public static final int CAVE_WIDTH_DEFAULT = 960;
        public static final int CAVE_HEIGHT_DEFAULT = 480;

        public static final int CAVE_WIDTH = (int)(CAVE_WIDTH_DEFAULT * Game.SCALE);
        public static final int CAVE_HEIGHT = (int)(CAVE_HEIGHT_DEFAULT * Game.SCALE);
    }
    public static class UI{ //Class used for all UI related things

        public static class Buttons{
            public static final int B_WIDTH_DEFAULT = 140; //Default width of button
            public static final int B_HEIGHT_DEFAULT = 56; //Default height of button
            public static final int B_WIDTH = (int)(B_WIDTH_DEFAULT * Game.SCALE); //Actually width of button relative to game scale
            public static final int B_HEIGHT = (int)(B_HEIGHT_DEFAULT * Game.SCALE); //Actually height of button relative to game scale
        }

        public static class PauseButtons{ //Width and height of buttons r both same
            public static final int SOUND_SIZE_DEFAULT = 42; //So they are both initialized by this variable for size
            public static final int SOUND_SIZE = (int)(SOUND_SIZE_DEFAULT * Game.SCALE); //Same reason there is only a sound size var here

        }

        public static class URMButtons{ //values for pause, replay and menu
            public static final int URM_DEFAULT_SIZE = 56;
            public static final int URM_SIZE = (int)(URM_DEFAULT_SIZE * Game.SCALE);
        }

        public static class VolumeButtons{ //Variables for volume slider
            public static final int VOLUME_DEFAULT_WIDTH = 28;
            public static final int VOLUME_DEFAULT_HEIGHT = 44; //This height is also used for the slider as well as button
            public static final int SLIDER_DEFAULT_WIDTH = 215;

            public static final int VOLUME_WIDTH = (int)(VOLUME_DEFAULT_WIDTH * Game.SCALE);
            public static final int VOLUME_HEIGHT = (int)(VOLUME_DEFAULT_HEIGHT * Game.SCALE);
            public static final int SLIDER_WIDTH = (int)(SLIDER_DEFAULT_WIDTH * Game.SCALE);

        }
    }
    public static class Directions{
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
    }

    public static class PlayerConstants{
        public static final int IDLE = 0; //These integers are used to traverse the row in the player_animations file
        public static final int RUNNING = 1;
        public static final int JUMP = 2;
        public static final int FALLING = 3;
        public static final int GROUND = 4;
        public static final int HIT = 5;
        public static final int ATTACK_1 = 6;
        public static final int ATTACK_2 = 7;
        public static final int ATTACK_HEAVY = 8;
        public static final int DEAD = 9;

        public static int getSpriteAmount(int playerAction){

            switch(playerAction){

                case RUNNING:
                    return 5;
                case JUMP:
                    return 3; //Change to 10 for full somersault
                case IDLE:
                case HIT:
                case ATTACK_2:
                    return 4;
                case FALLING:
                    return 2;
                case GROUND:
                    return 2;
                case ATTACK_1:
                case DEAD:
                    return 6;
                case ATTACK_HEAVY:
                    return 7;
                default:
                    return 1;
            }
        }


    }

    public static class GhostConstants{ //Player 2
        public static final int IDLE = 0;
        public static final int BLINK = 1;
        public static final int WALK = 2;
        public static final int RUN = 3;
        public static final int CROUCH = 4;
        public static final int JUMP = 5;
        public static final int CLOAK = 6; //Can also be used for teleport
        public static final int DIE = 7;
        public static final int ATTACK = 8;

        public static int getSpriteAmount(int playerAction){

            switch(playerAction){

                case IDLE:
                case BLINK:
                    return 2;
                case WALK:
                case CLOAK:
                    return 4;
                case CROUCH:
                    return 6; //Crouches then uncrounches
                case JUMP:
                case DIE:
                case ATTACK:
                case RUN:
                    return 8;
                default:
                    return 1;
            }
        }
    }
}
