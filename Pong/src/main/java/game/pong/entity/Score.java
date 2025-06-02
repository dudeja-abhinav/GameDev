package game.pong.entity;

import java.awt.*;

public class Score extends Rectangle {
    static int GAME_WIDTH;
    static int GAME_HEIGHT;
    public int player1;
    public int player2;

    public Score(int gameWidth, int gameHeight){
        Score.GAME_WIDTH = gameWidth;
        Score.GAME_HEIGHT = gameHeight;
        player1 = 0;
        player2 = 0;
    }

    public void draw(Graphics g){
        //Draw the score on the screen
        g.setColor(Color.white);
        g.setFont(new Font("Consolas", Font.PLAIN, 60));
        g.drawLine(GAME_WIDTH/2, 0, GAME_WIDTH/2, GAME_HEIGHT);
        g.drawString(String.valueOf(player1/10) + String.valueOf(player1%10), (GAME_WIDTH/2) - 85, 50);
        g.drawString(String.valueOf(player2/10) + String.valueOf(player2%10), (GAME_WIDTH/2) + 20, 50);
    }
}
