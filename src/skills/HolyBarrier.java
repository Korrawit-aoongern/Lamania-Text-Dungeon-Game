package src.skills;

import src.characters.Character;

class HolyBarrier extends MagicalSkill {
    public HolyBarrier() { super("Holy Barrier", 155); }

    @Override
    public void use(Character user, Character target) {
        System.out.println(user.getName() + " summons Holy Barrier! Damage nullified for 1 turn.");
        incrementUse();
    }
}
