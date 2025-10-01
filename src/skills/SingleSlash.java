package src.skills;

import src.characters.Character;

public class SingleSlash extends PhysicalSkill {
    public SingleSlash() { super("Single Slash", 3); }

    @Override
    public void use(Character user, Character target) {
        int dmg = calcDamage(user, 40);
        int attackerPen = user.getPen(); // skill flatPen = 0 here
        System.out.println(user.getName() + " uses Single Slash!");
    target.takeDamage(dmg, attackerPen, src.characters.DamageType.PHYSICAL);
    invokeOnHitEffects(user, target);
        incrementUse();
    }
}
