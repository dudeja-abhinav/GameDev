package game.snake.util;

public class TurnPoint {
    public int x;
    public int y;
    public int xDirection;
    public int yDirection;

    public TurnPoint(final int x, final int y, final int xDirection, final int yDirection){
        this.x = x;
        this.y = y;
        this.xDirection = xDirection;
        this.yDirection = yDirection;
    }
}
