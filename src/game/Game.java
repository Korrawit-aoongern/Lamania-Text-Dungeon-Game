package src.game;

import java.util.Random;
import java.util.Scanner;
import src.characters.Enemy;
import src.characters.Player;
import src.characters.enemies.Goblin;
import src.characters.enemies.Slime;

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

            if (dir.equals("Q")) {
                System.out.println("Are you sure you want to quit? (Y/N)");
                String confirm = sc.nextLine().trim().toUpperCase();
                if (confirm.equals("Y") || confirm.equals("YES")) {
                    System.out.println("Quitting game. Goodbye!");
                    break;
                } else {
                    System.out.println("Quit cancelled.");
                    continue;
                }
            }
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
            // tick consumable step counters (Unholy Relic / Cleansing Cloth)
            player.tickConsumableSteps();
            if (steps % 2 == 0) {
                player.regenSp(3);
            }

            // base 20% chance random encounter, modified by active consumable effects
            double chance = 20.0;
            if (player.hasUnholyRelicActive()) chance *= 1.5; // +50%
            if (player.hasCleansingClothActive()) chance *= 0.5; // -50%

            // Check for exits: if we stepped on an EXIT tile, player wins immediately
            if (map.getTile(px, py) == Tile.EXIT) {
                System.out.println("You found the exit! Victory!");
                return; // end the game
            }

            if (rand.nextInt(100) < (int)Math.round(chance)) {
                Enemy e = spawnEnemy();
                // Scale enemy based on how many steps the player has taken: +1 level per 50 steps
                int extraLevels = steps / 50;
                if (extraLevels > 0) {
                    e.scaleUpByLevels(extraLevels);
                    System.out.println("⚔️ A wild " + e.getName() + " appears! It looks tougher (" + extraLevels + " extra levels).");
                } else {
                    System.out.println("⚔️ A wild " + e.getName() + " appears!");
                }
                Combat.fight(player, e, sc);
            }
        }
        System.out.println("Game Over!");
    }

    private Enemy spawnEnemy() {
        // Determine spawn level relative to the player's level
        int playerLevel = player.getLevel();
        // Build a small weighted distribution for levels: player-3 .. player are base options
        // and player+1 is a rare high-level option. We'll use integer weights.
        int minLevel = Math.max(1, playerLevel - 3);
        // weights: each of minLevel..playerLevel gets weight 100, player+1 gets weight 10
        int baseWeight = 100;
        int rareWeight = 10; // ~10% relative weight

        // Sum weights for range
        int rangeCount = playerLevel - minLevel + 1;
        int totalWeight = rangeCount * baseWeight + rareWeight;

        int pick = rand.nextInt(totalWeight);
        int chosenLevel;
        if (pick >= rangeCount * baseWeight) {
            // pick the rare higher level
            chosenLevel = playerLevel + 1;
        } else {
            // pick in the normal range
            int idx = pick / baseWeight; // 0..rangeCount-1
            chosenLevel = minLevel + idx;
        }

        Enemy e = rand.nextBoolean() ? new Goblin() : new Slime();
        // Scale enemy to the chosen level (scaleUpByLevels increases levels incrementally)
        int levelsToAdd = Math.max(0, chosenLevel - e.getLevel());
        if (levelsToAdd > 0) e.scaleUpByLevels(levelsToAdd);
        return e;
    }
}
