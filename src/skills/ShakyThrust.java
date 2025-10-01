package src.skills;

import src.characters.Character;
import src.characters.DamageType;

public class ShakyThrust extends PhysicalSkill {
    public ShakyThrust() {
        super("Shaky Thrust", 24);
    }

    @Override
    public void use(Character user, Character target) {
        int base = calcDamage(user, 34);
        int penBonus = (int) Math.round(user.getPen() * 0.12);
        int totalPen = user.getPen() + penBonus;
        int dealt = target.takeDamage(base, totalPen, DamageType.PHYSICAL);
        System.out.println(user.getName() + " uses Shaky Thrust and deals " + dealt + " damage.");
    }
}