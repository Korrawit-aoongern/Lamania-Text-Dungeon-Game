package src.skills;

import src.characters.Character;

public class FlameStrike extends MagicalSkill {
    public FlameStrike() { super("Flame Strike", 37); }

    @Override
    public void use(Character user, Character target) {
        int attackerPen = user.getPen();
        int dmg = calcHybridDamage(user, 35, 75);
    
        System.out.println(user.getName() + " uses Flame Strike!");
        target.takeDamage(dmg, attackerPen, src.characters.DamageType.MAGICAL);
        incrementUse();
    }
}
