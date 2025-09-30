package src.characters;


import src.skills.PlagueSplit;
import src.skills.SingleSlash;
import src.skills.WaterSoothing;
import src.skills.HolyBlessing;
import src.items.Inventory;
import src.items.Potion;

public class Player extends Character {
    private int expToLevel;
    private Inventory inventory = new Inventory();

    public Player(String name) {
        super(name, 1, 100, 50, 20, 5, 5, 0);
        this.expToLevel = 100;
        skills.add(new SingleSlash());
        skills.add(new PlagueSplit());
        skills.add(new WaterSoothing());
        skills.add(new HolyBlessing());
        // starting items (optional)
        inventory.addItem(Potion.smallPotion(), 1);
    }

    @Override
    public void takeTurn(Character opponent) {
        // Player turn handled in Combat
    }

    // Use Character's buffManager and tick implementation

    public Inventory getInventory() { return inventory; }

    // convenience: add item to inventory
    public void addToInventory(src.items.Item item, int qty) {
        inventory.addItem(item, qty);
    }

    // Print player stats and known skills
    public void printStats() {
        System.out.println("--- Player Stats ---");
        System.out.println("Name: " + name + "  Level: " + level + "  EXP: " + exp);
        System.out.println("HP: " + hp + "/" + maxHp + "  SP: " + sp);
        System.out.println("ATK: " + getAtk() + "  DEF: " + getDef() + "  MAG: " + getMag() + "  PEN: " + getPen());
        System.out.println("Active buffs/debuffs:");
        for (var b : buffManager.getActiveBuffs()) {
            System.out.println("  - " + b.getName() + " (" + b.getDuration() + " turns)");
        }
        System.out.println("Skills:");
        for (int i = 0; i < skills.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + skills.get(i).getName());
        }
        System.out.println("---------------------");
    }

    public void basicAttack(Character target) {
        int dmg = (int)Math.round(atk * 0.2);
        target.takeDamage(dmg, pen);
        System.out.println(name + " attacks!");
    }

    public void guard() {
        // Apply a one-turn DEF buff (30% increase) but don't stack if already present
        // Only consider an existing DEF buff a conflict if it was applied by the guard action itself
        boolean hasGuardDef = false;
        for (src.status.Buff b : buffManager.getActiveBuffs()) {
            if (b.getName().equals("DEF Buff") && "guard".equals(b.getSource())) { hasGuardDef = true; break; }
        }
        if (!hasGuardDef) {
            src.status.Defbuff db = new src.status.Defbuff(1, 30);
            applyBuff(db, "guard");
            System.out.println(name + " guards! DEF temporarily up for 1 turn.");
        } else {
            System.out.println(name + " already has a DEF buff from guarding; guard has no additional effect.");
        }
    }

    public void gainExp(int amount) {
        exp += amount;
        System.out.println(name + " gained " + amount + " EXP!");
        if (exp >= expToLevel) {
            levelUp();
        }
    }

    private void levelUp() {
        level++;
        exp = 0;
        expToLevel += 10;
        System.out.println("LEVEL UP! Now level " + level);
        // increase all base stats by 10% (rounding)
        atk = (int)Math.round(atk * 1.1);
        def = (int)Math.round(def * 1.1);
        mag = (int)Math.round(mag * 1.1);
        pen = (int)Math.round(pen * 1.1);
        // increase max HP and SP by 10% and refill to max
        int newMaxHp = (int)Math.round(getMaxHp() * 1.1);
        setMaxHp(newMaxHp);
        setHp(newMaxHp);
        int newMaxSp = (int)Math.round(getMaxSp() * 1.1);
        setMaxSp(newMaxSp);
        setSp(newMaxSp);
    }
    @Override
    public void setDamageTakenModifier(double modifier) {
        super.setDamageTakenModifier(modifier);
        System.out.println(name + " damage taken modifier set to " + modifier);
    }

    @Override
    public void setMaxHp(int newMaxHp) {
        super.setMaxHp(newMaxHp);
        System.out.println(name + "'s max HP changed to " + newMaxHp);
    }
}