package game.snake.entity;

import java.awt.*;
import java.util.Random;

import static game.snake.util.Constants.FOOD_DIAMETER;

public class Food extends Rectangle {
    public final int points;

    public Food(int x, int y, int foodDiameter){
        super(x, y, foodDiameter, foodDiameter);
        Random random = new Random(System.currentTimeMillis());
        points = random.nextInt(10) == 0 ? 2 : 1;
    }

    public void draw(Graphics g){
        g.setColor(Color.red);
        if(points == 2){
            g.fillOval(x + FOOD_DIAMETER/2, y + FOOD_DIAMETER/2, width/2, height/2);
        } else {
            g.fillOval(x, y, width, height);
        }
    }
}
