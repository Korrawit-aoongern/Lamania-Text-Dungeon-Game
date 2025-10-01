package src.skills;

import src.characters.Character;
import src.status.MagBuff;

public class WaterSoothing extends MagicalSkill {
    public WaterSoothing() { super("Water Soothing", 5); }

    @Override
    public void use(Character user, Character target) {
        int heal = (int)(user.getMag() * 0.65 * getMultiplier());
        // Healing self
        System.out.println(user.getName() + " uses Water Soothing healing " + heal + " HP and gains 20% MAG for 2 turns.");
        user.applyBuff(new MagBuff(2, 25), "water_soothing");user.heal(heal);
        incrementUse();
    }
}
