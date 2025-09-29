package src.skills;

import src.characters.Character;

public class LungeForward extends PhysicalSkill {
    public LungeForward() { super("Lunge Forward", 50); }

    @Override
    public void use(Character user, Character target) {
        int attackerPen = user.getPen() * ((30 / 100) + 1);
        int dmg = calcDamage(user, 70);
        target.takeDamage(dmg, attackerPen);
        System.out.println(user.getName() + " lunges forward dealing " + dmg + " damage (with 30% PEN)!");
        incrementUse();
    }
}
