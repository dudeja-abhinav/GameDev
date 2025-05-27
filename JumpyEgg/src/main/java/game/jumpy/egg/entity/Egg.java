package game.jumpy.egg.entity;

import java.awt.*;

import static game.jumpy.egg.util.Constants.GRAVITY;

public class Egg extends Rectangle {
    private int yVelocity;
    private int xVelocity;
    public Egg(int x, int y, int eggDiameter) {
        super(x, y, eggDiameter, eggDiameter);
        this.xVelocity = 0;
        this.yVelocity = 0;
    }

    public void setYVelocity(int yVelocity){
        this.yVelocity = yVelocity;
    }

    public void setXVelocity(int xVelocity){
        this.xVelocity = xVelocity;
    }

    public void applyGravity(){
        this.yVelocity += GRAVITY;
    }

    public void move(){
        x += xVelocity;
        y += yVelocity;
    }

    public void draw(Graphics g){
        g.setColor(Color.YELLOW);
        g.fillOval(x, y, width, height);
    }
}
