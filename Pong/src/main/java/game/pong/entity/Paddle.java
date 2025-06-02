package game.pong.entity;

import java.awt.*;
import java.awt.event.KeyEvent;


public class Paddle extends Rectangle {
    int id;
    int yVelocity;
    int speed = 10;
    public Paddle(int x, int y, int paddleWidth, int paddleHeight, int id){
        super(x, y, paddleWidth, paddleHeight);
        this.id = id;
    }

    public void keyPressed(KeyEvent e){
        switch (id){
            case 1 -> {
                if(e.getKeyCode() == KeyEvent.VK_W){
                    setYDirection(-speed);
                }
                if(e.getKeyCode() == KeyEvent.VK_S){
                    setYDirection(speed);
                }
            }
            case 2 -> {
                if(e.getKeyCode() == KeyEvent.VK_UP){
                    setYDirection(-speed);
                }
                if(e.getKeyCode() == KeyEvent.VK_DOWN){
                    setYDirection(speed);
                }
            }
        }
    }

    public void keyReleased(KeyEvent e){
        switch (id){
            case 1 -> {
                if(e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_S){
                    setYDirection(0);
                }
            }
            case 2 -> {
                if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN){
                    setYDirection(0);
                }
            }
        }
    }

    public void setYDirection(int yDirection){
        yVelocity = yDirection;
    }

    public void move(){
        y += yVelocity;
    }

    public void draw(Graphics g){
        if(id == 1){
            g.setColor(Color.BLUE);
        } else {
            g.setColor(Color.GREEN);
        }
        g.fillRect(x, y, width, height);
    }
}
