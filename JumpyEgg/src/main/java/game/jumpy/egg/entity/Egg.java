package game.jumpy.egg.entity;

import java.awt.*;

import static game.jumpy.egg.util.Constants.GRAVITY;

public class Egg extends Rectangle {
    public int xVelocity;
    public Egg(int x, int y, int eggDiameter) {
        super(x, y, eggDiameter, eggDiameter);
        this.xVelocity = 0;
    }

    public void setXVelocity(int xVelocity){
        this.xVelocity = xVelocity;
    }

    public void move(){
        x += xVelocity;
    }

    public void draw(Graphics g){
        g.setColor(Color.YELLOW);
        g.fillOval(x, y, width, height);
    }
}
