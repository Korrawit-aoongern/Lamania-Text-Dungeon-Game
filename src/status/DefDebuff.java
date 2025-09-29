package src.status;

import src.characters.Character;

public class DefDebuff extends Buff {
    private int percent;

    public DefDebuff(int percent, int duration) {
        super("Defense Down", duration, true);
        this.percent = percent;
    }

    @Override
    public void apply(Character target) {
        int reduce = (target.getDef() * percent) / 100;
        target.setDef(target.getDef() - reduce);
        System.out.println(target.getName() + "'s Defense decreased by " + percent + "% for " + duration + " turns!");
    }

    @Override
    public void remove(Character target) {
        int restore = (target.getDef() * percent) / (100 - percent); // reverse calc
        target.setDef(target.getDef() + restore);
        System.out.println(target.getName() + "'s Defense returned to normal.");
    }
}

