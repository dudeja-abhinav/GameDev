package game.flappy.bird.entity;

import java.awt.*;

public class Pipe extends Rectangle {
    private int xVelocity;

    public Pipe(int x, int y, int width, int height, int xVelocity){
        super(x, y, width, height);
        this.setXVelocity(xVelocity);
    }

    public void setXVelocity(int xVelocity){
        this.xVelocity = xVelocity;
    }

    public void move(){
        this.x -= xVelocity;
    }

    public void draw(Graphics g){
        g.setColor(Color.GREEN);
        g.fillRect(x, y, width, height);
    }
}
