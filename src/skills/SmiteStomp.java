package src.skills;

import src.characters.Character;
import src.status.DamageTakenUpDebuff;

public class SmiteStomp extends MagicalSkill {
    public SmiteStomp() { super("Smite Stomp", 95); }

    @Override
    public void use(Character user, Character target) {
        int attackerPen = user.getPen();
        int dmg = calcDamage(user, 100);
        System.out.println(user.getName() + " stomps with Smite Shockwave "+ " ! Enemy takes +25% dmg next turn.");
        target.takeDamage(dmg, attackerPen, src.characters.DamageType.MAGICAL);
        target.applyBuff(new DamageTakenUpDebuff(25, 1), "smite_stomp");
        incrementUse();
    }
}
