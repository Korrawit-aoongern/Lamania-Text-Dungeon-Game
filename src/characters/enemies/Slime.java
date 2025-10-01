package src.characters.enemies;

import src.characters.Enemy;

public class Slime extends Enemy {
    public Slime() {
    super("Slime", 1, 20, 30, 10, 2, 0, 0);
        // Slimes are weaker to MAGIC (take 25% more magic damage)
        setTypeModifier(src.characters.DamageType.MAGICAL, 1.25);
        // Skills
        try { addSkill(new src.skills.SlimeSplit()); } catch (Exception ignored) {}
        this.role = "mage";
        this.skillPriorityBias = 1.2;
        setExpReward(14);
    }
}
