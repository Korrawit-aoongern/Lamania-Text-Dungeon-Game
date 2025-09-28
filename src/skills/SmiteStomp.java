package src.skills;

import src.characters.Character;

class SmiteStomp extends MagicalSkill {
    public SmiteStomp() { super("Smite Stomp", 95); }

    @Override
    public void use(Character user, Character target) {
        int dmg = calcDamage(user, 100);
        target.takeDamage(dmg);
        System.out.println(user.getName() + " stomps with Smite Shockwave for " + dmg + " dmg! Enemy takes +25% dmg next turn.");
        incrementUse();
    }
}
