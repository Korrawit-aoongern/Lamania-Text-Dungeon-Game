package src.skills;

import src.characters.Character;

class DoubleSlash extends PhysicalSkill {
    public DoubleSlash() { super("Double Slash", 12); }

    @Override
    public void use(Character user, Character target) {
        int dmg1 = calcDamage(user, 30, 0);
        int dmg2 = calcDamage(user, 30, 0);
        target.takeDamage(dmg1);
        target.takeDamage(dmg2);
        System.out.println(user.getName() + " uses Double Slash dealing " + dmg1 + " + " + dmg2 + " damage!");
        incrementUse();
    }
}
