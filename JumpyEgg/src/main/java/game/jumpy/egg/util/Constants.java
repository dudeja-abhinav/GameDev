package game.jumpy.egg.util;

import java.awt.*;

public class Constants {
    public static final int GAME_HEIGHT = 720;
    public static final int GAME_WIDTH = (int) (9 * GAME_HEIGHT / 16.0);
    public static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    public static final int GRAVITY = 1;
    public static final int EGG_DIAMETER = 20;
    public static final int BASKET_WIDTH = 60;
    public static final int BASKET_HEIGHT = 20;
    public static final int MAX_BASKET_SPEED = 3;
    public static final int EGG_JUMP_VELOCITY = 18;
}
