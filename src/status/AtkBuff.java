package src.status;

import src.characters.Character;

public class AtkBuff extends Buff {
    private int flatIncrease;
    public AtkBuff(int duration, int flatIncrease) {
        super("ATK Buff", duration, false);
        this.flatIncrease = flatIncrease;
    }

    @Override
    public void apply(Character target) {
        target.modifyAtk(flatIncrease);
        System.out.println(target.getName() + " gains +" + flatIncrease + " ATK for " + duration + " turns.");
    }

    @Override
    public void remove(Character target) {
        target.modifyAtk(-flatIncrease);
        System.out.println(target.getName() + "'s ATK buff expired.");
    }
}
