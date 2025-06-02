package game.pong.entity;

import java.awt.*;
import java.util.Random;

public class Ball extends Rectangle {
    Random random;
    public int xVelocity;
    public int yVelocity;
    public int initialSpeed = 2;

    public Ball(int x, int y, int width, int height){
        super(x, y, width, height);
        random = new Random(System.currentTimeMillis());
        int randomXDirection = random.nextInt(2) % 2 == 0 ? -1 : 1;
        int randomYDirection = random.nextInt(2) % 2 == 0 ? -1 : 1;
        setXDirection(randomXDirection * initialSpeed);
        setYDirection(randomYDirection * initialSpeed);
    }

    public void setXDirection(int randomXDirection){
        xVelocity = randomXDirection;
    }
    public void setYDirection(int randomYDirection){
        yVelocity = randomYDirection;
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
