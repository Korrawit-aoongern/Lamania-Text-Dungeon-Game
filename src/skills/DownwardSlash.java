package src.skills;

import src.characters.Character;
import src.status.DefDebuff;

public class DownwardSlash extends PhysicalSkill {
    public DownwardSlash() { super("Downward Slash", 27); }

    @Override
    public void use(Character user, Character target) {
        int attackerPen = user.getPen();
        int dmg = calcDamage(user, 78);

        System.out.println(user.getName() + " performs Downward Slash Shatter 20% of thier DEF.");
    target.takeDamage(dmg, attackerPen, src.characters.DamageType.PHYSICAL);
    invokeOnHitEffects(user, target);
        target.applyBuff(new DefDebuff(20, 1), "downward_slash");
        incrementUse();
    }
}
