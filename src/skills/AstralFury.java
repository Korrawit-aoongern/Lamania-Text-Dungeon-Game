package src.skills;

import src.characters.Character;

class AstralFury extends MagicalSkill {
    public AstralFury() { super("Astral Fury", 220); }

    @Override
    public void use(Character user, Character target) {
        System.out.println(user.getName() + " unleashes Astral Fury! ATK, HP, DEF, PEN increased by 100% of MAG for 2 turns!");
        incrementUse();
    }
}
