package game.plank.path.entity;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

import static game.plank.path.util.Constants.PLAYER_SPEED;

public class Player extends Rectangle {
    public int direction;
    public int xVelocity;
    public int yVelocity;
    public int cellId;
    public int x_coord;
    public int y_coord;

    public Player(int x, int y, int size, int direction, int cellId, int x_coord, int y_coord){
        super(x, y, size, size);
        this.direction = direction;
        this.cellId = cellId;
        this.x_coord = x_coord;
        this.y_coord = y_coord;
        setXVelocity(0);
        setYVelocity(0);
    }

    public void setVelocity(int direction){
        if(direction == 0){
            setYVelocity(-PLAYER_SPEED);
        } else if(direction == 1){
            setXVelocity(-PLAYER_SPEED);
        } else if(direction == 2){
            setYVelocity(PLAYER_SPEED);
        } else if(direction == 3){
            setXVelocity(PLAYER_SPEED);
        }
    }

    public void stop(){
        setXVelocity(0);
        setYVelocity(0);
    }

    public void setXVelocity(int xVelocity) {
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
        Graphics2D g2d = (Graphics2D) g;
        // Enable anti-aliasing for smoother curves
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Polygon triangle = new Polygon();
        if(direction == 0){
            triangle.addPoint(x, y + (width/2));
            triangle.addPoint(x + width, y + (width/2));
            triangle.addPoint(x + (width/2), y - (width/2));
        } else if(direction == 1) {
            triangle.addPoint(x + (width/2), y);
            triangle.addPoint(x + (width/2), y + width);
            triangle.addPoint(x - (width/2), y + (width/2));
        } else if(direction == 2){
            triangle.addPoint(x, y + (width/2));
            triangle.addPoint(x + width, y + (width/2));
            triangle.addPoint(x + (width/2), y + (3 * width/2));
        } else if(direction == 3) {
            triangle.addPoint(x + (width/2), y);
            triangle.addPoint(x + (width/2), y + width);
            triangle.addPoint(x + (3 * width/2), y + (width/2));
        }
        g2d.setColor(Color.ORANGE);
        g2d.fill(triangle);
        g2d.setColor(Color.WHITE);
        Ellipse2D playerCircle = new Ellipse2D.Double(x, y, width, height);
        g2d.fill(playerCircle);
    }
}
