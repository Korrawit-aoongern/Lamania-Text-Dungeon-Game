package src.skills;

import src.characters.Character;

class TripleShadowStep extends PhysicalSkill {
    public TripleShadowStep() { super("Triple Shadow Step", 45); }

    @Override
    public void use(Character user, Character target) {
        int d = calcDamage(user, 33, 0);
        target.takeDamage(d); target.takeDamage(d); target.takeDamage(d);
        System.out.println(user.getName() + " slices swiftly with Triple Shadow Step, each hit " + d + "! DEF increased 10% for 1 turn.");
        incrementUse();
    }
}
