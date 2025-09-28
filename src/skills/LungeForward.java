package src.skills;

import src.characters.Character;

class LungeForward extends PhysicalSkill {
    public LungeForward() { super("Lunge Forward", 50); }

    @Override
    public void use(Character user, Character target) {
        int dmg = calcDamage(user, 70, 30);
        target.takeDamage(dmg);
        System.out.println(user.getName() + " lunges forward dealing " + dmg + " damage (with 30% PEN)!");
        incrementUse();
    }
}
