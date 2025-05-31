package game.wordle.entity;

import java.awt.*;
import java.util.ArrayList;

public class Word {
    public ArrayList<Letter> letters = new ArrayList<>(5);

    public Word(){

    }

    public void addLetter(Letter letter){
        letters.add(letter);
    }

    public void remove(){
        letters.remove(letters.size()-1);
    }

    public void draw(Graphics g){
        for(Letter letter : letters){
            letter.draw(g, Color.WHITE);
        }
    }

}
