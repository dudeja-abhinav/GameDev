package game.jumpy.egg.entity;

import java.awt.*;

public class Basket extends Rectangle {
    public int xVelocity;
    public int yVelocity;
    public boolean moves;
    public Basket(int x, int y, int width, int height, boolean moves, int xVelocity){
        super(x, y, width, height);
        this.moves = moves;
        this.xVelocity = xVelocity;
        this.yVelocity = 0;
    }

    public boolean isMoving(){
        return moves;
    }

    public void setXVelocity(int xVelocity){
        this.xVelocity = xVelocity;
    }
    public void setYVelocity(int yVelocity){
        this.yVelocity = yVelocity;
    }

    public void move(){
        x += xVelocity;
        y += yVelocity;
    }

    public void draw(Graphics g){
        g.setColor(Color.ORANGE);
        g.fillRect(x, y, width, height);
    }
}
