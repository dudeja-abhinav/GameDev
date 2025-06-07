package game.wordle;

import javax.swing.*;

import java.awt.*;

import static game.wordle.util.Constants.SCREEN_SIZE;

public class GamePanel extends JPanel implements Runnable {

    private final Thread gameThread;
    private Image image;
    private Graphics graphics;

    public GamePanel(){
        this.setFocusable(true);
        this.setPreferredSize(SCREEN_SIZE);
        gameThread = new Thread(this);
        gameThread.start();
    }
    @Override
    public void run() {

    }

    public void paint(Graphics g){
        image = createImage(getWidth(), getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image, 0, 0, this);
    }

    public void draw(Graphics g){

    }

}
