package game.flappy.bird;

import game.flappy.bird.entity.Gap;
import game.flappy.bird.entity.Pipe;

import java.awt.*;
import java.util.ArrayList;

import static game.flappy.bird.util.Constants.GAME_HEIGHT;

public class Obstacle {
    public ArrayList<Pipe> pipes = new ArrayList<>();
    public Gap gap;
    public boolean isCrossed;

    public Obstacle(int x, int y, int gapWidth, int gapHeight, int xVelocity){
        System.out.println("x " + x + ", y " + y + ", gapWidth " + gapWidth + ", gapHeight " + gapHeight + ", y+gapHeight " + (y+gapHeight));
        gap = new Gap(x, y, gapWidth, gapHeight, xVelocity);
        pipes.add(new Pipe(x, 0, gapWidth, y, xVelocity));
        pipes.add(new Pipe(x, y+gapHeight, gapWidth, GAME_HEIGHT - (y+gapHeight-1), xVelocity));
        isCrossed = false;
    }

    public void markCrossed(){
        isCrossed = true;
    }

    public void move(){
        gap.move();
        for(Pipe pipe : pipes){
            pipe.move();
        }
    }

    public void draw(Graphics g){
        for(Pipe pipe : pipes){
            pipe.draw(g);
        }
    }
}
