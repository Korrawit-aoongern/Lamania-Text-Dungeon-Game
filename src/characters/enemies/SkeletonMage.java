package src.characters.enemies;

import src.characters.Enemy;

public class SkeletonMage extends Enemy {
    public SkeletonMage() {
        // Low HP, decent DEF, very high magic. Marked as weaker to magic conceptually.
        super("Skeleton Mage", 6, 50, 120, 10, 12, 45, 0);
        setTypeModifier(src.characters.DamageType.MAGICAL, 1.25);
    }
}
