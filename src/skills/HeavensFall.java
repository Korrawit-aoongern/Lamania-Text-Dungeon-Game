package src.skills;

import src.characters.Character;

class HeavensFall extends MagicalSkill {
    public HeavensFall() { super("Heaven's Fall", 133); }

    @Override
    public void use(Character user, Character target) {
        int dmg = calcDamage(user, 200);
        target.takeDamage(dmg);
        System.out.println(user.getName() + " summons Heaven's Fall crushing foe for " + dmg + " dmg! Enemy stunned 1 turn.");
        incrementUse();
    }
}
