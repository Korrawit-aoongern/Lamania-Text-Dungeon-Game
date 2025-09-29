package src.status;

import src.characters.Character;

public class PoisonDebuff extends Buff {
    private int damagePerTurn;
    public PoisonDebuff(int duration, int damagePerTurn) {
        super("Poison", duration, true);
        this.damagePerTurn = damagePerTurn;
    }

    @Override
    public void apply(Character target) {
        System.out.println(target.getName() + " is poisoned for " + duration + " turns!");
    }

    @Override
    public void tick(Character target) {
        super.tick(target);
        target.takeDamage(damagePerTurn, 0);
        System.out.println(target.getName() + " takes " + damagePerTurn + " poison damage!");
    }

    @Override
    public void remove(Character target) {
        System.out.println(target.getName() + "'s poison wore off.");
    }
}
