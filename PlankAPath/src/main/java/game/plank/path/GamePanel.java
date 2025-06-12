package game.plank.path;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import game.plank.path.entity.Cell;
import game.plank.path.entity.Plank;
import game.plank.path.entity.Player;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static game.plank.path.util.Constants.*;

public class GamePanel extends JPanel implements Runnable{
    private final Thread gameThread;
    private Image image;
    private Graphics graphics;
    private Map<Integer, Cell> cells = new HashMap<>();
    private Map<Integer, Plank> planks = new HashMap<>();
    private int [][] levelLayout = new int[MAX_ROWS][MAX_COLUMNS];
    private Plank pickedUpPlank;
    private int level_group = 1;
    private int level_number = 1;

    GamePanel() throws IOException {
        initLevelMap();
        loadLevel();
//        printAllDetails();
        this.setFocusable(true);
        this.setPreferredSize(SCREEN_SIZE);
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
        for(Cell cell : cells.values()){
            cell.draw(g);
        }
        for(Plank plank : planks.values()){
            plank.draw(g);
        }
    }

    public void initLevelMap(){
        for(int i = 0; i<MAX_ROWS; i++){
            for(int j = 0; j<MAX_COLUMNS; j++){
                this.levelLayout[i][j] = -1;
            }
        }
    }

    public void loadLevel() throws IOException {
        String filePath = String.join("", "/Level_", String.valueOf(level_group), "_", String.valueOf(level_number), ".json");
        ObjectMapper mapper = new ObjectMapper();
        InputStream inputStream = GamePanel.class.getResourceAsStream(filePath);
        Map<String, Object> levelMap = mapper.readValue(inputStream, new TypeReference<>() {});
        ArrayList<Map<String, Object>> cellsArray = (ArrayList<Map<String, Object>>) levelMap.get("cells");
        for(int i = 0; i < cellsArray.size(); i++){
            this.cells.put((Integer) cellsArray.get(i).get("id"), computeCell(cellsArray.get(i)));
        }
        ArrayList<Map<String, Object>> planksArray = (ArrayList<Map<String, Object>>) levelMap.get("planks");
        for(int i = 0; i < planksArray.size(); i++){
            this.planks.put((Integer) planksArray.get(i).get("id"), computePlank(planksArray.get(i)));
        }
    }

    private Cell computeCell(Map<String, Object> cellJson){
        int id = (int) cellJson.get("id");
        int location = (int) cellJson.get("location");
        int type = (int) cellJson.get("type");
        ArrayList<Boolean> entry = (ArrayList<Boolean>) cellJson.get("entry");
        int x_coord = (int) cellJson.get("x");
        int y_coord = (int) cellJson.get("y");
        levelLayout[y_coord][x_coord] = id;
        int x = (MINIMUM_CELL_GAP/2) + (x_coord * (TOP_CELL_WIDTH + MINIMUM_CELL_GAP));
        int y = (GAME_HEIGHT - (MINIMUM_CELL_GAP/2) - ((MAX_ROWS - 1 - y_coord) * MINIMUM_CELL_GAP) - ((MAX_ROWS - y_coord) * TOP_CELL_WIDTH));
        return new Cell(x, y, TOP_CELL_WIDTH, id, type, location, entry);
    }

    private Plank computePlank(Map<String, Object> plankJson){
        int id = (int) plankJson.get("id");
        boolean isFixed = (boolean) plankJson.get("isFixed");
        int type = (int) plankJson.get("type");
        ArrayList<Integer> cell1_coord = (ArrayList<Integer>) plankJson.get("cell1");
        ArrayList<Integer> cell2_coord = (ArrayList<Integer>) plankJson.get("cell2");
        int length = Math.abs((cell1_coord.get(0) - cell2_coord.get(0)) + (cell1_coord.get(1) - cell2_coord.get(1)));
        int orientation = 0;
        int width = (length * (MINIMUM_CELL_GAP + TOP_CELL_WIDTH)) - TOP_CELL_WIDTH;
        int height = PLANK_WIDHT;
        if(cell1_coord.get(0) != cell2_coord.get(0)){
            orientation = 1;
            int tmp = width;
            width = height;
            height = tmp;
        }
        Cell cell1 = cells.get(levelLayout[cell1_coord.get(0)][cell1_coord.get(1)]);
        Cell cell2 = cells.get(levelLayout[cell2_coord.get(0)][cell2_coord.get(1)]);
        addPlankToCells(cell1, cell2, id);
        int x = computePlankX(Math.min(cell1.x, cell2.x), orientation);
        int y = computePlankY(Math.min(cell1.y, cell2.y), orientation);
        return new Plank(x, y, width, height, length, id, orientation, isFixed, type);
    }

    private int computePlankX(int x, int orientation){
        if(orientation == 1){
            return x;
        }
        return x + TOP_CELL_WIDTH;
    }

    private int computePlankY(int y, int orientation){
        if(orientation == 0){
            return y;
        }
        return y + TOP_CELL_WIDTH;
    }

    private void addPlankToCells(Cell cell1, Cell cell2, int plankId){
        if(cell1.x == cell2.x){
            if(cell1.y < cell2.y){
                cell1.plankId.set(2, plankId);
                cell2.plankId.set(0, plankId);
            } else {
                cell1.plankId.set(0, plankId);
                cell2.plankId.set(2, plankId);
            }
        } else {
            if(cell1.x < cell2.x){
                cell1.plankId.set(3, plankId);
                cell2.plankId.set(1, plankId);
            } else {
                cell1.plankId.set(1, plankId);
                cell2.plankId.set(3, plankId);
            }
        }
    }

    private void printAllDetails(){
        System.out.println("----------------------------------------------------------");
        for(int i = 0; i<MAX_ROWS; i++){
            for(int j = 0; j<MAX_COLUMNS; j++){
                System.out.print(levelLayout[i][j] + " ");
            }
            System.out.println(" ");
        }
        for(Cell cell : cells.values()){
            System.out.println("----------------------------------------------------------");
            System.out.println("x : " + cell.x + "y : " + cell.y + "width : " + cell.width + "location : " + cell.location + "type : " + cell.type + "entry : " + cell.entry + "plankId : " + cell.plankId);
        }
        System.out.println("----------------------------------------------------------");
        for(Plank plank : planks.values()){
            System.out.println("----------------------------------------------------------");
            System.out.println("x : " + plank.x + "y : " + plank.y + "width : " + plank.width + "height : " + plank.height + "type : " + plank.type + "isFixed : " + plank.isFixed + "length : " + plank.length + "orientation : " + plank.orientation);
        }
    }
}
