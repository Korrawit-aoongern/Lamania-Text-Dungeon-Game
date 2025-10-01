package src.skills;

import src.characters.Character;
import src.status.Defbuff;

public class ShieldUp extends AbstractSkill {
    public ShieldUp() { super("Shield Up", 20); }

    @Override
    public void use(Character user, Character target) {
        user.applyBuff(new Defbuff(1, 50), "shield_up");
        System.out.println(user.getName() + " uses Shield Up, increasing DEF by 50% for 1 turn.");
    }

    @Override
    protected boolean isBuffSkill() { return true; }
}
