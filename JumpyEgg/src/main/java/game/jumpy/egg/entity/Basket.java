package game.jumpy.egg.entity;

import java.awt.*;

public class Basket extends Rectangle {
    private int xVelocity;
    private boolean moves;
    public Basket(int x, int y, int width, int height, boolean moves, int xVelocity){
        super(x, y, width, height);
        this.moves = moves;
        this.xVelocity = xVelocity;
    }

    public boolean isMoving(){
        return moves;
    }

    public void setXVelocity(int xVelocity){
        this.xVelocity = xVelocity;
    }

    public void move(){
        x += xVelocity;
    }

    public void draw(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(x, y, width, height);
    }
}
