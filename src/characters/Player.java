package src.characters;


import src.items.Inventory;
import src.items.Potion;
import src.skills.HolyBlessing;
import src.skills.PlagueSplit;
import src.skills.PoisonInfuse;
import src.skills.SingleSlash;
import src.skills.WaterSoothing;

public class Player extends Character {
    private int expToLevel;
    private Inventory inventory = new Inventory();
    private src.items.Blade equippedBlade = null;
    private src.items.Boots equippedBoots = null;
    private src.items.Gauntlets equippedGauntlets = null;
    private src.items.Helmet equippedHelmet = null;
    private src.items.Staff equippedStaff = null;
    private src.items.Chestplate equippedChestplate = null;
    // Equip chestplate, applying bonuses and remembering the equipped item
    public void equipChestplate(src.items.Chestplate chestplate) {
        if (equippedChestplate != null) {
            def -= equippedChestplate.getDefBonus();
            maxHp -= equippedChestplate.getHpBonus();
        }
        equippedChestplate = chestplate;
        def += chestplate.getDefBonus();
        maxHp += chestplate.getHpBonus();
        System.out.println(getName() + " equipped chestplate: " + chestplate.getName() + "! DEF +" + chestplate.getDefBonus() + ", HP +" + chestplate.getHpBonus());
    }
    
    // temporary consumable effects that last a number of movement steps
    private int unholyRelicSteps = 0;
    private int cleansingClothSteps = 0;

    public Player(String name) {
        super(name, 1, 100, 50, 30, 5, 5, 0);
        this.expToLevel = 100;
        skills.add(new SingleSlash());
        // starting items (optional)
        inventory.addItem(Potion.smallPotion(), 3);
    }

    @Override
    public void takeTurn(Character opponent) {
        // Player turn handled in Combat
    }

    // Use Character's buffManager and tick implementation

    public Inventory getInventory() { return inventory; }

    // Apply consumable effects (in steps)
    public void applyUnholyRelic(int steps) {
        unholyRelicSteps = steps;
        System.out.println(getName() + " uses Unholy Relic. Encounter rate increased for " + steps + " steps.");
    }

    public void applyCleansingCloth(int steps) {
        cleansingClothSteps = steps;
        System.out.println(getName() + " uses Cleansing Cloth. Encounters reduced for " + steps + " steps.");
    }

    public boolean hasUnholyRelicActive() { return unholyRelicSteps > 0; }
    public boolean hasCleansingClothActive() { return cleansingClothSteps > 0; }

    // Called by the game each time the player takes a movement step
    public void tickConsumableSteps() {
        if (unholyRelicSteps > 0) {
            unholyRelicSteps--;
            if (unholyRelicSteps == 0) System.out.println(getName() + "'s Unholy Relic effect has expired.");
        }
        if (cleansingClothSteps > 0) {
            cleansingClothSteps--;
            if (cleansingClothSteps == 0) System.out.println(getName() + "'s Cleansing Cloth effect has expired.");
        }
    }

    // convenience: add item to inventory
    public void addToInventory(src.items.Item item, int qty) {
        inventory.addItem(item, qty);
    }

    // Equip a blade, applying its bonuses and remembering the equipped item
    public void equipBlade(src.items.Blade blade) {
        if (equippedBlade != null) {
            // remove previous bonuses
            atk -= equippedBlade.getAtkBonus();
            pen -= equippedBlade.getPenBonus();
        }
        equippedBlade = blade;
        atk += blade.getAtkBonus();
        pen += blade.getPenBonus();
        System.out.println(getName() + " equipped " + blade.getName() + "! ATK +" + blade.getAtkBonus() + ", PEN +" + blade.getPenBonus());
    }

    // Equip boots, applying bonuses and remembering the equipped item
    public void equipBoots(src.items.Boots boots) {
        if (equippedBoots != null) {
            def -= equippedBoots.getDefBonus();
            maxHp -= equippedBoots.getHpBonus();
        }
        equippedBoots = boots;
        def += boots.getDefBonus();
        maxHp += boots.getHpBonus();
        System.out.println(getName() + " equipped boots: " + boots.getName() + "! DEF +" + boots.getDefBonus() + ", HP +" + boots.getHpBonus());
    }

    // Equip gauntlets, applying bonuses and remembering the equipped item
    public void equipGauntlets(src.items.Gauntlets gauntlets) {
        if (equippedGauntlets != null) {
            def -= equippedGauntlets.getDefBonus();
            maxHp -= equippedGauntlets.getHpBonus();
        }
        equippedGauntlets = gauntlets;
        def += gauntlets.getDefBonus();
        maxHp += gauntlets.getHpBonus();
        System.out.println(getName() + " equipped gauntlets: " + gauntlets.getName() + "! DEF +" + gauntlets.getDefBonus() + ", HP +" + gauntlets.getHpBonus());
    }

    // Equip helmet, applying bonuses and remembering the equipped item
    public void equipHelmet(src.items.Helmet helmet) {
        if (equippedHelmet != null) {
            def -= equippedHelmet.getDefBonus();
            maxHp -= equippedHelmet.getHpBonus();
        }
        equippedHelmet = helmet;
        def += helmet.getDefBonus();
        maxHp += helmet.getHpBonus();
        System.out.println(getName() + " equipped helmet: " + helmet.getName() + "! DEF +" + helmet.getDefBonus() + ", HP +" + helmet.getHpBonus());
    }

    // Equip staff, applying magic attack bonus and remembering the equipped item
    public void equipStaff(src.items.Staff staff) {
        if (equippedStaff != null) {
            mag -= equippedStaff.getMagicAtkBonus();
        }
        equippedStaff = staff;
        mag += staff.getMagicAtkBonus();
        System.out.println(getName() + " equipped staff: " + staff.getName() + "! MAG +" + staff.getMagicAtkBonus());
    }

    public boolean hasExcaliburEquipped() {
        return equippedBlade != null && "Excalibur".equals(equippedBlade.getName());
    }

    // Print player stats and known skills
    public void printStats() {
        System.out.println("--- Player Stats ---");
        System.out.println("Name: " + getName() + "  Level: " + level + "  EXP: " + exp);
        System.out.println("HP: " + hp + "/" + maxHp + "  SP: " + sp);
        System.out.println("ATK: " + getAtk() + "  DEF: " + getDef() + "  MAG: " + getMag() + "  PEN: " + getPen());
        System.out.println("Active buffs/debuffs:");
        for (var b : buffManager.getActiveBuffs()) {
            System.out.println("  - " + b.getName() + " (" + b.getDuration() + " turns)");
        }
        // Show active consumable effects
        if (unholyRelicSteps > 0) {
            System.out.println("  - Unholy Relic (" + unholyRelicSteps + " steps)");
        }
        if (cleansingClothSteps > 0) {
            System.out.println("  - Cleansing Cloth (" + cleansingClothSteps + " steps)");
        }
        System.out.println("Skills:");
        for (int i = 0; i < skills.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + skills.get(i).getName());
        }
        System.out.println("---------------------");
    }

    public void basicAttack(Character target) {
        int dmg = (int)Math.round(atk * 0.2);
        System.out.println(getName() + " attacks!");
        target.takeDamage(dmg, pen, src.characters.DamageType.PURE);
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
            System.out.println(getName() + " guards! DEF temporarily up for 1 turn.");
        } else {
            System.out.println(getName() + " already has a DEF buff from guarding; guard has no additional effect.");
        }
    }

    public void gainExp(int amount) {
        exp += amount;
        System.out.println(getName() + " gained " + amount + " EXP!");
        // Handle possible multiple level-ups with carryover
        while (exp >= expToLevel) {
            exp -= expToLevel;
            levelUp();
        }
    }

    private void levelUp() {
        level++;
        System.out.println("LEVEL UP! Now level " + level);
        // increase next level requirement
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
        System.out.println(getName() + " damage taken modifier set to " + modifier);
    }

    @Override
    public void setMaxHp(int newMaxHp) {
        super.setMaxHp(newMaxHp);
        System.out.println(getName() + "'s max HP changed to " + newMaxHp);
    }
    @Override
    public void setMaxSp(int ms) {
        if (ms > 500) ms = 500;
        super.setMaxSp(ms);
        System.out.println(getName() + "'s max SP changed to " + ms);
    }
}