package game.flappy.bird.entity;

import java.awt.*;

import static game.flappy.bird.util.Constants.*;

public class GameMenu extends Rectangle {

    public GameMenu() {
    }

    public void draw(Graphics g){
        g.setColor(Color.BLACK);
        g.setFont(new Font("Consolas", Font.PLAIN, 60));
        g.drawString(SET_BIRD_COLOR_MSG, (GAME_WIDTH/2) - ((SET_BIRD_COLOR_MSG.length()/2)*30) , (GAME_HEIGHT/2)-30);
        g.setFont(new Font("Consolas", Font.PLAIN, 30));
        g.setColor(Color.RED);
        g.drawString(SET_RED_COLOR_MSG, (GAME_WIDTH/2) - ((SET_RED_COLOR_MSG.length()/2)*15) , (GAME_HEIGHT/2));
        g.setColor(Color.BLUE);
        g.drawString(SET_BLUE_COLOR_MSG, (GAME_WIDTH/2) - ((SET_BLUE_COLOR_MSG.length()/2)*15) , (GAME_HEIGHT/2)+30);
        g.setColor(Color.YELLOW);
        g.drawString(SET_YELLOW_COLOR_MSG, (GAME_WIDTH/2) - ((SET_YELLOW_COLOR_MSG.length()/2)*15) , (GAME_HEIGHT/2)+60);
    }
}
