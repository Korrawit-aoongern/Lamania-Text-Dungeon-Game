package src.status;

import src.characters.Character;

public class StunDebuff extends Buff {
    public StunDebuff(int duration) { super("Stunned", duration, true); }

    @Override
    public void apply(Character target) {
        target.setStunned(true);
        System.out.println(target.getName() + " is stunned for " + duration + " turns!");
    }

    @Override
    public void remove(Character target) {
        target.setStunned(false);
        System.out.println(target.getName() + " is no longer stunned.");
    }
}
