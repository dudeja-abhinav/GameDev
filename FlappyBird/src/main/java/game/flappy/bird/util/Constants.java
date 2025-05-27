package game.flappy.bird.util;

import java.awt.*;

public class Constants {
    public static final int GAME_WIDTH = 1280;
    public static final int GAME_HEIGHT = (int) (GAME_WIDTH * (5/9.0));
    public static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    public static final int BIRD_DIAMETER = 50;
    public static final int BIRD_INITIAL_SPEED = -15;
    public static final int GRAVITY = 1;
    public static final int PIPE_WIDTH = 100;
    public static final int GAP_HEIGHT = 250;
    public static final int PIPE_SPEED = 2;
    public static final int FRAMES_PER_OBSTACLE = 160;
    public static final int POINTS_PER_GAP = 1;
    public static final String SET_BIRD_COLOR_MSG = "Set Bird Color To Start The Game";
    public static final String SET_RED_COLOR_MSG = "Press R to set colour to RED";
    public static final String SET_BLUE_COLOR_MSG = "Press B to set colour to BLUE";
    public static final String SET_YELLOW_COLOR_MSG = "Press Y to set color to YELLOW";
    public static final String GAME_OVER_MSG = "GAME OVER!!";
    public static final String GAME_RESET_MSG = "Press 'Enter' To Restart With Same Color";
}
