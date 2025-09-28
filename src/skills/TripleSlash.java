package src.skills;

import src.characters.Character;

class TripleSlash extends PhysicalSkill {
    public TripleSlash() { super("Triple Slash", 34); }

    @Override
    public void use(Character user, Character target) {
        int d1 = calcDamage(user, 40, 0);
        int d2 = calcDamage(user, 35, 0);
        int d3 = calcDamage(user, 30, 0);
        target.takeDamage(d1); target.takeDamage(d2); target.takeDamage(d3);
        System.out.println(user.getName() + " uses Triple Slash dealing " + d1 + " + " + d2 + " + " + d3 + " damage!");
        incrementUse();
    }
}
