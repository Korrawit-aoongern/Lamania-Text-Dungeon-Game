package src.characters.enemies;

import src.characters.Enemy;

public class Slime extends Enemy {
    public Slime() {
        super("Slime", 1, 20, 5, 10, 2, 0, 0);
        // Slimes are weaker to MAGIC (take 25% more magic damage)
        setTypeModifier(src.characters.DamageType.MAGICAL, 1.25);
    }
}
