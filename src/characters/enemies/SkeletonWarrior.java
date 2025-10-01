package src.characters.enemies;

import src.characters.Enemy;

public class SkeletonWarrior extends Enemy {
    public SkeletonWarrior() {
    super("Skeleton Warrior", 5, 80, 90, 25, 15, 5, 5);
        try {
            addSkill(new src.skills.DoubleSlash());
            addSkill(new src.skills.ShakyThrust());
            addSkill(new src.skills.ShieldUp());
        } catch (Exception ignored) {}
        this.role = "warrior";
        this.skillPriorityBias = 1.1;
    setExpReward(60);
    }
}
