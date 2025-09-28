package src.skills;

import src.characters.Character;

public class SingleSlash extends PhysicalSkill {
    public SingleSlash() { super("Single Slash", 3); }

    @Override
    public void use(Character user, Character target) {
        int dmg = calcDamage(user, 35, 0);
        target.takeDamage(dmg);
        System.out.println(user.getName() + " uses Single Slash dealing " + dmg + " damage!");
        incrementUse();
    }
}
