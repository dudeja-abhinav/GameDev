package game.flappy.bird.entity;

import java.awt.*;

import static game.flappy.bird.util.Constants.*;

public class Score extends Rectangle {
    public int points;

    public Score(){
        points = 0;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public void draw(Graphics g){
        g.setColor(Color.BLACK);
        g.setFont(new Font("Consolas", Font.PLAIN, 80));
        g.drawString(GAME_OVER_MSG, (GAME_WIDTH/2) - ((GAME_OVER_MSG.length()/2)*40), GAME_HEIGHT/2 - 160);
        g.drawString(String.valueOf(points/10) + String.valueOf(points % 10), (GAME_WIDTH/2) - 40, GAME_HEIGHT/2 - 80);
        g.setFont(new Font("Consolas", Font.PLAIN, 30));
        g.drawString(GAME_RESET_MSG, (GAME_WIDTH/2) - ((GAME_RESET_MSG.length()/2)*15) , (GAME_HEIGHT/2));
        g.setColor(Color.RED);
        g.drawString(SET_RED_COLOR_MSG, (GAME_WIDTH/2) - ((SET_RED_COLOR_MSG.length()/2)*15) , (GAME_HEIGHT/2)+30);
        g.setColor(Color.BLUE);
        g.drawString(SET_BLUE_COLOR_MSG, (GAME_WIDTH/2) - ((SET_BLUE_COLOR_MSG.length()/2)*15) , (GAME_HEIGHT/2)+60);
        g.setColor(Color.YELLOW);
        g.drawString(SET_YELLOW_COLOR_MSG, (GAME_WIDTH/2) - ((SET_YELLOW_COLOR_MSG.length()/2)*15) , (GAME_HEIGHT/2)+90);
    }
}
