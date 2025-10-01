package src.skills;

import src.characters.Character;

public class DoubleSlash extends PhysicalSkill {
    public DoubleSlash() { super("Double Slash", 12); }

    @Override
    public void use(Character user, Character target) {
        int attackerPen = user.getPen();
        int dmg1 = calcDamage(user, 30);
        int dmg2 = calcDamage(user, 30);

        System.out.println(user.getName() + " uses Double Slash!");
    target.takeDamage(dmg1, attackerPen, src.characters.DamageType.PHYSICAL);
    invokeOnHitEffects(user, target);
    target.takeDamage(dmg2, attackerPen, src.characters.DamageType.PHYSICAL);
    invokeOnHitEffects(user, target);
        incrementUse();
    }
}
