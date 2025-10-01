package src.status;

import src.characters.Character;

public class PoisonWeaponBuff extends Buff {
    private int remainingUses; // number of skills it applies to
    private int percentMag;    // % of MAG follow-up damage
    private int dotDuration;   // duration of poison DoT

    public PoisonWeaponBuff(int skills, int percentMag, int dotDuration) {
        super("Poison Infuse", skills, false);
        this.remainingUses = skills;
        this.percentMag = percentMag;
        this.dotDuration = dotDuration;
    }

    @Override
    public void apply(Character target) {
        System.out.println(target.getName() + " infused their weapon with poison for " + remainingUses + " skill(s).");
    }

    @Override
    public void remove(Character target) {
        System.out.println("Poison infusion has worn off.");
    }

    // hook for combat system to apply on-hit effect
    public void onHit(Character user, Character target) {
        int poisonDmg = (int)(user.getMag() * (percentMag / 100.0));
        System.out.println("Poison lash deals " + poisonDmg + " extra damage!");
    target.takeDamage(poisonDmg, 0, src.characters.DamageType.PURE);
        target.getBuffManager().addBuff(target, new PoisonDebuff(poisonDmg, dotDuration));

        remainingUses--;
        if (remainingUses <= 0) {
            duration = 0; // expire
            remove(user);
        }
    }
}
