package game.plank.path;

import javax.swing.*;

public class GameFrame extends JFrame {
    GameFrame(){
        GamePanel gamePanel = new GamePanel();
        this.add(gamePanel);
        this.setTitle("PlankAPath");
        this.pack();
        this.setVisible(true);
        this.setResizable(false);
//        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
