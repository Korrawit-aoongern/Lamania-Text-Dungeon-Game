package src.skills;

import src.characters.Character;

public class TripleSlash extends PhysicalSkill {
    public TripleSlash() { super("Triple Slash", 34); }

    @Override
    public void use(Character user, Character target) {
        int attackerPen = user.getPen();
        int d1 = calcDamage(user, 40);
        int d2 = calcDamage(user, 35);
        int d3 = calcDamage(user, 30);
        System.out.println(user.getName() + " uses Triple Slash!");
        target.takeDamage(d1, attackerPen, src.characters.DamageType.PHYSICAL);
        target.takeDamage(d2, attackerPen, src.characters.DamageType.PHYSICAL);
        target.takeDamage(d3, attackerPen, src.characters.DamageType.PHYSICAL);
        incrementUse();
    }
}
