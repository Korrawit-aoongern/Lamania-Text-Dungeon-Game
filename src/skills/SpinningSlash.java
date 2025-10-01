package src.skills;

import src.characters.Character;

public class SpinningSlash extends PhysicalSkill {
    public SpinningSlash() { super("Spinning Slash", 21); }

    @Override
    public void use(Character user, Character target) {
        int attackerPen = user.getPen();
        int dmg = calcDamage(user, 65);

        System.out.println(user.getName() + " spins 360° with Spinning Slash!");
    target.takeDamage(dmg, attackerPen, src.characters.DamageType.PHYSICAL);
    invokeOnHitEffects(user, target);
        incrementUse();
    }
}
