package src.characters.enemies;

import src.characters.Enemy;

public class GoblinWarrior extends Enemy {
    public GoblinWarrior() {
        // name, level, hp, sp, atk, def, mag, pen
    super("Goblin Warrior", 2, 45, 35, 22, 6, 0, 0);
        try { addSkill(new src.skills.DoubleSlash()); } catch (Exception ignored) {}
        this.role = "warrior";
        this.skillPriorityBias = 1.0;
        setExpReward(20);
    }
}
