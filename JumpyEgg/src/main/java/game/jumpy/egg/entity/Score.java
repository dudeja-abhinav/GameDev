package game.jumpy.egg.entity;

import java.awt.*;

import static game.jumpy.egg.util.Constants.GAME_WIDTH;

public class Score extends Rectangle {
    public int points;
    private static int highScore = 0;

    public Score(){
        this.points = 0;
    }

    public void addPoint(){
        this.points++;
        if(points > highScore){
            highScore = points;
        }
    }

    public void draw(Graphics g){
        g.setColor(Color.BLACK);
        g.setFont(new Font("Consolas", Font.PLAIN, 40));
        g.drawString(String.valueOf(points / 10)+String.valueOf(points % 10), 5, 40);
        g.setColor(Color.RED);
        g.drawString(String.valueOf(highScore/10) + String.valueOf(highScore%10), GAME_WIDTH - 50, 40);
    }
}
