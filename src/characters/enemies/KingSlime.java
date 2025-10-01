package src.characters.enemies;

import src.characters.Enemy;

public class KingSlime extends Enemy {
    public KingSlime() {
        // Very tanky: high HP, high DEF, decent ATK. Design note: 100% physical RES is implemented by
        // extremely high DEF here; specialized damage-type system can be added later.
    super("King Slime", 6, 300, 140, 30, 40, 5, 0);
        // King Slime: extremely resistant to PHYSICAL (treated as immune here)
        setTypeModifier(src.characters.DamageType.PHYSICAL, 0.0);
    }
}
