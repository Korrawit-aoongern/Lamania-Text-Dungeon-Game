package src.characters.enemies;

import src.characters.Enemy;

public class SkeletonMage extends Enemy {
    public SkeletonMage() {
        // Low HP, decent DEF, very high magic. Marked as weaker to magic conceptually.
    super("Skeleton Mage", 7, 50, 200, 10, 12, 45, 0);
        setTypeModifier(src.characters.DamageType.MAGICAL, 1.25);
        try {
            addSkill(new src.skills.ThunderStrike());
            addSkill(new src.skills.PlagueSplit());
        } catch (Exception ignored) {}
        this.role = "mage";
        this.skillPriorityBias = 1.6;
        setExpReward(32);
    }
}
