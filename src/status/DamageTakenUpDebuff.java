package src.status;

import src.characters.Character;

public class DamageTakenUpDebuff extends Buff {
    private double percent;

    public DamageTakenUpDebuff(double percent, int duration) {
        super("Vulnerable", duration, true);
        this.percent = percent;
    }

    @Override
    public void apply(Character target) {
        target.setDamageTakenModifier(target.getDamageTakenModifier() + percent / 100.0);
        System.out.println(target.getName() + " takes +" + percent + "% more damage for " + duration + " turn(s)!");
    }

    @Override
    public void remove(Character target) {
        target.setDamageTakenModifier(target.getDamageTakenModifier() - percent / 100.0);
        System.out.println(target.getName() + "'s vulnerability has faded.");
    }
}
