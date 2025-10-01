package src.characters.enemies;

import src.characters.Enemy;

public class GoblinMage extends Enemy {
    public GoblinMage() {
        // higher magic, weak to physical (we'll keep pen/def low to reflect weakness)
    super("Goblin Mage", 3, 30, 60, 8, 2, 28, 0);
        setTypeModifier(src.characters.DamageType.PHYSICAL, 1.2);
        try { addSkill(new src.skills.MagicBolt()); } catch (Exception ignored) {}
        this.role = "mage";
        this.skillPriorityBias = 1.8; // much more likely to use skills
        setExpReward(22);
    }
}
