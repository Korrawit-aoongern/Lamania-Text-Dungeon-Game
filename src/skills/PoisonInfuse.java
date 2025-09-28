package src.skills;

import src.characters.Character;

class PoisonInfuse extends MagicalSkill {
    public PoisonInfuse() { super("Poison Infuse", 84); }

    @Override
    public void use(Character user, Character target) {
        System.out.println(user.getName() + " infuses blade with poison! Physical skills deal +45% MAG follow-up dmg for 3 turns.");
        incrementUse();
    }
}
