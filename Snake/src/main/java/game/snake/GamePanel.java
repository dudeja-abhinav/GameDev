package game.snake;

import game.snake.entity.Food;
import game.snake.entity.Score;
import game.snake.entity.Snake;
import game.snake.entity.SnakeSegment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.security.Key;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

import static game.snake.util.Constants.*;

public class GamePanel extends JPanel implements Runnable {
    private final Thread gameThread;
    private Image image;
    private Graphics graphics;
    private Snake snake;
    private Food food;
    private Random random = new Random(System.currentTimeMillis());
    private Score score;
    private final ArrayBlockingQueue<KeyEvent> directionInputs = new ArrayBlockingQueue<>(2);
    private int lastCheckpoint = 0;
    private int lastScore = 0;
    public GamePanel(){
        newSnake();
        newFood();
        newScore();
        this.setFocusable(true);
        this.setPreferredSize(SCREEN_SIZE);
        this.addKeyListener(new ActionListener());
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
                move();
                checkCollision();
                repaint();
                delta--;
            }
        }
    }

    public void checkCollision() {
        //Check if snake ate the food
        if(snake.getSnakeHead().intersects(food)){
            score.addPoints(food.points);
            snake.addSnakeSegment();
        }

        //logic to check if the snake collides with itself or the walls
        snake.checkCollisions();

        //logic to check if the snake is at a valid position to process direction inputs
        if(snake.getSnakeHead().x % SNAKE_WIDTH == 0 && snake.getSnakeHead().y % SNAKE_HEIGHT == 0){
            //Check for direction inputs
            if(!directionInputs.isEmpty()){
                KeyEvent keyEvent = directionInputs.poll();
                if(keyEvent != null){
                    snake.keyPressed(keyEvent);
                }
            }
            if(score.points > 0 && score.points != lastScore){
                newFood();
                lastScore = score.points;
                if(score.points % 10 == 0 && score.points != lastCheckpoint){
                    // Increase speed
                    lastCheckpoint = score.points;
                    snake.increaseSpeed();
                }
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
        if(snake.getSnakeHead().xVelocity == 0 && snake.getSnakeHead().yVelocity == 0){
            score.draw(g);
        } else {
            snake.draw(g);
            food.draw(g);
        }
    }

    public void move(){
        snake.move();
    }

    private void newSnake(){
        snake = new Snake(random.nextInt(GAME_WIDTH/SNAKE_WIDTH) * 20, random.nextInt(GAME_HEIGHT/SNAKE_HEIGHT) * 20, SNAKE_WIDTH, SNAKE_HEIGHT, SNAKE_LENGTH, SNAKE_SPEED);
    }

    private void newFood(){
        //Generate new food at a random position withing the game area
        ArrayList<Point> snakePoints = new ArrayList<>();
        for(SnakeSegment snakeSegment : snake.getSnakeSegments()){
            snakePoints.add(new Point(snakeSegment.x, snakeSegment.y));
        }
        ArrayList<Point> availablePoints = new ArrayList<>();
        for(int i = 0; i < GAME_WIDTH; i+=FOOD_DIAMETER){
            for(int j = 0; j < GAME_HEIGHT; j+= FOOD_DIAMETER){
                Point currentPoint = new Point(i, j);
                if(snakePoints.contains(currentPoint)){
                    continue;
                }
                availablePoints.add(currentPoint);
            }
        }
        Point spawnPoint = availablePoints.get(random.nextInt(availablePoints.size()));
        food = new Food(spawnPoint.x, spawnPoint.y, FOOD_DIAMETER);
    }

    private void newScore() {
        score = new Score();
    }

    public void resetGame() {
        directionInputs.clear();
        newSnake();
        newFood();
        newScore();
    }

    public class ActionListener extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            if(snake.getSnakeHead().xVelocity == 0 && snake.getSnakeHead().yVelocity == 0) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    //Reset the game
                    resetGame();
                }
            } else {
                directionInputs.offer(e);
            }
            if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                System.exit(0);
            }
            if(e.getKeyCode() == KeyEvent.VK_R){
                resetGame();
            }
        }
    }
}
