package game.wordle.entity;

import java.awt.*;

public class Letter extends Rectangle {
    private char value;
    public Letter(int x, int y, int width, int height, char value) {
        super(x, y, width, height);
        this.value = value;
    }

    public void setValue(char value){
        this.value = value;
    }

    public void draw(Graphics g, Color c){
        g.setColor(c);
        g.fillRect(x, y, width, height);
        g.setFont(new Font("Consolas", Font.PLAIN, 30));
        g.setColor(Color.BLACK);
        g.drawString(String.valueOf(value), x+(width/2)-5, y+5);
    }
}
