package src.skills;

import src.characters.Character;

class WaterSoothing extends MagicalSkill {
    public WaterSoothing() { super("Water Soothing", 60); }

    @Override
    public void use(Character user, Character target) {
        int heal = (int)(user.getMag() * 0.40 * getMultiplier());
        // Healing self
        user.heal(heal);
        System.out.println(user.getName() + " uses Water Soothing healing " + heal + " HP and gains +20 MAG for 2 turns.");
        incrementUse();
    }
}
