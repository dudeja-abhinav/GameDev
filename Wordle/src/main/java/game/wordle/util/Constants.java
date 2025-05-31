package game.wordle.util;

import java.awt.*;

public class Constants {
    public static final int GAME_HEIGHT = 720;
    public static final int GAME_WIDTH = (int) (3 * GAME_HEIGHT / 4.0);
    public static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    public static final int WORD_LENGTH = 5;
    public static final int LETTER_WIDTH = 40;
    public static final int LETTER_HEIGHT = 40;
    public static final Color WRONG_LETTER_COLOR = Color.DARK_GRAY;
    public static final Color WRONG_POSITION_COLOR = Color.ORANGE;
    public static final Color CORRECT_POSITION_COLOR = Color.GREEN;
    public static final int CHANCES_PER_LEVEL = 6;

}
