package src.skills;

import src.characters.Character;
import src.status.StunDebuff;

public class PummelStrike extends AbstractSkill {
    public PummelStrike() { super("Pummel Strike", 57); }

    @Override
    public void use(Character user, Character target) {
        // No damage, only stun for 2 turns
        target.applyBuff(new StunDebuff(2), "pummel_strike");
        System.out.println(user.getName() + " uses Pummel Strike and stuns " + target.getName() + " for 2 turns!");
    }
}
