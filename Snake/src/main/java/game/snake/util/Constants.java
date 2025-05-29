package game.snake.util;

import java.awt.*;

public class Constants {
    public static final int GAME_WIDTH = 1260;
    public static final int GAME_HEIGHT = (int) (GAME_WIDTH * (5 / 9.0));
    public static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    public static final int SNAKE_WIDTH = 20;
    public static final int SNAKE_HEIGHT = 20;
    public static final int SNAKE_LENGTH = 5;
    public static final int SNAKE_SPEED = 5;
    public static final int FOOD_DIAMETER = 20;
    public static final String HIGH_SCORE_MSG = "High Score: ";
    public static final String RESET_MSG = "'R' Anytime to Reset The Game";
    public static final String GAME_CLOSE_MSG = "'Esc' Anytime To Close The Game";
    public static final String GAME_OVER_MSG = "Game Over! Press 'Enter' To Restart";
}
