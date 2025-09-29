package src.skills;

import src.characters.Character;
import src.status.AtkBuff;
import src.status.Defbuff;

public class HolyBlessing extends MagicalSkill {
    public HolyBlessing() { super("Holy Blessing", 64); }

    @Override
    public void use(Character user, Character target) {
        System.out.println(user.getName() + " casts Holy Blessing! DEF +30% (2 turns), ATK +20 (1 turn). ");
        user.applyBuff(new Defbuff(2, 30)); // 30% DEF for 2 turns
        user.applyBuff(new AtkBuff(1, 20)); // +20 ATK for 1 turn
        incrementUse();
    }
}
