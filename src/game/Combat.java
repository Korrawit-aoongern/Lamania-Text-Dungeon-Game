package src.game;

import java.util.Random;
import java.util.Scanner;
import src.characters.Enemy;
import src.characters.Player;
import src.items.Blade;
import src.items.CleansingCloth;
import src.items.HolyChalice;
import src.items.Inventory;
import src.items.ItemStack;
import src.items.Gauntlets;
import src.items.Helmet;
import src.items.Chestplate;
import src.items.Boots;
import src.items.Staff;
import src.items.Potion;
import src.items.Scrolls;
import src.items.UnholyRelic;

public class Combat {
    public static void fight(Player p, Enemy e, Scanner sc) {
        Random rand = new Random();
        boolean playerTurn = rand.nextBoolean();

        Inventory inv = p.getInventory();

        boolean firstLoop = true;
        while (p.getAlive() && e.getAlive()) {
            boolean actionTaken = false;
     // If we just entered the player's turn (i.e., enemy acted last), tick buffs and
            // regen SP now
            // On the very first iteration, if it's the player's turn, apply SP regen now
            if (firstLoop) {
                if (playerTurn) {
                    int regen = (int)Math.round(3 * Math.pow(1.1, p.getLevel() - 1));
                    regen = Math.min(50, regen);
                    p.regenSp(regen);
                } else {
                    int regenE = (int)Math.round(3 * Math.pow(1.1, e.getLevel() - 1));
                    regenE = Math.min(50, regenE);
                    e.regenSp(regenE);
                }
            }
            firstLoop = false;
            if (playerTurn) {

                // If player is stunned, they skip their turn
                if (p.isStunned()) {
                    System.out.println(p.getName() + " is stunned and cannot act this turn!");
                    actionTaken = true;
                }

                if (actionTaken) {
                    // we will toggle turn below
                } else {

                System.out.println("\nYour turn! (HP: " + p.getHp() + ")");
                System.out.println("1. Attack  2. Guard  3. Use Skill  4. Use Item 5. Flee");
                String choice = sc.nextLine().trim();

                if (choice.equalsIgnoreCase("stats")) {
                    p.printStats();
                    continue; // don't consume turn
                }
                if (choice.equalsIgnoreCase("estats") || choice.equalsIgnoreCase("e-stats")) {
                    printEnemyStats(e);
                    continue; // don't consume turn
                }

                switch (choice) {
                    case "1" -> {
                        int prevHp = e.getHp();
                        p.basicAttack(e);
                        // One-hit kill cheat: if the attack dealt damage, instantly kill the enemy
                        if (CheatManager.oneHitKill && e.getHp() < prevHp && e.getAlive()) {
                            e.setHp(0);
                            System.out.println("Cheat: One-hit kill activated! " + e.getName() + " is instantly slain.");
                        }
                        actionTaken = true;
                    }
                    case "2" -> {
                        p.guard();
                        actionTaken = true;
                    }
                    case "3" -> {
                        // list skills
                        System.out.println("Choose skill:");
                        for (int i = 0; i < p.getSkills().size(); i++) {
                            var s = p.getSkills().get(i);
                            System.out.println((i + 1) + ". " + s.getName() + " (Cost " + s.getCost() + " SP)");
                        }
                        System.out.println((p.getSkills().size() + 1) + ". Cancel");
                        String in = sc.nextLine().trim();
                        int idx = -1;
                        try {
                            idx = Integer.parseInt(in) - 1;
                        } catch (Exception ignored) {
                        }
                        if (idx == p.getSkills().size()) {
                            // cancel
                            System.out.println("Skill selection cancelled.");
                            continue; // no turn consumed
                        }
                        if (idx >= 0 && idx < p.getSkills().size()) {
                            var skill = p.getSkills().get(idx);
                            if (p.getSp() >= skill.getCost()) {
                                p.consumeSp(skill.getCost());
                                int prevHp = e.getHp();
                                skill.use(p, e);
                                // One-hit kill cheat: if the skill dealt damage, instantly kill the enemy
                                if (CheatManager.oneHitKill && e.getHp() < prevHp && e.getAlive()) {
                                    e.setHp(0);
                                    System.out.println("Cheat: One-hit kill activated! " + e.getName() + " is instantly slain.");
                                }
                                actionTaken = true;
                            } else {
                                System.out.println("Not enough SP!");
                            }
                        } else
                            System.out.println("Invalid skill.");
                    }
                    case "4" -> {
                        // list items
                        System.out.println("Choose item:");
                        var items = inv.getItems();
                        for (int i = 0; i < items.size(); i++) {
                            ItemStack s = items.get(i);
                            System.out.println((i + 1) + ". " + s.getItem().getName() + " x" + s.getCount());
                        }
                        System.out.println((items.size() + 1) + ". Cancel");
                        String in = sc.nextLine().trim();
                        int idx = -1;
                        try {
                            idx = Integer.parseInt(in) - 1;
                        } catch (Exception ignored) {
                        }
                        if (idx == items.size()) {
                            System.out.println("Item selection cancelled.");
                            continue; // don't consume turn
                        }
                        if (idx >= 0 && idx < items.size()) {
                            ItemStack st = items.get(idx);
                            // use via inventory helper so removal is consistent
                            boolean used = inv.useItem(st.getItem().getName(), p, sc);
                            if (used)
                                actionTaken = true;
                        } else {
                            System.out.println("Invalid item.");
                            continue; // invalid selection shouldn't consume turn
                        }
                    }
                    case "5" -> {
                        actionTaken = true;
                        if (rand.nextInt(100) < 50) {
                            System.out.println("You fled successfully!");
                            return;
                        } else {
                            System.out.println("Failed to flee! Lose 10% HP.");
                            p.takeDamage(p.getHp() / 10, e.getPen(), src.characters.DamageType.PHYSICAL);
                        }
                    }
                    default -> System.out.println("Invalid.");
                }
                }
            } else {
                    // If enemy is stunned, they skip their turn immediately
                    if (e.isStunned()) {
                        System.out.println(e.getName() + " is stunned and cannot act this turn!");
                        actionTaken = true;
                    } else {
                        // (regen handled at round boundaries; avoid double-regening here)
                        boolean fled = e.act(p, new Random());
                        if (fled) {
                            System.out.println(e.getName() + " fled the battle!");
                            return; // end combat
                        }
                        actionTaken = true;
                    }
            }
            // After action, check for death and revival
            if (!p.getAlive()) {
                if (tryRevive(inv, p)) {
                    // revived, player gets next turn
                    playerTurn = true;
                    // continue combat
                    continue;
                } else {
                    break;
                }
            }

            if (!e.getAlive())
                break;

            if (actionTaken) {
                // toggle to next actor
                playerTurn = !playerTurn;
                // Tick buffs at the end of a full round (i.e., when we return to the player's turn).
                // This means each side's buffs are decremented once per round and remain active through
                // the opponent's action. When we've toggled back into the player's turn, the round ended.
                if (playerTurn) {
                    p.tickBuffs();
                    e.tickBuffs();
                    // We've just entered the player's turn, regen SP now (scale by level)
                    int regen = (int)Math.round(3 * Math.pow(1.1, p.getLevel() - 1));
                    regen = Math.min(50, regen);
                    p.regenSp(regen);
                } else {
                    // entering enemy's turn -> enemy regen (cap to 50)
                    int regenE = (int)Math.round(3 * Math.pow(1.1, e.getLevel() - 1));
                    regenE = Math.min(50, regenE);
                    e.regenSp(regenE);
                }
            }

            // continue looping; next iteration will tick the appropriate actor's buffs
        }

        if (p.getAlive()) {
            System.out.println("You defeated " + e.getName() + "!");
            int xp = e.getExpReward();
            p.gainExp(xp);
                System.out.println("Checking for loot...");
                addLoot(inv, rand);
        } else {
            System.out.println("You were slain by " + e.getName() + "...");
        }
    }

    private static boolean tryRevive(Inventory inv, Player p) {
        ItemStack chalice = inv.find("Holy Chalice");
        if (chalice != null && chalice.getCount() > 0) {
            chalice.remove(1);
            int reviveHp = (int) Math.round(p.getMaxHp() * 0.8);
            p.setHp(reviveHp);
            System.out.println("The Holy Chalice revives you with " + reviveHp + " HP!");
            return true;
        }
        return false;
    }

    private static void addLoot(Inventory inv, Random rand) {
        java.util.List<String> drops = new java.util.ArrayList<>();
        // probabilities are percent chances
        // 2.5% small potion
        if (rand.nextInt(10000) < 1000) { inv.addItem(Potion.smallPotion(), 1); drops.add("Small HP Potion"); }
    // Increase Unholy/Cleansing spawn chance to 3% (300/10000)
    if (rand.nextInt(10000) < 300) { inv.addItem(new UnholyRelic(), 1); drops.add("Unholy Relic"); }
    if (rand.nextInt(10000) < 300) { inv.addItem(new CleansingCloth(), 1); drops.add("Cleansing Cloth"); }
        if (rand.nextInt(10000) < 170) { inv.addItem(Potion.mediumPotion(), 1); drops.add("Medium HP Potion"); }
        if (rand.nextInt(10000) < 100) { inv.addItem(Potion.largePotion(), 1); drops.add("Large HP Potion"); }

        // scrolls and high value items - treat as 1% -> 100/10000
        String[] scrolls = new String[]{
            "Single Slash Scroll","Double Slash Scroll","Triple Slash Scroll","Spinning Slash Scroll",
            "Downward Slash Scroll","Lunge Forward Scroll","Tornado Blade Scroll","Flame Strike Scroll",
            "Water Soothing Scroll","Holy Blessing Scroll","Thunder Strike Scroll","Plague Split Scroll",
            "Poison Infuse Scroll","Triple Shadow Step Scroll","Heaven's Fall Scroll","Smite Stomp Scroll",
            "Holy Barrier Scroll","Astral Blade Scroll","Astral Fury Scroll"
            ,"Heal Scroll"
        };
        for (String sname : scrolls) {
            if (rand.nextInt(10000) < 500) {
                // map name to actual Scroll factory where possible
                switch (sname) {
                    case "Single Slash Scroll" -> { inv.addItem(Scrolls.singleSlash(), 1); drops.add("Single Slash Scroll"); }
                    case "Double Slash Scroll" -> { inv.addItem(Scrolls.doubleSlash(), 1); drops.add("Double Slash Scroll"); }
                    case "Triple Slash Scroll" -> { inv.addItem(Scrolls.tripleSlash(), 1); drops.add("Triple Slash Scroll"); }
                    case "Spinning Slash Scroll" -> { inv.addItem(Scrolls.spinningSlash(), 1); drops.add("Spinning Slash Scroll"); }
                    case "Downward Slash Scroll" -> { inv.addItem(Scrolls.downwardSlash(), 1); drops.add("Downward Slash Scroll"); }
                    case "Lunge Forward Scroll" -> { inv.addItem(Scrolls.lungeForward(), 1); drops.add("Lunge Forward Scroll"); }
                    case "Tornado Blade Scroll" -> { inv.addItem(Scrolls.tornadoBlade(), 1); drops.add("Tornado Blade Scroll"); }
                    case "Flame Strike Scroll" -> { inv.addItem(Scrolls.flameStrike(), 1); drops.add("Flame Strike Scroll"); }
                    case "Water Soothing Scroll" -> { inv.addItem(Scrolls.waterSoothing(), 1); drops.add("Water Soothing Scroll"); }
                    case "Holy Blessing Scroll" -> { inv.addItem(Scrolls.holyBlessing(), 1); drops.add("Holy Blessing Scroll"); }
                    case "Thunder Strike Scroll" -> { inv.addItem(Scrolls.thunderStrike(), 1); drops.add("Thunder Strike Scroll"); }
                    case "Plague Split Scroll" -> { inv.addItem(Scrolls.plagueSplit(), 1); drops.add("Plague Split Scroll"); }
                    case "Poison Infuse Scroll" -> { inv.addItem(Scrolls.poisonInfuse(), 1); drops.add("Poison Infuse Scroll"); }
                    case "Triple Shadow Step Scroll" -> { inv.addItem(Scrolls.tripleShadowStep(), 1); drops.add("Triple Shadow Step Scroll"); }
                    case "Heaven's Fall Scroll" -> { inv.addItem(Scrolls.heavensFall(), 1); drops.add("Heaven's Fall Scroll"); }
                    case "Smite Stomp Scroll" -> { inv.addItem(Scrolls.smiteStomp(), 1); drops.add("Smite Stomp Scroll"); }
                    case "Holy Barrier Scroll" -> { inv.addItem(Scrolls.holyBarrier(), 1); drops.add("Holy Barrier Scroll"); }
                    case "Astral Blade Scroll" -> { inv.addItem(Scrolls.astralBlade(), 1); drops.add("Astral Blade Scroll"); }
                    case "Astral Fury Scroll" -> { inv.addItem(Scrolls.astralFury(), 1); drops.add("Astral Fury Scroll"); }
                    case "Heal Scroll" -> { inv.addItem(Scrolls.heal(), 1); drops.add("Heal Scroll"); }
                }
            }
        }

        // rare items
        if (rand.nextInt(10000) < 40) { inv.addItem(Potion.supremePotion(), 1); drops.add("Supreme HP Elixir"); }
        if (rand.nextInt(10000) < 10) { inv.addItem(new HolyChalice(), 1); drops.add("Holy Chalice"); }
        if (rand.nextInt(10000) < 1) { inv.addItem(new Blade("Excalibur", 300, 150), 1); drops.add("Excalibur"); }
        if (rand.nextInt(10000) < 10) { inv.addItem(new Staff("Elder Staff", 200), 1); drops.add("Elder Staff"); }
        if (rand.nextInt(10000) < 100) { inv.addItem(new Helmet("Dragon Helm", 100, 50), 1); drops.add("Dragon Helm"); }
        if (rand.nextInt(10000) < 100) { inv.addItem(new Chestplate("Dragon Chestplate", 150, 75), 1); drops.add("Dragon Chestplate"); }
        if (rand.nextInt(10000) < 100) { inv.addItem(new Gauntlets("Dragon Gauntlets", 75, 25), 1); drops.add("Dragon Gauntlets"); }
        if (rand.nextInt(10000) < 100) { inv.addItem(new Boots("Dragon Boots", 70, 20), 1); drops.add("Dragon Boots"); }


        if (drops.isEmpty()) {
            System.out.println("Loot: none");
        } else {
            for (String d : drops) System.out.println("Loot: " + d);
        }
    }

    private static void printEnemyStats(Enemy e) {
        System.out.println("--- Enemy Stats ---");
        System.out.println("Name: " + e.getName() + "  Level: " + e.getLevel());
        System.out.println("HP: " + e.getHp() + "/" + e.getMaxHp() + "  SP: " + e.getSp() + "/" + e.getMaxSp());
        System.out.println("ATK: " + e.getAtk() + "  DEF: " + e.getDef() + "  MAG: " + e.getMag() + "  PEN: " + e.getPen());
        System.out.println("Active buffs/debuffs:");
        for (var b : e.getBuffManager().getActiveBuffs()) {
            System.out.println("  - " + b.getName() + " (" + b.getDuration() + " turns)");
        }
        System.out.println("Known skills:");
        for (int i = 0; i < e.getSkills().size(); i++) {
            var s = e.getSkills().get(i);
            System.out.println("  " + (i + 1) + ". " + s.getName() + " (Cost " + s.getCost() + " SP)");
        }
        System.out.println("-------------------");
    }
}
