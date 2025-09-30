package src.skills;

import src.characters.Character;
import src.status.PoisonWeaponBuff;

public class PoisonInfuse extends MagicalSkill {
    public PoisonInfuse() { super("Poison Infuse", 10); }

    @Override
    public void use(Character user, Character target) {
    user.applyBuff(new PoisonWeaponBuff(3, 45, 1), "poison_infuse");
        System.out.println(user.getName() + " infuses blade with poison! Physical skills deal +45% MAG follow-up dmg for 3 skills use.");
        incrementUse();
    }
}
