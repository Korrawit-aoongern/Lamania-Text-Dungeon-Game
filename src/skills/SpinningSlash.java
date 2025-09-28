package src.skills;

import src.characters.Character;

class SpinningSlash extends PhysicalSkill {
    public SpinningSlash() { super("Spinning Slash", 21); }

    @Override
    public void use(Character user, Character target) {
        int dmg = calcDamage(user, 65, 0);
        target.takeDamage(dmg);
        System.out.println(user.getName() + " spins 360° with Spinning Slash dealing " + dmg + " damage!");
        incrementUse();
    }
}
