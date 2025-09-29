package src.skills;

import src.characters.Character;

public class ThunderStrike extends MagicalSkill {
    public ThunderStrike() { super("Thunder Strike", 85); }

    @Override
    public void use(Character user, Character target) {
        int attackerPen = user.getPen();
        int dmg = calcDamage(user, 178);
        target.takeDamage(dmg, attackerPen);
        System.out.println(user.getName() + " calls Thunder Strike dealing " + dmg + " magic damage!");
        incrementUse();
    }
}
