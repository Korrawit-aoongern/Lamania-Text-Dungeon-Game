package src.skills;

import src.characters.Character;
import src.status.DefDebuff;

public class PlagueSplit extends MagicalSkill {
    public PlagueSplit() { super("Plague Split", 3); }

    @Override
    public void use(Character user, Character target) {
    target.applyBuff(new DefDebuff(35, 3), "plague_split");
        System.out.println(user.getName() + " casts Plague Split! Target DEF -35% (3 turns). ");
        incrementUse();
    }
}
