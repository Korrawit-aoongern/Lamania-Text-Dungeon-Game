package src.skills;

import src.characters.Character;
import src.status.StunDebuff;

public class CrushingBlow extends PhysicalSkill {
    public CrushingBlow() { super("Crushing Blow", 30); }

    @Override
    public void use(Character user, Character target) {
        int dmg = calcDamage(user, 75);

        System.out.println(user.getName() + " uses Crushing Blow and stuns the target!");
        target.takeDamage(dmg, user.getPen(), src.characters.DamageType.PHYSICAL);
        target.applyBuff(new StunDebuff(1), "crushing_blow");
        incrementUse();
    }
}
