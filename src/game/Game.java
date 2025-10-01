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
    // Steps per move (affects how many steps are counted per move); default 1
    private int stepsPerMove = 1;
    private boolean noclip = false;

    public Game(Player p) {
        this.player = p;
        this.map = new MapGenerator();
        int[] spawn = this.map.findSpawn();
        this.px = spawn[0];
        this.py = spawn[1];
    // MapGenerator now places directional exits relative to center spawn.
    // We intentionally do not relocate exits away from the player here.
    }

    public void start() {
        System.out.println("Welcome to Dungeon Crawler!");
        int steps = 0;
        while (player.getAlive()) {
            map.reveal(px, py, player, noclip);
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
            // Hack / dev cheat menu
            if (dir.equalsIgnoreCase("HACKDEV")) {
                System.out.println("--- HACKDEV MENU ---");
                System.out.println("1. Change map reveal size (width height)");
                System.out.println("2. Change steps per move (integer)");
                System.out.println("3. Toggle Noclip (current: " + (noclip ? "ON" : "OFF") + ")");
                System.out.println("4. Cancel");
                String choice = sc.nextLine().trim();
                if (choice.equals("1")) {
                    System.out.println("Enter width and height (e.g. 30 12):");
                    String[] parts = sc.nextLine().trim().split("\\s+");
                    try {
                        int w = Integer.parseInt(parts[0]);
                        int h = Integer.parseInt(parts[1]);
                        map.setRevealSize(w, h);
                        System.out.println("Reveal size set to " + w + "x" + h);
                    } catch (Exception ex) { System.out.println("Invalid input."); }
                } else if (choice.equals("2")) {
                    System.out.println("Enter steps per move (e.g. 1):");
                    try {
                        int s = Integer.parseInt(sc.nextLine().trim());
                        if (s < 1) s = 1;
                        stepsPerMove = s;
                        System.out.println("Steps per move set to " + stepsPerMove);
                    } catch (Exception ex) { System.out.println("Invalid input."); }
                } else if (choice.equals("3")) {
                    noclip = !noclip;
                    System.out.println("Noclip is now " + (noclip ? "ON" : "OFF"));
                } else {
                    System.out.println("Cancelled.");
                }
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

            // Move up to stepsPerMove tiles in the requested direction, stopping at walls.
            int dx = 0, dy = 0;
            switch (dir) {
                case "W" -> dy = -1;
                case "S" -> dy = 1;
                case "D" -> dx = 1;
                case "A" -> dx = -1;
                default -> { System.out.println("Invalid!"); continue; }
            }

            boolean bumped = false;
            for (int stepIter = 0; stepIter < stepsPerMove; stepIter++) {
                int nx2 = px + dx;
                int ny2 = py + dy;
                if (!noclip && map.getTile(nx2, ny2) == Tile.WALL) {
                    if (stepIter == 0) System.out.println("You bump into a wall.");
                    bumped = true;
                    break;
                }
                // move one tile
                px = nx2; py = ny2;

                // per-sub-step effects
                steps++;
                player.tickConsumableSteps();
                if (steps % 2 == 0) player.regenSp(3);

                // check exit immediately
                if (map.getTile(px, py) == Tile.EXIT) {
                    System.out.println("🎉 You found the exit! Congratulations, you escaped the dungeon!");
                    return;
                }
            }
            if (bumped) continue;

            // If noclip is enabled, disable random encounters and inform the player
            if (noclip) {
                // skip encounters while noclip is active
                continue;
            }

            // base 20% chance random encounter, modified by active consumable effects
            double chance = 0.0;
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
