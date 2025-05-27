package game.flappy.bird.entity;

import java.awt.*;

public class Gap extends Rectangle {

    public int xVelocity;
    public Gap(int x, int y, int width, int height, int xVelocity) {
        super(x, y, width, height);
        this.setXVelocity(xVelocity);
    }

    public void setXVelocity(int xVelocity){
        this.xVelocity = xVelocity;
    }

    public void move(){
        this.x -= xVelocity;
    }
}
