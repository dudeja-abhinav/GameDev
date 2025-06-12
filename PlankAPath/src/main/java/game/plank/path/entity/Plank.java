package game.plank.path.entity;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class Plank extends Rectangle {
    public int id;
    public int length;
    //0 - horizontal, 1 - vertical
    public int orientation;
    //0 - not fixed, 1 - fixed
    public boolean isFixed;
    //-1 - Normal, 1 - Once
    public int type;

    public Plank(int x, int y, int width, int height, int length, int id, int orientation, boolean isFixed, int type){
        super(x, y, width, height);
        this.length = length;
        this.id = id;
        this.orientation = orientation;
        this.isFixed = isFixed;
        this.type = type;
    }

    public void draw(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        // Enable anti-aliasing for smoother curves
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color color = Color.decode("#754209");
        if(this.isFixed){
            color = Color.GRAY;
        }
        g2d.setColor(color);
        RoundRectangle2D roundedPlank = new RoundRectangle2D.Double(x, y, width, height, 10, 10);
        g2d.fill(roundedPlank);
        color = Color.GREEN;
        if(this.type == 1){
            color = Color.yellow;
        }
        g2d.setColor(color);
        RoundRectangle2D roundedLining = new RoundRectangle2D.Double(x+5, y+5, width-10, height-10, 5, 5);
        g2d.draw(roundedLining);
    }

}
