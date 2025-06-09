package game.plank.path;

import javax.swing.*;
import java.awt.*;

import static game.plank.path.util.Constants.SCREEN_SIZE;

public class GamePanel extends JPanel implements Runnable{
    private final Thread gameThread;
    private Image image;
    private Graphics graphics;
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
//        g.setColor(Color.GREEN);
//        g.fillRect(35, 166, 50, 50);
//        g.setColor(Color.RED);
//        g.fillRect(40, 171, 40, 40);
//        g.setColor(Color.GREEN);
//        g.fillRect(145, 166, 50, 50);
//        g.setColor(Color.RED);
//        g.fillRect(150, 171, 40, 40);
//        g.setColor(Color.DARK_GRAY);
//        g.fillRect(80, 166, 70, 50);
    }
}
