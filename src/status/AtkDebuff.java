package src.status;

import src.characters.Character;

public class AtkDebuff extends Buff {
    private int percent;

    public AtkDebuff(int percent, int duration) {
        super("ATK Down", duration, true);
        this.percent = percent;
    }

    @Override
    public void apply(Character target) {
        int reduce = (target.getAtk() * percent) / 100;
        target.modifyAtk(-reduce);
        System.out.println(target.getName() + "'s Attack decreased by " + percent + "% for " + duration + " turns!");
    }

    @Override
    public void remove(Character target) {
        int restore = (int)Math.round((double)target.getAtk() * percent / (100 - percent));
        // attempting to reverse change; simpler: no perfect reversal available for dynamic stacks
        // We'll restore proportionally by increasing back by same reduce percentage of current atk
        int toRestore = (target.getAtk() * percent) / (100 - percent);
        target.modifyAtk(toRestore);
        System.out.println(target.getName() + "'s Attack returned to normal.");
    }
}
