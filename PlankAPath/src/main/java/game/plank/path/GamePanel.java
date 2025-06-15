package game.plank.path;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import game.plank.path.entity.Cell;
import game.plank.path.entity.HUD;
import game.plank.path.entity.Plank;
import game.plank.path.entity.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static game.plank.path.util.Constants.*;

public class GamePanel extends JPanel implements Runnable{
    private final Thread gameThread;
    private Image image;
    private Graphics graphics;
    private Map<Integer, Cell> cells = new HashMap<>();
    private Map<Integer, Plank> planks = new HashMap<>();
    private int [][] levelLayout = new int[MAX_ROWS][MAX_COLUMNS];
    private Plank pickedUpPlank;
    private int level_group = 0;
    private int level_number = 0;
    private Player player;
    private HUD hud = new HUD(0, 1, 1);
    private int previousCellId;
    private int previousPlankId;
    private int nextCellId;
    private boolean keyPressAllowed = true;
    private boolean foodConsumedForLevel = false;

    GamePanel() throws IOException {
        loadNextLevel();
//        printAllDetails();
        this.addKeyListener(new ActionListener());
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
                hud.updateTime();
                move();
                try {
                    checkCollision();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                repaint();
                delta--;
            }
        }
    }

    public void paint(Graphics g){
        super.paint(g);
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
        if(Objects.nonNull(pickedUpPlank)){
            pickedUpPlank.draw(g);
        }
        player.draw(g);
        hud.draw(g);
    }

    public void move(){
        player.move();
    }

    public void checkCollision() throws IOException {
        if(nextCellId != -1) {
            Cell currentCell = cells.get(nextCellId);
            if (playerInsideCell(currentCell)) {
                player.stop();
                int new_x_coord = getNewXCoords(player.direction, player.x_coord, player.y_coord, nextCellId);
                int new_y_coord = getNewYCoords(player.direction, player.x_coord, player.y_coord, nextCellId);
                player = computePlayer(nextCellId, new_x_coord, new_y_coord);
                reconcilePlank(previousPlankId);
                reconcileCell(previousCellId, player.direction);
                checkAndRemovePlank((player.direction + 2) % 4, currentCell);
                checkAndEatFood(currentCell);
                checkAndUpdateLevel(currentCell.location, currentCell.type);
                previousCellId = -1;
                previousPlankId = -1;
                nextCellId = -1;
                keyPressAllowed = true;
            }
        }
    }

    public void initLevelMap(){
        for(int i = 0; i<MAX_ROWS; i++){
            for(int j = 0; j<MAX_COLUMNS; j++){
                this.levelLayout[i][j] = -1;
            }
        }
    }

    public void loadNextLevel() throws IOException {
        level_number = (level_number % 10) + 1;
        if(level_number == 1){
            level_group = (level_group % 10) + 1;
        }
        loadLevel();
    }

    public void loadLevel() throws IOException {
        initLevelMap();
        previousPlankId = -1;
        previousCellId = -1;
        nextCellId = -1;
        keyPressAllowed = true;
        foodConsumedForLevel = false;
        cells.clear();
        planks.clear();
        player = null;
        hud = new HUD(hud.foodCount, level_group, level_number);
        String filePath = String.join("", "/Level_", String.valueOf(level_group), "_", String.valueOf(level_number), ".json");
        ObjectMapper mapper = new ObjectMapper();
        InputStream inputStream = GamePanel.class.getResourceAsStream(filePath);
        Map<String, Object> levelMap = mapper.readValue(inputStream, new TypeReference<>() {});
        ArrayList<Map<String, Object>> cellsArray = (ArrayList<Map<String, Object>>) levelMap.get("cells");
        int startCellId = 0;
        int x_coord = 0;
        int y_coord = 0;
        for(int i = 0; i < cellsArray.size(); i++){
            this.cells.put((Integer) cellsArray.get(i).get("id"), computeCell(cellsArray.get(i)));
            if((int) cellsArray.get(i).get("location") == 0){
                startCellId = (int) cellsArray.get(i).get("id");
                x_coord = (int) cellsArray.get(i).get("x");
                y_coord = (int) cellsArray.get(i).get("y");
            }
        }
        ArrayList<Map<String, Object>> planksArray = (ArrayList<Map<String, Object>>) levelMap.get("planks");
        for(int i = 0; i < planksArray.size(); i++){
            this.planks.put((Integer) planksArray.get(i).get("id"), computePlank(planksArray.get(i)));
        }
        pickedUpPlank = null;
        player = computePlayer(startCellId, x_coord, y_coord);
        inputStream.close();
    }

    private Player computePlayer(int startCellId, int x_coord, int y_coord){
        Cell startCell = cells.get(startCellId);
        int x = startCell.x;
        int y = startCell.y;
        int direction = 3;
        if(Objects.nonNull(player)){
            direction = player.direction;
        }
        return new Player(x + ((TOP_CELL_WIDTH - PLAYER_SIZE) / 2), y + ((TOP_CELL_WIDTH - PLAYER_SIZE) / 2), PLAYER_SIZE, direction, startCellId, x_coord, y_coord);
    }

    private Cell computeCell(Map<String, Object> cellJson){
        int id = (int) cellJson.get("id");
        int location = (int) cellJson.get("location");
        int type = (int) cellJson.get("type");
        boolean hasFood = (boolean) cellJson.get("hasFood");
        ArrayList<Boolean> entry = (ArrayList<Boolean>) cellJson.get("entry");
        int x_coord = (int) cellJson.get("x");
        int y_coord = (int) cellJson.get("y");
        levelLayout[y_coord][x_coord] = id;
        int x = (MINIMUM_CELL_GAP/2) + (x_coord * (TOP_CELL_WIDTH + MINIMUM_CELL_GAP));
        int y = (GAME_HEIGHT - (MINIMUM_CELL_GAP/2) - ((MAX_ROWS - 1 - y_coord) * MINIMUM_CELL_GAP) - ((MAX_ROWS - y_coord) * TOP_CELL_WIDTH));
        return new Cell(x, y, TOP_CELL_WIDTH, id, type, location, entry, hasFood);
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
        blockPath(cell1_coord.get(1), cell1_coord.get(0), cell2_coord.get(1), cell2_coord.get(0));
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

    private void reconcileCell(int cellId, int direction){
        Cell cell = cells.get(cellId);
        if(cell.type == 1){
            cell.type = 0;
        }
        checkAndRemovePlank(direction, cell);
    }

    private void checkAndUpdateLevel(int location, int type) throws IOException {
        if(location == 1){
            loadNextLevel();
        }
        if(type == 0){
            loadLevel();
        }
    }

    private void checkAndEatFood(Cell cell){
        if(cell.hasFood){
            cell.hasFood = false;
            foodConsumedForLevel = true;
            hud.addFoodCount();
        }
    }

    private void checkAndRemovePlank(int direction, Cell cell) {
        if(!planks.containsKey(cell.plankId.get(direction))){
            cell.plankId.set(direction, -1);
        }
    }

    public void reconcilePlank(int plankId){
        if(planks.get(plankId).type == 1){
            planks.remove(plankId);
        }
    }

    private boolean playerInsideCell(Cell cell){
        if((player.x > cell.x) && ((player.x + PLAYER_SIZE) < (cell.x + TOP_CELL_WIDTH))){
            if((player.y > cell.y) && ((player.y + PLAYER_SIZE) < (cell.y + TOP_CELL_WIDTH))){
                return true;
            }
        }
        return false;
    }

    private int getNewXCoords(int direction, int col, int row, int cellId){
        if(direction == 1){
            for (int i = col - 1; i >= 0; i--) {
                if (levelLayout[row][i] == cellId) {
                    return i;
                }
            }
        } else if (direction == 3){
            for (int i = col + 1; i < MAX_COLUMNS; i++) {
                if (levelLayout[row][i] == cellId) {
                    return i;
                }
            }
        }
        return col;
    }

    private int getNewYCoords(int direction, int col, int row, int cellId){
        if(direction == 0){
            for(int i = row - 1; i >= 0; i--){
                if(levelLayout[i][col] == cellId){
                    return i;
                }
            }
        } else if (direction == 2){
            for(int i = row + 1; i < MAX_ROWS; i++){
                if(levelLayout[i][col] == cellId){
                    return i;
                }
            }
        }
        return row;
    }

    public int getCellId2(int direction, int col, int row){
        if(direction == 0){
            for(int i = row - 1; i >= 0; i--){
                if(levelLayout[i][col] > -1){
                    return levelLayout[i][col];
                }
            }
        } else if(direction == 1) {
            for (int i = col - 1; i >= 0; i--) {
                if (levelLayout[row][i] > -1) {
                    return levelLayout[row][i];
                }
            }
        } else if(direction == 2){
            for(int i = row + 1; i < MAX_ROWS; i++){
                if(levelLayout[i][col] > -1){
                    return levelLayout[i][col];
                }
            }
        } else if(direction == 3) {
            for (int i = col + 1; i < MAX_COLUMNS; i++) {
                if (levelLayout[row][i] > -1) {
                    return levelLayout[row][i];
                }
            }
        }
        return -1;
    }

    public int getDistanceBetweenCells(int direction, int col, int row){
        if(direction == 0){
            for(int i = row - 1; i >= 0; i--){
                if(levelLayout[i][col] != -1){
                    return row - i;
                }
            }
        } else if(direction == 1) {
            for (int i = col - 1; i >= 0; i--) {
                if (levelLayout[row][i] != -1) {
                    return col - i;
                }
            }
        } else if(direction == 2){
            for(int i = row + 1; i < MAX_ROWS; i++){
                if(levelLayout[i][col] != -1){
                    return i - row;
                }
            }
        } else if(direction == 3) {
            for (int i = col + 1; i < MAX_COLUMNS; i++) {
                if (levelLayout[row][i] != -1) {
                    return i - col;
                }
            }
        }
        return -1;
    }

    public boolean noPlankBlocking(int x1, int y1, int x2, int y2){
        if(x1 == x2){
            for(int i = Math.min(y1, y2) + 1; i < Math.max(y1, y2); i++){
                if(levelLayout[i][x1] == -2){
                    return false;
                }
            }
        } else {
            for(int i = Math.min(x1, x2) + 1; i < Math.max(x1, x2); i++){
                if(levelLayout[y1][i] == -2){
                    return false;
                }
            }
        }
        return true;
    }

    public void blockPath(int x1, int y1, int x2, int y2){
        if(x1 == x2){
            for(int i = Math.min(y1, y2) + 1; i < Math.max(y1, y2); i++){
                levelLayout[i][x1] = -2;
            }
        } else {
            for(int i = Math.min(x1, x2) + 1; i < Math.max(x1, x2); i++){
                levelLayout[y1][i] = -2;
            }
        }
    }

    public void unblockPath(int x1, int y1, int x2, int y2){
        if(x1 == x2){
            for(int i = Math.min(y1, y2) + 1; i < Math.max(y1, y2); i++){
                levelLayout[i][x1] = -1;
            }
        } else {
            for(int i = Math.min(x1, x2) + 1; i < Math.max(x1, x2); i++){
                levelLayout[y1][i] = -1;
            }
        }
    }

    public void pickUpPlank(int cellId1, int cellId2, int plankId, int direction){
        pickedUpPlank = planks.remove(plankId);
        pickedUpPlank.x = PICKED_UP_PLANK_X;
        pickedUpPlank.y = PICKED_UP_PLANK_Y;
        if(pickedUpPlank.orientation != 0){
            pickedUpPlank.orientation = 0;
            int temp = pickedUpPlank.width;
            pickedUpPlank.width = pickedUpPlank.height;
            pickedUpPlank.height = temp;
        }
        Cell cell1 = cells.get(cellId1);
        cell1.plankId.set(direction, -1);
        Cell cell2 = cells.get(cellId2);
        cell2.plankId.set((direction+2)%4, -1);
        System.out.println("All plank ids : " + planks.keySet());
    }

    public void putDownPlank(int cellId1, int cellId2, int direction){
        Plank plank = pickedUpPlank;
        pickedUpPlank = null;
        plank.orientation = 1 - (direction % 2);
        if(plank.orientation == 1){
            int temp = plank.width;
            plank.width = plank.height;
            plank.height = temp;
        }
        Cell cell1 = cells.get(cellId1);
        Cell cell2 = cells.get(cellId2);

        addPlankToCells(cell1, cell2, plank.id);
        plank.x = computePlankX(Math.min(cell1.x, cell2.x), plank.orientation);
        plank.y = computePlankY(Math.min(cell1.y, cell2.y), plank.orientation);
        System.out.println("Plank at location : x " + plank.x + " y " + plank.y);
        planks.put(plank.id, plank);
        System.out.println("All plank ids : " + planks.keySet());
    }

    public void plankKeyPressed(){
        int direction = player.direction;
        int cellId1 = player.cellId;
        int cellId2 = getCellId2(direction, player.x_coord, player.y_coord);
        int cell1_x_coord = player.x_coord;
        int cell1_y_coord = player.y_coord;
        int cell2_x_coord = getNewXCoords(direction, cell1_x_coord, cell1_y_coord, cellId2);
        int cell2_y_coord = getNewYCoords(direction, cell1_x_coord, cell1_y_coord, cellId2);
        int distance = getDistanceBetweenCells(direction, player.x_coord, player.y_coord);
        System.out.println("direction : " + direction + " cellId1 " + cellId1 + " cellId2 " + cellId2 + " distance " + distance);
        if(cellId2 != -1 && distance > 0) {
            if (Objects.isNull(pickedUpPlank)) {
                int plankId = cells.get(cellId1).plankId.get(direction);
                System.out.println("picking up plank : " + plankId);
                if (plankId != -1 && !planks.get(plankId).isFixed) {
                    unblockPath(cell1_x_coord, cell1_y_coord, cell2_x_coord, cell2_y_coord);
                    pickUpPlank(cellId1, cellId2, plankId, direction);
                }
            } else {
                if(distance == pickedUpPlank.length && cells.get(cellId1).plankId.get(direction) == -1 && noPlankBlocking(cell1_x_coord, cell1_y_coord, cell2_x_coord, cell2_y_coord)){
                    System.out.println("Putting down plank : " + pickedUpPlank.id);
                    blockPath(cell1_x_coord, cell1_y_coord, cell2_x_coord, cell2_y_coord);
                    putDownPlank(cellId1, cellId2, direction);
                }
            }
        }
    }

    public void movePlayer(int direction){
        Cell cell1 = cells.get(player.cellId);
        int cell2Id = getCellId2(direction, player.x_coord, player.y_coord);
        if(cell1.plankId.get(direction) != -1){
            player.setVelocity(direction);
            previousCellId = player.cellId;
            previousPlankId = cell1.plankId.get(direction);
            nextCellId = cell2Id;
            keyPressAllowed = false;
            hud.addMoveCount();
        }
    }

    public void playerKeyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_UP){
            if(player.direction == 0){
                movePlayer(player.direction);
            } else {
                player.direction = 0;
            }
        } else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (player.direction == 1) {
                movePlayer(player.direction);
            } else {
                player.direction = 1;
            }
        } else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (player.direction == 2) {
                movePlayer(player.direction);
            } else {
                player.direction = 2;
            }
        } else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (player.direction == 3) {
                movePlayer(player.direction);
            } else {
                player.direction = 3;
            }
        }
    }



    public class ActionListener extends KeyAdapter{
        public void keyPressed(KeyEvent e) {
            if (keyPressAllowed) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    plankKeyPressed();
                } else if(e.getKeyCode() == KeyEvent.VK_R){
                    try {
                        if(foodConsumedForLevel){
                            hud.foodCount--;
                        }
                        loadLevel();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } else if(e.getKeyCode() == KeyEvent.VK_G){
                    level_group = 0;
                    level_number = 0;
                    hud.foodCount = 0;
                    try {
                        loadNextLevel();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }else {
                    playerKeyPressed(e);
                }
                printAllDetails();
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
        System.out.println("----------------------------------------------------------");
        System.out.println("----------------------------------------------------------");
        System.out.println("x : " + player.x + "y : " + player.y + "width : " + player.width + "height : " + player.height + "direction : " + player.direction + "cellId : " + player.cellId + "x_coord : " + player.x_coord + "y_coord : " + player.y_coord);
    }
}
