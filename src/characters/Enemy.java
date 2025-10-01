package src.characters;

public class Enemy extends Character {
    private int zeroDamageStreak = 0; // consecutive turns dealing 0 damage
    private Integer lastSkillUsedIndex = null; // prevent repeating the same skill twice
    // Role-based AI modifiers
    protected String role = "grunt"; // "mage", "warrior", "boss"
    protected double skillPriorityBias = 1.0; // multiplies base skill chance
    protected int expReward = 0;

    public Enemy(String name, int level, int hp, int sp, int atk, int def, int mag, int pen) {
        super(name, level, hp, sp, atk, def, mag, pen);
        // All enemies start with Single Slash as a fallback skill
        try {
            addSkill(new src.skills.SingleSlash());
        } catch (Exception ignored) {}
    }

    public int getExpReward() { return expReward; }
    public void setExpReward(int xp) { this.expReward = xp; }

    @Override
    public int getMaxSkillSlots() { return 4; }

    @Override
    public void takeTurn(Character opponent) {
        // Legacy: delegate to AI act for default behavior
        act(opponent, new java.util.Random());
    }

    /**
     * Enemy AI action. Returns true if the enemy fled.
     */
    public boolean act(Character opponent, java.util.Random rand) {
        // Basic behavioral heuristics:
        // - Mostly attack
        // - Guard more often when SP low or HP low
        // - Use skills if available and SP allows
        // - Attempt to flee if HP < 5% and zeroDamageStreak >= 3

        if (isStunned()) {
            System.out.println(getName() + " is stunned and cannot act this turn!");
            return false;
        }

        double hpRatio = (double)getHp() / getMaxHp();
        int spNow = getSp();

    int baseAttackChance = 70;
    int baseGuardChance = 15;
    int baseSkillChance = (int)Math.round(15 * skillPriorityBias);

        if (spNow < 8) baseGuardChance += 15;
        if (hpRatio < 0.4) baseGuardChance += 15;

        int total = baseAttackChance + baseGuardChance + baseSkillChance;
        int roll = rand.nextInt(total);

        if (roll < baseAttackChance) {
            // Attack
            System.out.println(getName() + " attacks!");
            int dealt = opponent.takeDamage(atk, pen, DamageType.PHYSICAL);
            if (dealt == 0) zeroDamageStreak++; else zeroDamageStreak = 0;
            lastSkillUsedIndex = null; // reset skill repetition when doing a basic attack
        } else if (roll < baseAttackChance + baseGuardChance) {
            // Guard
            System.out.println(getName() + " guards!");
            src.status.Defbuff db = new src.status.Defbuff(1, 30);
            applyBuff(db, "guard");
            // guarding doesn't deal damage; count as 0-damage turn
            zeroDamageStreak++;
            lastSkillUsedIndex = null;
        } else {
            // Use skill if possible
            var skills = getSkills();
            // pick usable skills (cost <= sp)
            java.util.List<Integer> usable = new java.util.ArrayList<>();
            for (int i = 0; i < skills.size(); i++) {
                if (skills.get(i).getCost() <= spNow) usable.add(i);
            }
                if (!usable.isEmpty()) {
                // Prefer higher-cost skills but avoid repeating the same skill twice
                // Weight choices by cost
                int totalWeight = 0;
                int[] weights = new int[usable.size()];
                for (int i = 0; i < usable.size(); i++) {
                    int idx = usable.get(i);
                    int cost = skills.get(idx).getCost();
                    int w = Math.max(1, cost / 10); // cost-based weight
                        // Give preference to caster-style enemies for magical skills
                        if ("mage".equals(role)) {
                            // boost weight for magical skills
                            if (skills.get(idx) instanceof src.skills.MagicalSkill) w *= 2;
                        }
                    // discourage picking the last used skill
                    if (lastSkillUsedIndex != null && lastSkillUsedIndex == idx) w = Math.max(1, w / 3);
                    weights[i] = w;
                    totalWeight += w;
                }
                int r = rand.nextInt(totalWeight);
                int chosenIndexInUsable = 0;
                int cum = 0;
                for (int i = 0; i < weights.length; i++) {
                    cum += weights[i];
                    if (r < cum) { chosenIndexInUsable = i; break; }
                }
                int pick = usable.get(chosenIndexInUsable);
                var skill = skills.get(pick);
                int beforeHp = opponent.getHp();
                consumeSp(skill.getCost());
                skill.use(this, opponent);
                int afterHp = opponent.getHp();
                int dealt = Math.max(0, beforeHp - afterHp);
                if (dealt == 0) zeroDamageStreak++; else zeroDamageStreak = 0;
                // remember which skill was used
                lastSkillUsedIndex = pick;
            } else {
                // fallback to attack
                System.out.println(getName() + " attacks!");
                int dealt = opponent.takeDamage(atk, pen, DamageType.PHYSICAL);
                if (dealt == 0) zeroDamageStreak++; else zeroDamageStreak = 0;
            }
        }

        // Flee check
        if ((double)getHp() / getMaxHp() < 0.05 && zeroDamageStreak >= 3) {
            System.out.println(getName() + " attempts to flee in desperation!");
            return true;
        }

        return false;
    }
}
