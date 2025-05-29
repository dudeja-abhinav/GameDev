package game.jumpy.egg;

import game.jumpy.egg.entity.Basket;
import game.jumpy.egg.entity.Egg;
import game.jumpy.egg.entity.Score;

import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import static game.jumpy.egg.util.Constants.*;

public class GamePanel extends JPanel implements Runnable{

    private final Thread gameThread;
    private Image image;
    private Graphics graphics;
    private Egg egg;
    private ArrayList<Basket> baskets = new ArrayList<>();
    private Score score;
    private Random random = new Random(System.currentTimeMillis());
    private static boolean eggJumped = false;

    public GamePanel(){
        newEgg();
        newBaskets();
        newScore();
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
                if(eggJumped){
                    applyGravity();
                }
                if(baskets.get(0).y < 0){
                    resetGame();
                }
                move();
                checkCollision();
                repaint();
                delta --;
            }
        }
    }

    public void newScore() {
        score = new Score();
    }

    public void newEgg() {
        egg = new Egg((GAME_WIDTH - EGG_DIAMETER)/2, (GAME_HEIGHT - (5*EGG_DIAMETER)), EGG_DIAMETER);
    }

    public void newBaskets(){
        for (int y = (GAME_HEIGHT - 4*BASKET_HEIGHT); y >= -(5*BASKET_HEIGHT); y -= 5*BASKET_HEIGHT){
            if(y == (GAME_HEIGHT - 4*BASKET_HEIGHT)){
                baskets.add(newBasket((GAME_WIDTH - BASKET_WIDTH)/2, y, null));
            } else {
                baskets.add(newBasket(random.nextInt(0, (GAME_WIDTH - BASKET_WIDTH + 1)), y, baskets.get(baskets.size()-1)));
            }
        }
    }

    public Basket newBasket(final int x, final int y, final Basket previousBasket) {
        int velocity = 0;
        if(previousBasket == null){
            velocity = 0;
        }
        else if(!previousBasket.moves){
           velocity = random.nextInt(1, MAX_BASKET_SPEED);
        } else {
            if(random.nextInt(2) % 2 == 0){
                velocity = 0;
            }
            else{
                velocity = random.nextInt(1, MAX_BASKET_SPEED);
            }
        }
        return new Basket(x, y, BASKET_WIDTH, BASKET_HEIGHT, velocity>0, velocity);
    }

    public void resetGame(){
        eggJumped = false;
        baskets.clear();
        newEgg();
        newBaskets();
        newScore();
    }

    public void move() {
        for(Basket basket : baskets){
            basket.move();
        }
        egg.move();
    }

    public void checkCollision(){
        //Change basket direction if it hits an edge
        for(Basket basket : baskets){
            if(basket.x <= 0 || basket.x >= (GAME_WIDTH - BASKET_WIDTH)){
                basket.setXVelocity(-1 * basket.xVelocity);
                if(basket.intersects(egg)){
                    egg.setXVelocity(-1 * egg.xVelocity);
                }
            }
        }

        //Check Egg land on basket
        Basket nextBasket = baskets.get(1);
        if(nextBasket.yVelocity < 0 && egg.intersects(nextBasket) && (egg.x > nextBasket.x && egg.x+EGG_DIAMETER < nextBasket.x+BASKET_WIDTH)){
            stopBaskets();
            eggJumped = false;
            baskets.remove(baskets.get(0));
            egg.setXVelocity(nextBasket.xVelocity);
            score.addPoint();
        }
    }

    public void stopBaskets(){
        for(Basket basket : baskets){
            basket.setYVelocity(0);
        }
    }

    public void eggJump(){
        eggJumped = true;
        egg.setXVelocity(0);
        Basket lastBasket = baskets.get(baskets.size()-1);
        baskets.add(newBasket(random.nextInt(0, (GAME_WIDTH - BASKET_WIDTH + 1)), lastBasket.y - 5*BASKET_HEIGHT, lastBasket));
        for(Basket basket : baskets){
            basket.setYVelocity(EGG_JUMP_VELOCITY);
        }
    }

    public void applyGravity(){
        for(Basket basket : baskets){
            basket.setYVelocity(basket.yVelocity - GRAVITY);
        }
    }

    public void paint(Graphics g){
        image = createImage(getWidth(), getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image, 0, 0, this);
    }

    public void draw(Graphics g){
        egg.draw(g);
        for(Basket basket : baskets){
            basket.draw(g);
        }
        score.draw(g);
    }

    public class ActionListener extends KeyAdapter{
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_SPACE){
                eggJump();
            }
        }

    }
}
