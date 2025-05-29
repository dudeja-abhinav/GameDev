package game.snake.entity;

import game.snake.util.TurnPoint;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

public class SnakeSegment extends Rectangle {
    public boolean isHead;
    public int xVelocity;
    public int yVelocity;
    public Queue<TurnPoint> turnPoints = new LinkedList<>();

    public SnakeSegment(int x, int y, int width, int height, boolean isHead, int xVelocity, int yVelocity, Queue<TurnPoint> turnPoints){
        super(x, y, width, height);
        setXVelocity(xVelocity);
        setYVelocity(yVelocity);
        this.turnPoints.addAll(turnPoints);
        this.isHead = isHead;
    }

    public void addTurnPoint(int x, int y, int xDirection, int yDirection){
        turnPoints.add(new TurnPoint(x, y, xDirection, yDirection));
    }

    public void setXVelocity(int xVelocity){
        this.xVelocity = xVelocity;
    }

    public void setYVelocity(int yVelocity) {
        this.yVelocity = yVelocity;
    }

    public void move(){
        x += xVelocity;
        y += yVelocity;
    }

    public void draw(Graphics g){
        if(isHead){
            g.setColor(Color.green);
        } else {
            g.setColor(Color.yellow);
        }
        g.fillRect(x, y, width, height);
    }
}
