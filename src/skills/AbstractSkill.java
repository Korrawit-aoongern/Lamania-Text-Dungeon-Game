package src.skills;

import src.characters.Character;
import src.status.Buff;
import src.status.PoisonWeaponBuff;

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
        // For buff-only skills, mastery should reduce SP cost (they don't scale with damage multiplier the same way)
        if (isBuffSkill()) {
            int reduced = baseCost - extraCost;
            return Math.max(1, reduced);
        }
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

    /**
     * Override in skills that are buff-only (apply buffs/debuffs or heal) so mastery
     * reduces SP cost instead of increasing it. Default is false.
     */
    protected boolean isBuffSkill() {
        return false;
    }

    /**
     * Invoke on-hit effects from any active buffs on the user (e.g., PoisonWeaponBuff).
     * Called by physical skills after they deal damage so follow-up effects can apply.
     */
    protected void invokeOnHitEffects(Character user, Character target) {
        if (user == null || target == null) return;
        // iterate over a snapshot copy so buffs can safely remove themselves during onHit
        java.util.List<Buff> snapshot = new java.util.ArrayList<>(user.getBuffManager().getActiveBuffs());
        for (Buff b : snapshot) {
            if (b instanceof PoisonWeaponBuff) {
                ((PoisonWeaponBuff)b).onHit(user, target);
            }
        }
    }

    @Override
    public abstract void use(Character user, Character target);
}
