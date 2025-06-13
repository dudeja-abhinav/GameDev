package game.plank.path.util;

import java.awt.*;

public class Constants {
    public static final int GAME_WIDTH = 1150;
    public static final int GAME_HEIGHT = (int) (GAME_WIDTH * (5 / 7.0));
    public static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    public static final int TOP_CELL_WIDTH = 45;
    public static final int BOTTOM_CELL_WIDTH = 55;
    public static final int PLANK_WIDHT = 45;
    public static final int MINIMUM_CELL_GAP = 70;
    public static final int MAX_ROWS = 6;
    public static final int MAX_COLUMNS = 10;
    public static final int PLAYER_SIZE = 25;
    public static final int PICKED_UP_PLANK_X = MINIMUM_CELL_GAP / 2;
    public static final int PICKED_UP_PLANK_Y = MINIMUM_CELL_GAP / 2;
    public static final int PLAYER_SPEED = 2;
}
