package src.skills;

import src.characters.Character;

class ThunderStrike extends MagicalSkill {
    public ThunderStrike() { super("Thunder Strike", 85); }

    @Override
    public void use(Character user, Character target) {
        int dmg = calcDamage(user, 178);
        target.takeDamage(dmg);
        System.out.println(user.getName() + " calls Thunder Strike dealing " + dmg + " magic damage!");
        incrementUse();
    }
}
