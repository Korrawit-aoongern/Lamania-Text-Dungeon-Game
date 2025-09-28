package src.skills;

import src.characters.Character;

class AstralBlade extends MagicalSkill {
    public AstralBlade() { super("Astral Blade", 202); }

    @Override
    public void use(Character user, Character target) {
        int dmg = target.getHp() / 2;
        target.takeDamage(dmg);
        System.out.println(user.getName() + " slashes with Astral Blade dealing " + dmg + " (50% of target HP)!");
        incrementUse();
    }
}
