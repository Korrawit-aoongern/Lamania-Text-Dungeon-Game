package src.game;

import java.util.Random;
import java.util.Scanner;
import src.characters.Enemy;
import src.characters.Player;
import src.characters.enemies.Goblin;
import src.characters.enemies.Slime;
import src.items.Inventory;

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
        int steps = 0;
        while (player.getAlive()) {
            map.reveal(px, py);
            System.out.println("\nChoose direction: W/A/S/D");
            System.out.println("stats to view stats");
            System.out.println("inv to view inventory");
            System.out.println("Q to quit");
            String dir = sc.nextLine().trim().toUpperCase();

            if (dir.equals("Q")) break;
            if (dir.equalsIgnoreCase("STATS")) {
                player.printStats();
                continue;
            }

            if (dir.equalsIgnoreCase("INV")) {
                // Inventory viewer + use
                var items = player.getInventory().getItems();
                if (items.isEmpty()) { System.out.println("Inventory is empty."); continue; }
                System.out.println("Inventory:");
                for (int i = 0; i < items.size(); i++) {
                    var s = items.get(i);
                    System.out.println((i + 1) + ". " + s.getItem().getName() + " x" + s.getCount());
                }
                System.out.println((items.size() + 1) + ". Cancel");
                String ans = sc.nextLine().trim();
                int idx = -1;
                try { idx = Integer.parseInt(ans) - 1; } catch (Exception ignored) {}
                if (idx == items.size()) { System.out.println("Cancelled."); continue; }
                if (idx >= 0 && idx < items.size()) {
                    var st = items.get(idx);
                    boolean used = player.getInventory().useItem(st.getItem().getName(), player, sc);
                    if (used) System.out.println("Used " + st.getItem().getName() + ".");
                } else {
                    System.out.println("Invalid.");
                }
                continue;
            }

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

            px = nx; 
            py = ny;

            // 🔹 เช็คถ้าผู้เล่นถึงประตูทางออก
            if (map.getTile(px, py) == Tile.EXIT) {
                System.out.println("🎉 You found the exit! Congratulations, you escaped the dungeon!");
                break;
            }

            // count steps and regen SP every 2 steps
            steps++;
            if (steps % 2 == 0) {
                player.regenSp(3);
            }

            // base 20% chance random encounter, modified by items
            double chance = 20.0;
            Inventory inv = player.getInventory();
            if (inv.find("Unholy Relic") != null) chance *= 1.5; // +50%
            if (inv.find("Cleansing Cloth") != null) chance *= 0.5; // -50%

            // Check for exits: if we stepped on an EXIT tile, player wins immediately
            if (map.getTile(px, py) == Tile.EXIT) {
                System.out.println("You found the exit! Victory!");
                return; // end the game
            }

            if (rand.nextInt(100) < (int)Math.round(chance)) {
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
