package src.skills;

import src.characters.Character;

public class LungeForward extends PhysicalSkill {
    public LungeForward() { super("Lunge Forward", 50); }

    @Override
    public void use(Character user, Character target) {
        int attackerPen = user.getPen() * ((30 / 100) + 1);
        int dmg = calcDamage(user, 70);
        System.out.println(user.getName() + " lunges attack forward (with 30% PEN)!");
        target.takeDamage(dmg, attackerPen, src.characters.DamageType.PHYSICAL);
        incrementUse();
    }
}
