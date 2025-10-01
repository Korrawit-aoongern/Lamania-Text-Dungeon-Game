package src.skills;

import src.characters.Character;
import src.status.AtkBuff;
import src.status.Defbuff;
import src.status.HpBuff;
import src.status.PenBuff;

public class AstralFury extends MagicalSkill {
    public AstralFury() { super("Astral Fury", 220); }

    @Override
    public void use(Character user, Character target) {
        int bonus = user.getMag();

        System.out.println(user.getName() + " unleashes Astral Fury! ATK, HP, DEF, PEN increased by 100% of MAG for 3 turns!");
        user.applyBuff(new AtkBuff(3, bonus), "astral_fury");
        user.applyBuff(new HpBuff(3, bonus), "astral_fury");
        user.applyBuff(new Defbuff(3, bonus, false), "astral_fury");
        user.applyBuff(new PenBuff(3, bonus), "astral_fury");
        incrementUse();
    }
}
