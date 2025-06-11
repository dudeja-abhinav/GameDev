package game.plank.path.entity;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

import static game.plank.path.util.Constants.BOTTOM_CELL_WIDTH;

public class Cell extends Rectangle {
    //-1 - Middle, 0 - Start, 1 - Finish
    public int type;
    //-1 - Normal, 0 - Death, 1 - Once
    public int status;
    //Up, Left, Down, Right
    public int [] plankId;
    //Up, Left, Down, Right
    public boolean [] entry;

    public Cell(int x, int y, int width, int status, int type, boolean [] entry){
        super(x, y, width, width);
        this.type = type;
        this.status = status;
        this.entry = entry;
        this.plankId = new int[]{-1, -1, -1, -1};
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
        switch (this.status) {
            case 0:
                color = Color.YELLOW;
                break;
            case 1:
                color = Color.RED;
                break;
            default:
                color = Color.GREEN;
        }
        if(this.type == 1){
            color = Color.BLUE;
        }
        g2d.setColor(color);
        RoundRectangle2D roundedLining = new RoundRectangle2D.Double(x+5, y+5, width-10, height-10, 5, 5);
        g2d.draw(roundedLining);
    }
}
