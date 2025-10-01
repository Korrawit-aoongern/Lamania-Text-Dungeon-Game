package src.skills;

import src.characters.Character;
import src.status.AtkDebuff;
import src.status.DefDebuff;

public class Devour extends MagicalSkill {
    public Devour() { super("Devour", 42); }

    @Override
    public void use(Character user, Character target) {
        System.out.println(user.getName() + " uses Devour: Target ATK -20% and DEF -45% for 2 turns.");
        target.applyBuff(new AtkDebuff(20, 2), "devour");
        target.applyBuff(new DefDebuff(45, 2), "devour");
        incrementUse();
    }
}
