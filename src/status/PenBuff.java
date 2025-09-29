package src.status;

import src.characters.Character;

public class PenBuff extends Buff {
    private int percentage;

    public PenBuff(int duration, int percentage) {
        super("PEN Buff", duration, false);
        this.percentage = percentage;
    }

    @Override
    public void apply(Character target) {
        target.modifyPen(percentage);
        System.out.println(target.getName() + " gains +" + percentage + "% PEN for " + duration + " turns.");
    }

    @Override
    public void remove(Character target) {
        target.modifyPen(-percentage);
        System.out.println(target.getName() + "'s PEN buff expired.");
    }
}
