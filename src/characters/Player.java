package src.characters;


import src.skills.PoisonInfuse;
import src.skills.SingleSlash;
 
import src.items.Inventory;
import src.items.Potion;

public class Player extends Character {
    private int expToLevel;
    private Inventory inventory = new Inventory();

    public Player(String name) {
        super(name, 1, 100, 50, 20, 5, 5, 0);
        this.expToLevel = 100;
        skills.add(new SingleSlash());
        skills.add(new PoisonInfuse());
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
        boolean hasDef = false;
        for (src.status.Buff b : buffManager.getActiveBuffs()) {
            if (b.getName().equals("DEF Buff")) { hasDef = true; break; }
        }
        if (!hasDef) {
            src.status.Defbuff db = new src.status.Defbuff(1, 30);
            applyBuff(db);
            System.out.println(name + " guards! DEF temporarily up for 1 turn.");
        } else {
            System.out.println(name + " already has a DEF buff; guard has no additional effect.");
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
        atk += 2;
        hp += 10;
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