package src.skills;

import src.characters.Character;
import src.status.AtkDebuff;

public class SlimeSplit extends MagicalSkill {
    public SlimeSplit() { super("Slime Split", 5); }

    @Override
    public void use(Character user, Character target) {
        
        System.out.println(user.getName() + " uses Slime Split! Target ATK -10% for 2 turns.");
        target.applyBuff(new AtkDebuff(10, 2), "slime_split");
        incrementUse();
    }
}
