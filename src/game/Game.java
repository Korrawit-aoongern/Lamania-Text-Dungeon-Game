package src.game;

import src.characters.Player;
import src.characters.Enemy;
import src.characters.enemies.Goblin;
import src.characters.enemies.Slime;

import java.util.Random;
import java.util.Scanner;

public class Game {
    private final Player player;
    private final MapGenerator map;
    private int px, py;
    private final Random rand = new Random();
    private final Scanner sc = new Scanner(System.in);

    public Game(Player p) {
        this.player = p;
        this.map = new MapGenerator();
        this.px = 0;
        this.py = 0;
    }

    public void start() {
        System.out.println("Welcome to Dungeon Crawler!");
        while (player.isAlive()) {
            map.reveal(px, py);
            System.out.println("\nChoose direction: W/A/S/D or Q to quit");
            String dir = sc.nextLine().trim().toUpperCase();

            if (dir.equals("Q")) break;

            int nx = px, ny = py;
            switch (dir) {
                case "W" -> ny--;
                case "S" -> ny++;
                case "D" -> nx++;
                case "A" -> nx--;
                default -> { System.out.println("Invalid!"); continue; }
            }

            if (map.getTile(nx, ny) == Tile.WALL) {
                System.out.println("You bump into a wall.");
                continue;
            }

            px = nx; py = ny;

            // 20% chance random encounter
            if (rand.nextInt(100) < 20) {
                Enemy e = spawnEnemy();
                System.out.println("⚔️ A wild " + e.getName() + " appears!");
                Combat.fight(player, e, sc);
            }
        }
        System.out.println("Game Over!");
    }

    private Enemy spawnEnemy() {
        return rand.nextBoolean() ? new Goblin() : new Slime();
    }
}
