package src.skills;

import src.characters.Character;

public class FlameStrike extends MagicalSkill {
    public FlameStrike() { super("Flame Strike", 37); }

    @Override
    public void use(Character user, Character target) {
        int dmg = calcHybridDamage(user, 35, 75);
        target.takeDamage(dmg);
        System.out.println(user.getName() + " uses Flame Strike dealing " + dmg + " hybrid damage!");
        incrementUse();
    }
}
