package src.status;

import src.characters.Character;

public class ImmunityBuff extends Buff {
    public ImmunityBuff(int duration) {
        super("Immunity", duration, false);
    }

    @Override
    public void apply(Character target) {
        target.setImmune(true);
        System.out.println(target.getName() + " is immune to debuffs for " + duration + " turns!");
    }

    @Override
    public void remove(Character target) {
        target.setImmune(false);
        System.out.println(target.getName() + "'s immunity has ended.");
    }
}
