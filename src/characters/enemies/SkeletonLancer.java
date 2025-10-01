package src.characters.enemies;

import src.characters.Enemy;

public class SkeletonLancer extends Enemy {
    public SkeletonLancer() {
        // decent HP/ATK, high PEN, slightly weak to physical (kept as lower DEF)
        super("Skeleton Lancer", 5, 75, 48, 28, 10, 5, 18);
        setTypeModifier(src.characters.DamageType.PHYSICAL, 1.15);
    }
}
