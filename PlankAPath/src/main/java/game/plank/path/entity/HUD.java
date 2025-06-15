package game.plank.path.entity;

import java.awt.*;

import static game.plank.path.util.Constants.*;

public class HUD extends Rectangle {
    public int foodCount;
    public int levelTime;
    public int levelGroup;
    public int levelNumber;
    public long previousTime;
    public int moveCount;

    public HUD(int foodCount, int levelGroup, int levelNumber){
        this.foodCount = foodCount;
        this.levelTime = 0;
        this.moveCount = 0;
        this.previousTime = System.currentTimeMillis();
        this.levelGroup = levelGroup;
        this.levelNumber = levelNumber;
    }

    public void updateTime(){
        if(System.currentTimeMillis() - previousTime >= 1000){
            previousTime = System.currentTimeMillis();
            levelTime++;
        }
    }

    public void addMoveCount(){
        moveCount++;
    }

    public void addFoodCount(){
        foodCount++;
    }

    public void draw(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Consolas", Font.PLAIN, 25));
        g2d.drawString(PICKED_UP_PLANK_STRING, MINIMUM_CELL_GAP/2, MINIMUM_CELL_GAP/2);
        g2d.drawString("Level : " + levelGroup + "-" + levelNumber, GAME_WIDTH/2 - 2 * MINIMUM_CELL_GAP, MINIMUM_CELL_GAP/2);
        g2d.drawString("Time : " + levelTime, GAME_WIDTH/2 + MINIMUM_CELL_GAP, MINIMUM_CELL_GAP/2);
        g2d.drawString("Moves : " + moveCount, GAME_WIDTH/2 + 4 * MINIMUM_CELL_GAP, MINIMUM_CELL_GAP/2);
        g2d.drawString(foodCount / 10 + String.valueOf(foodCount%10), GAME_WIDTH - MINIMUM_CELL_GAP/2, MINIMUM_CELL_GAP/2);
        g2d.setColor(Color.decode("#00B0F0"));
        g2d.fillOval(GAME_WIDTH - MINIMUM_CELL_GAP, MINIMUM_CELL_GAP/5, FOOD_SIZE, FOOD_SIZE);

    }
}
