package src.skills;

import src.characters.Character;

public class ThunderStrike extends MagicalSkill {
    public ThunderStrike() { super("Thunder Strike", 85); }

    @Override
    public void use(Character user, Character target) {
        int attackerPen = user.getPen();
        int dmg = calcDamage(user, 178);
        System.out.println(user.getName() + " calls Thunder Strike");
        target.takeDamage(dmg, attackerPen, src.characters.DamageType.MAGICAL);
        incrementUse();
    }
}
