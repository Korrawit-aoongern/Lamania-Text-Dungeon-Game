package src.skills;

import src.characters.Character;

public class DoubleSlash extends PhysicalSkill {
    public DoubleSlash() { super("Double Slash", 12); }

    @Override
    public void use(Character user, Character target) {
        int attackerPen = user.getPen();
        int dmg1 = calcDamage(user, 30);
        int dmg2 = calcDamage(user, 30);
        target.takeDamage(dmg1, attackerPen);
        target.takeDamage(dmg2, attackerPen);
        System.out.println(user.getName() + " uses Double Slash dealing " + dmg1 + " + " + dmg2 + " damage!");
        incrementUse();
    }
}
