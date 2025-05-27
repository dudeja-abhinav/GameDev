package game.flappy.bird.entity;

import java.awt.*;
import java.awt.event.KeyEvent;

import static game.flappy.bird.util.Constants.BIRD_INITIAL_SPEED;
import static game.flappy.bird.util.Constants.GRAVITY;

public class Bird extends Rectangle {

    public int yVelocity;
    public Color birdColor;

    public Bird(int x, int y, int birdDiameter, Color birdColor){
        super(x, y, birdDiameter, birdDiameter);
        this.birdColor = birdColor;
        setYVelocity(BIRD_INITIAL_SPEED);
    }

    public void keyPressed(){
        yVelocity = BIRD_INITIAL_SPEED;
    }

    public void setYVelocity(int yVelocity){
        this.yVelocity = yVelocity;
    }

    public void move(){
        y += yVelocity;
    }

    public void draw(Graphics g){
        g.setColor(birdColor);
        g.fillOval(x, y, width, height);
    }
}
