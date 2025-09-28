package src.skills;

import src.characters.Character;

class PlagueSplit extends MagicalSkill {
    public PlagueSplit() { super("Plague Split", 80); }

    @Override
    public void use(Character user, Character target) {
        System.out.println(user.getName() + " casts Plague Split! Target DEF -25% (3 turns). ");
        incrementUse();
    }
}
