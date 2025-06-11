package game.plank.path;

import game.plank.path.entity.Cell;
import game.plank.path.entity.Plank;
import game.plank.path.entity.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static game.plank.path.util.Constants.SCREEN_SIZE;

public class GamePanel extends JPanel implements Runnable{
    private final Thread gameThread;
    private Image image;
    private Graphics graphics;
    private Cell [][] cells;
    private ArrayList<Plank> planks = new ArrayList<>();
    private Plank pickedUpPlank;

    GamePanel(){
        this.setFocusable(true);
        this.setPreferredSize(SCREEN_SIZE);
        System.out.println(SCREEN_SIZE);
        gameThread = new Thread(this);
        gameThread.start();
    }
    @Override
    public void run() {
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
                delta--;
            }
        }
    }

    public void paint(Graphics g){
        image = createImage(getWidth(), getHeight());
        graphics = image.getGraphics();
        draw(g);
        graphics.drawImage(image, 0, 0, this);
    }
    public void draw(Graphics g){

    }
}
