package game.snake.entity;

import java.awt.*;

import static game.snake.util.Constants.*;

public class Score extends Rectangle {
    private static int highScore = 0;
    public int points;

    public Score(){
        points = 0;
    }

    public void addPoints(int points){
        this.points += points;
        if(this.points > highScore){
            highScore = this.points;
        }
    }

    public void draw(Graphics g){
        g.setColor(Color.WHITE);
        g.setFont(new Font("Consolas", Font.PLAIN, 80));
        g.drawString(String.valueOf(points/10) + String.valueOf(points%10), (GAME_WIDTH/2) - 40, (GAME_HEIGHT/2) - 80);
        g.setFont(new Font("Consolas", Font.PLAIN, 50));
        g.drawString(HIGH_SCORE_MSG + String.valueOf(highScore/10) + String.valueOf(highScore%10), (GAME_WIDTH/2) - ((HIGH_SCORE_MSG.length()/2)*25) - 25, (GAME_HEIGHT/2));
        g.setFont(new Font("Consolas", Font.PLAIN, 30));
        g.drawString(GAME_OVER_MSG, (GAME_WIDTH/2) - ((GAME_OVER_MSG.length()/2) * 15), (GAME_HEIGHT/2) + 60);
        g.drawString(RESET_MSG, (GAME_WIDTH/2) - ((RESET_MSG.length()/2) * 15), (GAME_HEIGHT/2) + 100);
        g.drawString(GAME_CLOSE_MSG, (GAME_WIDTH/2) - ((GAME_CLOSE_MSG.length()/2) * 15), (GAME_HEIGHT/2) + 140);
    }
}
