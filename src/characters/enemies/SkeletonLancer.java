package src.characters.enemies;

import src.characters.Enemy;

public class SkeletonLancer extends Enemy {
    public SkeletonLancer() {
        // decent HP/ATK, high PEN, slightly weak to physical (kept as lower DEF)
    super("Skeleton Lancer", 6, 75, 120, 28, 10, 5, 18);
        setTypeModifier(src.characters.DamageType.PHYSICAL, 1.15);
        try {
            addSkill(new src.skills.DoubleSlash());
            addSkill(new src.skills.LungeForward());
        } catch (Exception ignored) {}
        this.role = "warrior";
        this.skillPriorityBias = 1.0;
    setExpReward(56);
    }
}
