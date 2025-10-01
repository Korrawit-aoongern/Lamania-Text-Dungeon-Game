package src.game;

import java.util.Random;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import src.characters.Enemy;
import src.characters.Player;
import src.characters.enemies.*;
import java.util.function.Supplier;

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
                System.out.println("1. Toggle One-hit kill (current: " + (CheatManager.oneHitKill ? "ON" : "OFF") + ")");
                System.out.println("2. Toggle Immunity (player) (cheat - true invulnerability) (current: " + (CheatManager.immunity ? "ON" : "OFF") + ")");
                System.out.println("3. Warp to nearest Exit (not on top)");
                System.out.println("4. Fight Demon King");
                System.out.println("5. All stats 999");
                System.out.println("6. All stats self-set");
                System.out.println("7. Get all items (1 quantity)");
                System.out.println("8. Get all potions (99)");
                System.out.println("9. Toggle No Skill Limit (current: " + (CheatManager.noSkillLimit ? "ON" : "OFF") + ")");
                System.out.println("10. Set level");
                System.out.println("11. Set reveal window size (current: " + map.getRevealWidth() + "x" + map.getRevealHeight() + ")");
                System.out.println("12. Set steps per move (current: " + stepsPerMove + ")");
                System.out.println("13. Toggle noclip (current: " + (noclip ? "ON" : "OFF") + ")");
                System.out.println("14. Fight random enemy (same level)");
                System.out.println("15. Fight random enemy (your level -10)");
                System.out.println("16. Fight random enemy (your level +10)");
                System.out.println("17. Kill yourself");
                System.out.println("18. Unlock all Skills (learn every skill)");
                System.out.println("0. Cancel");
                String choice = sc.nextLine().trim();
                switch (choice) {
                    case "1" -> { CheatManager.oneHitKill = !CheatManager.oneHitKill; System.out.println("One-hit kill: " + (CheatManager.oneHitKill ? "ON" : "OFF")); }
                    case "2" -> { CheatManager.immunity = !CheatManager.immunity; System.out.println("Cheat immunity: " + (CheatManager.immunity ? "ON" : "OFF")); }
                    case "3" -> {
                        int[] ex = map.findNearestExit(px, py);
                        if (ex == null) { System.out.println("No exit found."); }
                        else {
                            // teleport player adjacent to exit if possible (not on top)
                            int exx = ex[0], exy = ex[1];
                            // try to place one tile left/right/up/down if floor
                            int[][] cand = new int[][]{{exx-1,exy},{exx+1,exy},{exx,exy-1},{exx,exy+1}};
                            boolean placed = false;
                            for (int[] c : cand) {
                                if (map.getTile(c[0], c[1]) == Tile.FLOOR) { px = c[0]; py = c[1]; placed = true; break; }
                            }
                            if (!placed) { px = exx; py = exy; }
                            System.out.println("Warped near exit at (" + px + "," + py + ")");
                        }
                    }
                    case "4" -> {
                        System.out.println("Spawning Demon King...");
                        boolean won = Combat.fight(player, new DemonKing(), sc);
                        if (won) {
                            // try to place an exit in front of the player (prefer up, right, left, down)
                            int[][] cand = new int[][]{{px,py-1},{px+1,py},{px-1,py},{px,py+1}};
                            boolean placed = false;
                            for (int[] c : cand) {
                                if (map.getTile(c[0], c[1]) == Tile.FLOOR) { map.placeExitAt(c[0], c[1]); placed = true; System.out.println("An exit appears nearby!"); break; }
                            }
                            if (!placed) {
                                // place on current tile if nothing else
                                map.placeExitAt(px, py);
                                System.out.println("An exit appears at your location!");
                            }
                        }
                    }
                    case "5" -> { player.setAtk(999); player.setDef(999); player.setMag(999); player.setHp(999); player.setMaxHp(999); player.setSp(999); player.setMaxSp(999); player.setPen(999); System.out.println("All stats set to 999."); }
                    case "6" -> {
                        System.out.println("Enter ATK DEF MAG PEN HP SP (space-separated):");
                        String[] parts = sc.nextLine().trim().split("\\s+");
                        try {
                            int a = Integer.parseInt(parts[0]); int d = Integer.parseInt(parts[1]); int m = Integer.parseInt(parts[2]); int pval = Integer.parseInt(parts[3]); int h = Integer.parseInt(parts[4]); int s = Integer.parseInt(parts[5]);
                            player.setAtk(a); player.setDef(d); player.setMag(m); player.setPen(pval); player.setMaxHp(h); player.setHp(h); player.setMaxSp(s); player.setSp(s);
                            System.out.println("Stats updated.");
                        } catch (Exception ex) { System.out.println("Invalid input."); }
                    }
                    case "7" -> {
                        // add one of a representative set of items, potions and scrolls
                        System.out.println("Adding a broad set of items and potions...");
                        player.addToInventory(new src.items.HolyChalice(), 1);
                        player.addToInventory(new src.items.CleansingCloth(), 1);
                        player.addToInventory(new src.items.UnholyRelic(), 1);
                        player.addToInventory(new src.items.Blade("Excalibur", 300, 150), 1);
                        player.addToInventory(new src.items.Staff("Elder Staff", 200), 1);
                        // auto-equip if possible
                        player.equipBlade(new src.items.Blade("Excalibur", 300, 150));
                        player.equipStaff(new src.items.Staff("Elder Staff", 200));
                        player.addToInventory(new src.items.Helmet("Dragon Helm", 100, 50), 1);
                        player.addToInventory(new src.items.Chestplate("Dragon Chestplate", 150, 75), 1);
                        player.addToInventory(new src.items.Gauntlets("Dragon Gauntlets", 75, 25), 1);
                        player.addToInventory(new src.items.Boots("Dragon Boots", 70, 20), 1);
                        // potions
                        player.addToInventory(src.items.Potion.smallPotion(), 10);
                        player.addToInventory(src.items.Potion.mediumPotion(), 5);
                        player.addToInventory(src.items.Potion.largePotion(), 2);
                        player.addToInventory(src.items.Potion.supremePotion(), 1);
                        // some scrolls
                        player.addToInventory(src.items.Scrolls.singleSlash(), 1);
                        player.addToInventory(src.items.Scrolls.tripleSlash(), 1);
                        player.addToInventory(src.items.Scrolls.heal(), 1);
                        player.addToInventory(src.items.Scrolls.poisonInfuse(), 1);
                        System.out.println("Items added to inventory.");
                    }
                    case "8" -> { player.addToInventory(src.items.Potion.smallPotion(), 99); player.addToInventory(src.items.Potion.mediumPotion(), 99); player.addToInventory(src.items.Potion.largePotion(), 99); player.addToInventory(src.items.Potion.supremePotion(), 99); System.out.println("Added potions."); }
                    case "9" -> { CheatManager.noSkillLimit = !CheatManager.noSkillLimit; System.out.println("No skill limit: " + (CheatManager.noSkillLimit ? "ON" : "OFF")); }
                    case "10" -> {
                        System.out.println("Enter level:");
                        try { int lvl = Integer.parseInt(sc.nextLine().trim()); player.scaleToLevel(lvl); System.out.println("Level set to " + lvl + " and stats scaled accordingly."); } catch (Exception ex) { System.out.println("Invalid input."); }
                    }
                    case "11" -> {
                        System.out.println("Enter reveal width and height (e.g. 20 10):");
                        String[] parts = sc.nextLine().trim().split("\\s+");
                        if (parts.length >= 2) {
                            try {
                                int w = Integer.parseInt(parts[0]);
                                int h = Integer.parseInt(parts[1]);
                                map.setRevealSize(w, h);
                                System.out.println("Reveal window set to " + w + "x" + h + ".");
                            } catch (NumberFormatException nfe) {
                                System.out.println("Invalid numbers.");
                            }
                        } else System.out.println("Cancelled.");
                    }
                    case "12" -> {
                        System.out.println("Enter steps per move (integer >=1):");
                        try { int s = Integer.parseInt(sc.nextLine().trim()); if (s < 1) s = 1; stepsPerMove = s; System.out.println("stepsPerMove set to " + stepsPerMove); } catch (Exception ex) { System.out.println("Invalid input."); }
                    }
                    case "13" -> { noclip = !noclip; System.out.println("noclip: " + (noclip ? "ON" : "OFF")); }
                    case "14" -> { Enemy e = spawnEnemy(); e.scaleUpByLevels(Math.max(0, player.getLevel() - e.getLevel())); Combat.fight(player, e, sc); }
                    case "15" -> { Enemy e = spawnEnemy(); int targetLvl = Math.max(1, player.getLevel() - 10); int levelsToAdd = Math.max(0, targetLvl - e.getLevel()); if (levelsToAdd>0) e.scaleUpByLevels(levelsToAdd); Combat.fight(player, e, sc); }
                    case "16" -> { Enemy e = spawnEnemy(); int targetLvl = player.getLevel() + 10; int levelsToAdd = Math.max(0, targetLvl - e.getLevel()); if (levelsToAdd>0) e.scaleUpByLevels(levelsToAdd); Combat.fight(player, e, sc); }
                    case "17" -> { System.out.println("Self-destruct initiated."); player.setHp(0); }
                    case "18" -> {
                        System.out.println("Unlocking all skills...");
                        // instantiate and add every skill class we know about
                        java.util.List<src.skills.AbstractSkill> all = new java.util.ArrayList<>();
                        all.add(new src.skills.SingleSlash());
                        all.add(new src.skills.DoubleSlash());
                        all.add(new src.skills.TripleSlash());
                        all.add(new src.skills.SpinningSlash());
                        all.add(new src.skills.LungeForward());
                        all.add(new src.skills.TornadoBlade());
                        all.add(new src.skills.DownwardSlash());
                        all.add(new src.skills.ShakyThrust());
                        all.add(new src.skills.TripleShadowStep());
                        all.add(new src.skills.CrushingBlow());
                        all.add(new src.skills.PummelStrike());
                        all.add(new src.skills.AstralBlade());
                        all.add(new src.skills.AstralFury());
                        all.add(new src.skills.MagicBolt());
                        all.add(new src.skills.FlameStrike());
                        all.add(new src.skills.ThunderStrike());
                        all.add(new src.skills.HeavensFall());
                        all.add(new src.skills.WaterSoothing());
                        all.add(new src.skills.HolyBlessing());
                        all.add(new src.skills.HolyBarrier());
                        all.add(new src.skills.PlagueSplit());
                        all.add(new src.skills.SlimeSplit());
                        all.add(new src.skills.PoisonInfuse());
                        all.add(new src.skills.SmiteStomp());
                        all.add(new src.skills.Devour());
                        all.add(new src.skills.ShieldUp());
                        all.add(new src.skills.Heal());
                        int learned = 0;
                        for (var s : all) {
                            if (!player.hasSkill(s)) {
                                player.addSkill(s);
                                learned++;
                            }
                        }
                        System.out.println("Learned " + learned + " new skills.");
                    }
                    default -> System.out.println("Cancelled.");
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

            if (dir.equalsIgnoreCase("STATS") || dir.equalsIgnoreCase("STAT")) {
                player.printStats();
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
                boolean won = Combat.fight(player, e, sc);
                if (won && e instanceof DemonKing) {
                    // try to place an exit adjacent to player
                    int[][] cand = new int[][]{{px,py-1},{px+1,py},{px-1,py},{px,py+1}};
                    boolean placed = false;
                    for (int[] c : cand) {
                        if (map.getTile(c[0], c[1]) == Tile.FLOOR) { map.placeExitAt(c[0], c[1]); placed = true; System.out.println("An exit appears nearby!"); break; }
                    }
                    if (!placed) { map.placeExitAt(px, py); System.out.println("An exit appears at your location!"); }
                }
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

        // Build a list of candidate enemy factories whose base level <= player level
        List<Supplier<Enemy>> factories = new ArrayList<>();
        factories.add(Goblin::new);
        factories.add(Slime::new);
        factories.add(GoblinWarrior::new);
        factories.add(GoblinMage::new);
        factories.add(KingSlime::new);
        factories.add(SkeletonWarrior::new);
        factories.add(SkeletonMage::new);
        factories.add(SkeletonLancer::new);
        factories.add(SkeletonElite::new);

        // If player is >= 30, small chance to spawn Demon King as a boss (rare)
        boolean bossRoll = playerLevel >= 30 && rand.nextInt(100) < 5; // 5% chance
        if (bossRoll) {
            Enemy boss = new DemonKing();
            int bossLevelsToAdd = Math.max(0, (playerLevel + 2) - boss.getLevel());
            if (bossLevelsToAdd > 0) boss.scaleUpByLevels(bossLevelsToAdd);
            return boss;
        }

        // Filter factories by their prototype's base level
        List<Supplier<Enemy>> candidates = new ArrayList<>();
        for (Supplier<Enemy> f : factories) {
            Enemy proto = f.get();
            if (proto.getLevel() <= playerLevel) candidates.add(f);
        }
        if (candidates.isEmpty()) candidates.add(Goblin::new); // fallback

        Supplier<Enemy> chosenFactory = candidates.get(rand.nextInt(candidates.size()));
        Enemy e = chosenFactory.get();
        // Scale enemy to the chosenLevel (scaleUpByLevels increases levels incrementally)
        int levelsToAdd = Math.max(0, chosenLevel - e.getLevel());
        if (levelsToAdd > 0) e.scaleUpByLevels(levelsToAdd);
        return e;
    }
}
