package src.status;

import src.characters.Character;

public class MagBuff extends Buff {
    private int percentage;
    private int appliedAmount;

    public MagBuff(int duration, int percentage) {
        super("MAG Buff", duration, false);
        this.percentage = percentage;
    }

    @Override
    public void apply(Character target) {
        appliedAmount = target.getMag() * percentage / 100;
        target.modifyMag(appliedAmount);
        System.out.println(target.getName() + " gains +" + appliedAmount + " MAG (" + percentage + "%) for " + duration + " turns.");
    }

    @Override
    public void remove(Character target) {
        target.modifyMag(-appliedAmount);
        System.out.println(target.getName() + "'s MAG buff expired.");
    }
}
