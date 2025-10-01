package src.characters.enemies;

import src.characters.Enemy;

public class Goblin extends Enemy {
    public Goblin() {
    super("Goblin", 1, 30, 20, 15, 3, 0, 0);
        // Skills: only Single Slash (already added by base)
        this.role = "grunt";
        this.skillPriorityBias = 0.8;
    setExpReward(24);
    }
}
