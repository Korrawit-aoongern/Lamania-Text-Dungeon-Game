package src.status;

import src.characters.Character;

public class DamageUpBuff extends Buff {
    private int percentage;

    public DamageUpBuff(int duration, int percentage) {
        super("Damage Up", duration, false);
        this.percentage = percentage;
    }

    @Override
    public void apply(Character target) {
        target.modifyDamageMultiplier(percentage);
        System.out.println(target.getName() + " deals +" + percentage + "% extra damage for " + duration + " turns!");
    }

    @Override
    public void remove(Character target) {
        target.modifyDamageMultiplier(-percentage);
        System.out.println(target.getName() + "'s damage boost expired.");
    }
}
