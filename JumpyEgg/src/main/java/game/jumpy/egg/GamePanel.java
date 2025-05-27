package game.jumpy.egg;

import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyAdapter;

import static game.jumpy.egg.util.Constants.SCREEN_SIZE;

public class GamePanel extends JPanel implements Runnable{

    private final Thread gameThread;
    private Image image;
    private Graphics graphics;

    public GamePanel(){
        this.setFocusable(true);
        this.setPreferredSize(SCREEN_SIZE);
        this.addKeyListener(new ActionListener());
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void run() {
        //Game loop logic goes here
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        while(true){
            long now = System.nanoTime();
            delta += (now - lastTime)/ns;
            lastTime = now;
            if(delta >= 1){
                repaint();
                delta --;
            }
        }
    }

    public void paint(Graphics g){
        image = createImage(getWidth(), getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image, 0, 0, this);
    }

    public void draw(Graphics g){

    }

    public class ActionListener extends KeyAdapter{

    }
}
