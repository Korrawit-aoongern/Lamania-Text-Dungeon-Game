package src.skills;

import src.characters.Character;

public abstract class MagicalSkill extends AbstractSkill {
    public MagicalSkill(String name, int cost) {
        super(name, cost);
    }

    protected int calcDamage(Character user, double percentMag) {
        double scaled = user.getMag() * (percentMag / 100.0) * getMultiplier();
        return Math.max(1, (int)scaled);
    }

    protected int calcHybridDamage(Character user, double percentAtk, double percentMag) {
        double scaled = (user.getAtk() * (percentAtk / 100.0)) + (user.getMag() * (percentMag / 100.0));
        scaled *= getMultiplier();
        return Math.max(1, (int)scaled);
    }
}
// Skill classes moved to their own files to fix file size and avoid stray tokens