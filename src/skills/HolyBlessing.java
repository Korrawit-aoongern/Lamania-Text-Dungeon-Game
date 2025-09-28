package src.skills;

import src.characters.Character;

class HolyBlessing extends MagicalSkill {
    public HolyBlessing() { super("Holy Blessing", 64); }

    @Override
    public void use(Character user, Character target) {
        System.out.println(user.getName() + " casts Holy Blessing! DEF +30% (2 turns), ATK +20 (1 turn). ");
        incrementUse();
    }
}
