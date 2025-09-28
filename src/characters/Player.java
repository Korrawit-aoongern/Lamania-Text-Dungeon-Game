package src.characters;

import src.status.BuffManager;

public class Player extends Character {
    private int expToLevel;
    private BuffManager buffManager = new BuffManager();

    public Player(String name) {
        super(name, 1, 100, 50, 10, 5, 5, 0);
        this.expToLevel = 100;
    }

    @Override
    public void takeTurn(Character opponent) {
        // Player turn handled in Combat
    }

    public BuffManager getBuffManager() { return buffManager; }
    public void tickBuffs() {
        buffManager.tick(this);
    }

    public void basicAttack(Character target) {
        System.out.println(name + " attacks!");
        target.takeDamage(atk);
    }

    public void guard() {
        System.out.println(name + " guards! DEF temporarily up.");
        def += 5; // simplified
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
}