package src.characters.enemies;

import src.characters.Enemy;

public class SkeletonElite extends Enemy {
    public SkeletonElite() {
        // high atk/def/hp, slightly weak to magic
    super("Skeleton Elite", 20, 578, 300, 102, 54, 24, 8);
        setTypeModifier(src.characters.DamageType.MAGICAL, 1.15);
    }
}
