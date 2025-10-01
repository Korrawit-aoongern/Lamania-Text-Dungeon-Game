package src.skills;

import src.characters.Character;
import src.status.StunDebuff;

public class HeavensFall extends MagicalSkill {
    public HeavensFall() { super("Heaven's Fall", 133); }

    @Override
    public void use(Character user, Character target) {
        int attackerPen = user.getPen();
        int dmg = calcDamage(user, 200);

        System.out.println(user.getName() + " summons Heaven's Fall crushing foe, Enemy stunned 2 turn.");
        target.takeDamage(dmg, attackerPen, src.characters.DamageType.MAGICAL);
        target.applyBuff(new StunDebuff(2), "heavens_fall");
        incrementUse();
    }
}
