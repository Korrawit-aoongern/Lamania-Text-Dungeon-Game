package src.skills;

import src.characters.Character;

public abstract class PhysicalSkill extends AbstractSkill {
    public PhysicalSkill(String name, int cost) {
        super(name, cost);
    }

    protected int calcDamage(Character user, double percentAtk, int flatPen) {
        double scaled = user.getAtk() * (percentAtk / 100.0) * getMultiplier();
        int dmg = (int)scaled;
        // PEN logic: flatPen is additional penetration applied
        return Math.max(1, dmg + flatPen);
    }
}
