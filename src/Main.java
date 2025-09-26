package src;

import src.characters.Player;
import src.game.Game;

public class Main {
    public static void main(String[] args) {
        Player p = new Player("Hero");
        Game g = new Game(p);
        g.start();
    }
}