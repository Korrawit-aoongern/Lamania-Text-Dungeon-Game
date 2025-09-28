package src.skills;

import src.characters.Character;

class DownwardSlash extends PhysicalSkill {
    public DownwardSlash() { super("Downward Slash", 27); }

    @Override
    public void use(Character user, Character target) {
        int dmg = calcDamage(user, 78, 0);
        target.takeDamage(dmg);
        System.out.println(user.getName() + " performs Downward Slash dealing " + dmg + " damage! DEF reduced by 10% for 2 turns.");
        incrementUse();
    }
}
