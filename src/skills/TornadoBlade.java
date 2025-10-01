package src.skills;

import src.characters.Character;
import src.status.StunDebuff;

public class TornadoBlade extends PhysicalSkill {
    public TornadoBlade() { super("Tornado Blade", 70); }

    @Override
    public void use(Character user, Character target) {
        int total = 0;
        int attackerPen = user.getPen();
        for (int i=0; i<10; i++) {
            int dmg = calcDamage(user, 12);
            target.takeDamage(dmg, attackerPen, src.characters.DamageType.PHYSICAL);
            invokeOnHitEffects(user, target);
            total += dmg;
        }
    user.applyBuff(new StunDebuff(2), "tornado_blade");
        System.out.println(user.getName() + " summons Tornado Blade hitting 10 times" + total + " (Cannot act 2 turns)");
        incrementUse();
    }
}
