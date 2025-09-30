package src.skills;

import src.characters.Character;
import src.status.StunDebuff;

public class HeavensFall extends MagicalSkill {
    public HeavensFall() { super("Heaven's Fall", 133); }

    @Override
    public void use(Character user, Character target) {
        int attackerPen = user.getPen();
        int dmg = calcDamage(user, 200);
        target.takeDamage(dmg, attackerPen);
        target.applyBuff(new StunDebuff(2), "heavens_fall");
        System.out.println(user.getName() + " summons Heaven's Fall crushing foe for " + dmg + " dmg! Enemy stunned 2 turn.");
        incrementUse();
    }
}
