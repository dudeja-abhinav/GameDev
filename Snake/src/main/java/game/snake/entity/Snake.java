package game.snake.entity;

import game.snake.util.TurnPoint;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import static game.snake.util.Constants.*;

public class Snake {
    private final ArrayList<SnakeSegment> snakeSegments = new ArrayList<>();
    private int xVelocity = 0;
    private int yVelocity = 0;
    private int speed;

    public Snake(int x, int y, int snakeWidth, int snakeHeight, int length, int initialSpeed) {
        this.speed = initialSpeed;
        Random random = new Random(System.currentTimeMillis());
        if(random.nextInt(2) % 2 == 0){
            this.xVelocity = this.speed;
        } else {
            this.yVelocity = this.speed;
        }

        for(int i = 0; i < length; i++) {
            if(i == 0){
                snakeSegments.add(new SnakeSegment(x, y, SNAKE_WIDTH, SNAKE_HEIGHT, true, this.xVelocity, this.yVelocity, new LinkedList<>()));
            } else {
                addSnakeSegment();
            }
        }
    }

    public void keyPressed(KeyEvent e){
        if(yVelocity == 0){
            if(e.getKeyCode() == KeyEvent.VK_UP) {
                setYVelocity(-speed);
                setXVelocity(0);
            } else if(e.getKeyCode() == KeyEvent.VK_DOWN){
                setYVelocity(speed);
                setXVelocity(0);
            }
        } else if (xVelocity == 0){
            if(e.getKeyCode() == KeyEvent.VK_LEFT) {
                setXVelocity(-speed);
                setYVelocity(0);
            } else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                setXVelocity(speed);
                setYVelocity(0);
            }
        }
        for(int i = 1; i < snakeSegments.size(); i++){
            snakeSegments.get(i).addTurnPoint(snakeSegments.get(0).x, snakeSegments.get(0).y, Integer.signum(xVelocity), Integer.signum(yVelocity));
        }
    }

    public SnakeSegment getSnakeHead() {
        return snakeSegments.get(0);
    }

    public ArrayList<SnakeSegment> getSnakeSegments(){
        return snakeSegments.isEmpty() ? new ArrayList<>() : new ArrayList<>(snakeSegments);
    }

    public void increaseSpeed() {
        if(speed < 20){
            this.speed *= 2;
        }
    }

    public void checkCollisions(){
        //Check if the snake bit itself
        for(int i = 1; i < snakeSegments.size(); i++){
            if(snakeSegments.get(0).x == snakeSegments.get(i).x && snakeSegments.get(0).y == snakeSegments.get(i).y){
                for(int j = 0; j <  snakeSegments.size(); j++) {
                    setSegmentVelocity(j, 0, 0);
                }
                break;
            }
        }

        //Wrap snake to other side of the screen
        for(SnakeSegment snakeSegment : snakeSegments){
            if(snakeSegment.x < 0){
                snakeSegment.x = GAME_WIDTH - SNAKE_WIDTH;
            } else if (snakeSegment.x > (GAME_WIDTH - SNAKE_WIDTH)){
                snakeSegment.x = 0;
            }
            if(snakeSegment.y < 0) {
                snakeSegment.y = GAME_HEIGHT - SNAKE_HEIGHT;
            } else if (snakeSegment.y > (GAME_HEIGHT - SNAKE_HEIGHT)) {
                snakeSegment.y = 0;
            }
        }
    }

    public void addSnakeSegment() {
        SnakeSegment lastSegment = snakeSegments.get(snakeSegments.size() - 1);
        int newX = getNextSegmentX(lastSegment.x, lastSegment.xVelocity, lastSegment.width);
        int newY = getNextSegmentY(lastSegment.y, lastSegment.yVelocity, lastSegment.height);
        SnakeSegment newSegment = new SnakeSegment(newX, newY, lastSegment.width, lastSegment.height, false, lastSegment.xVelocity, lastSegment.yVelocity, lastSegment.turnPoints);
        snakeSegments.add(newSegment);
    }

    private int getNextSegmentX(int x, int xVelocity, int lastSegmentWidth){
        int newX = x;
        if(xVelocity > 0){
            newX -= lastSegmentWidth;
        } else if(xVelocity < 0){
            newX += lastSegmentWidth;
        }
        if(newX < 0) {
            newX = GAME_WIDTH - lastSegmentWidth;
        } else if(newX >= GAME_WIDTH){
            newX = 0;
        }
        return newX;
    }

    private int getNextSegmentY(int y, int yVelocity, int lastSegmentHeight){
        int newY = y;
        if(yVelocity > 0){
            newY -= lastSegmentHeight;
        } else if(yVelocity < 0){
            newY += lastSegmentHeight;
        }
        if(newY < 0) {
            newY = GAME_HEIGHT - lastSegmentHeight;
        } else if(newY >= GAME_WIDTH){
            newY = 0;
        }
        return newY;
    }

    public void setXVelocity(int xVelocity){
        this.xVelocity = xVelocity;
        SnakeSegment head = snakeSegments.get(0);
        head.setXVelocity(xVelocity);
    }

    public void setYVelocity(int yVelocity){
        this.yVelocity = yVelocity;
        SnakeSegment head = snakeSegments.get(0);
        head.setYVelocity(yVelocity);
    }

    public void setSegmentVelocity(int index, int xVelocity, int yVelocity){
        if(index < 0 || index >= snakeSegments.size()){
            return;
        }
        SnakeSegment segment = snakeSegments.get(index);
        segment.setXVelocity(xVelocity);
        segment.setYVelocity(yVelocity);
    }

    public void move() {
        for(SnakeSegment snakeSegment : snakeSegments){
            Queue<TurnPoint> turnPoints = snakeSegment.turnPoints;
            int xDirection = Integer.signum(snakeSegment.xVelocity);
            int yDirection = Integer.signum(snakeSegment.yVelocity);
            if(!turnPoints.isEmpty()){
                TurnPoint turnPoint = turnPoints.peek();
                if(turnPoint.x == snakeSegment.x && turnPoint.y == snakeSegment.y){
                    xDirection = turnPoint.xDirection;
                    yDirection = turnPoint.yDirection;
                    turnPoints.poll();
                }
            }
            snakeSegment.setXVelocity(speed * xDirection);
            snakeSegment.setYVelocity(speed * yDirection);
            snakeSegment.move();
        }
    }

    public void draw(Graphics g) {
        for(SnakeSegment snakeSegment : snakeSegments){
            snakeSegment.draw(g);
        }
    }
}
