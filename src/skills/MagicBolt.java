package src.skills;

import src.characters.Character;

public class MagicBolt extends MagicalSkill {
    public MagicBolt() { super("Magic Bolt", 15); }

    @Override
    public void use(Character user, Character target) {
        int dmg = calcDamage(user, 50); // 50% of MAG
        System.out.println(user.getName() + " casts Magic Bolt!");
        target.takeDamage(dmg, user.getPen(), src.characters.DamageType.MAGICAL);
        incrementUse();
    }
}
