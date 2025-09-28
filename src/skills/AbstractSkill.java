package src.skills;

import src.characters.Character;

public abstract class AbstractSkill implements Skill {
    protected String name;
    protected int baseCost;
    protected int useCount;
    protected double masteryMultiplier;

    public AbstractSkill(String name, int cost) {
        this.name = name;
        this.baseCost = cost;
        this.useCount = 0;
        this.masteryMultiplier = 1.0;
    }

    @Override
    public String getName() { return name; }

    @Override
    public int getCost() {
        // cost scales with mastery tiers
        int extraCost = 0;
        if (useCount >= 25) extraCost = 30;
        else if (useCount >= 15) extraCost = 20;
        else if (useCount >= 5)  extraCost = 10;
        return baseCost + extraCost;
    }

    protected double getMultiplier() {
        // damage/heal scaling with mastery tiers
        if (useCount >= 25) return 1.5;   // +50%
        if (useCount >= 15) return 1.2;   // +20%
        if (useCount >= 5)  return 1.05;  // +5%
        return 1.0;
    }

    protected void incrementUse() {
        useCount++;
    }

    @Override
    public abstract void use(Character user, Character target);
}
