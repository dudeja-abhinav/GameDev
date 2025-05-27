package game.flappy.bird;

import game.flappy.bird.entity.Bird;
import game.flappy.bird.entity.GameMenu;
import game.flappy.bird.entity.Pipe;
import game.flappy.bird.entity.Score;

import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import static game.flappy.bird.util.Constants.*;

public class GamePanel extends JPanel implements Runnable {

    private final Thread gameThread;
    private Image image;
    private Graphics graphics;
    private Bird bird;
    private Score score;
    private GameMenu gameMenu;
    private boolean gameOver;
    private boolean gameStart;
    private Color birdColor;
    private ArrayList<Obstacle> obstacles = new ArrayList<>();
    private int framesToNextObstacle = FRAMES_PER_OBSTACLE;
    private Random random = new Random(System.currentTimeMillis());

    public GamePanel() {
        gameOver = false;
        gameStart = true;
        newMenu();
        this.setFocusable(true);
        this.setPreferredSize(SCREEN_SIZE);
        this.addKeyListener(new ActionListener());
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
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
                if(!gameOver && !gameStart){
                    handleObstacles();
                    applyGravity();
                    move();
                    checkCollision();
                }
                repaint();
                delta --;
            }
        }
    }

    public void newBird(){
        this.bird = new Bird(((GAME_WIDTH/2) - (BIRD_DIAMETER/2)), ((GAME_HEIGHT/2) - (BIRD_DIAMETER/2)), BIRD_DIAMETER, birdColor);
    }

    public void newObstacle(){
        obstacles.add(new Obstacle(GAME_WIDTH+PIPE_WIDTH, random.nextInt(GAME_HEIGHT-GAP_HEIGHT), PIPE_WIDTH, GAP_HEIGHT, PIPE_SPEED));
    }

    public void newMenu(){
        gameMenu = new GameMenu();
    }

    public void newScore() {
        score = new Score();
    }

    public void move(){
        bird.move();
        for(Obstacle obstacle : obstacles){
            obstacle.move();
        }
    }
    public void applyGravity(){
        bird.setYVelocity(bird.yVelocity + GRAVITY);
    }

    public void handleObstacles() {
        if(obstacles.size() > 0 && obstacles.get(0).gap.x < - PIPE_WIDTH){
            obstacles.remove(obstacles.get(0));
        }
        if(--framesToNextObstacle == 0){
            newObstacle();
            framesToNextObstacle = FRAMES_PER_OBSTACLE;
        }
    }

    public void checkCollision(){
        //Checks if bird went out of game window
        if(bird.y < 0 || bird.y > (GAME_HEIGHT - BIRD_DIAMETER)){
            bird.yVelocity = 0;
            gameOver = true;
        }

        //Checks if bird went through a gap and adds point
        for(Obstacle obstacle : obstacles){
            for(Pipe pipe : obstacle.pipes){
                if(pipe.intersects(bird)){
                    gameOver = true;
                }
            }
            if(!obstacle.isCrossed){
                if(obstacle.gap.intersects(bird)){
                    score.addPoints(POINTS_PER_GAP);
                    obstacle.markCrossed();
                }
            }
        }
    }

    public void resetGame(){
        obstacles.clear();
        framesToNextObstacle = FRAMES_PER_OBSTACLE;
        newBird();
        newObstacle();
        newScore();
        gameOver = false;
        gameStart = false;
    }

    public void paint(Graphics g){
        // Drawing logic goes here
        image = createImage(getWidth(), getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image, 0, 0, this);
    }

    public void draw(Graphics g){
        if(gameStart){
            gameMenu.draw(g);
        }
        else if(gameOver){
            score.draw(g);
        }
        else{
            for(Obstacle obstacle : obstacles){
                obstacle.draw(g);
            }
            bird.draw(g);
        }
    }

    public class ActionListener extends KeyAdapter{
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                System.exit(0);
            }
            if(gameStart || gameOver){
                switch (e.getKeyCode()){
                    case KeyEvent.VK_R -> {
                        birdColor = Color.RED;
                        resetGame();
                    }
                    case KeyEvent.VK_Y -> {
                        birdColor = Color.YELLOW;
                        resetGame();
                    }
                    case KeyEvent.VK_B -> {
                        birdColor = Color.BLUE;
                        resetGame();
                    }
                }
                if(gameOver){
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        resetGame();
                    }
                }
            }
            else{
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    bird.keyPressed();
                }
            }
        }
    }
}
