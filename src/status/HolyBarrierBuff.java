package src.status;

import src.characters.Character;

public class HolyBarrierBuff extends Buff {
    public HolyBarrierBuff(int duration) {
        super("Holy Barrier", duration, false);
    }

    @Override
    public void apply(Character target) {
        target.setBarrierActive(true);
        System.out.println(target.getName() + " is shielded by Holy Barrier!");
    }

    @Override
    public void remove(Character target) {
        target.setBarrierActive(false);
        System.out.println("Holy Barrier on " + target.getName() + " has faded.");
    }
}