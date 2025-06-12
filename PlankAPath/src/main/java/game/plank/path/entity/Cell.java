package game.plank.path.entity;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Collections;

import static game.plank.path.util.Constants.BOTTOM_CELL_WIDTH;

public class Cell extends Rectangle {
    public int id;
    //-1 - Middle, 0 - Start, 1 - Finish
    public int location;
    //-1 - Normal, 0 - Death, 1 - Once
    public int type;
    //Up, Left, Down, Right
    public ArrayList<Integer> plankId;
    //Up, Left, Down, Right
    public ArrayList<Boolean> entry;

    public Cell(int x, int y, int width, int id, int type, int location, ArrayList entry){
        super(x, y, width, width);
        this.id = id;
        this.location = location;
        this.type = type;
        this.entry = entry;
        this.plankId = new ArrayList<>(Collections.nCopies(4, -1));
    }

    public void draw(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        // Enable anti-aliasing for smoother curves
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color color = Color.BLACK;
        g2d.setColor(color);
        RoundRectangle2D roundedStep = new RoundRectangle2D.Double(x-5, y-5, BOTTOM_CELL_WIDTH, BOTTOM_CELL_WIDTH, 15, 15);
        g2d.fill(roundedStep);
        color = Color.decode("#555555");
        g2d.setColor(color);
        RoundRectangle2D roundedSquare = new RoundRectangle2D.Double(x, y, width, height, 10, 10);
        g2d.fill(roundedSquare);
        switch (this.type) {
            case 0:
                color = Color.RED;
                break;
            case 1:
                color = Color.YELLOW;
                break;
            default:
                color = Color.GREEN;
        }
        if(this.location == 1){
            color = Color.BLUE;
        }
        g2d.setColor(color);
        RoundRectangle2D roundedLining = new RoundRectangle2D.Double(x+5, y+5, width-10, height-10, 5, 5);
        g2d.draw(roundedLining);
    }
}
