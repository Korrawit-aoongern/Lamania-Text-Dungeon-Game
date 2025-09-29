package src.status;

import src.characters.Character;

public class HpBuff extends Buff {
    private int bonusHp;

    public HpBuff(int duration, int bonusHp) {
        super("HP Boost", duration, false);
        this.bonusHp = bonusHp;
    }

    @Override
    public void apply(Character target) {
        target.setMaxHp(target.getMaxHp() + bonusHp);
        target.setHp(target.getHp() + bonusHp); // heal into bonus
        System.out.println(target.getName() + "'s HP increased by " + bonusHp + " for " + duration + " turns!");
    }

    @Override
    public void remove(Character target) {
        target.setMaxHp(target.getMaxHp() - bonusHp);
        if (target.getHp() > target.getMaxHp()) {
            target.setHp(target.getMaxHp()); // clamp
        }
        System.out.println(target.getName() + "'s HP boost has expired.");
    }
}

