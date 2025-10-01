package src.characters;

public class Enemy extends Character {
    private int zeroDamageStreak = 0; // consecutive turns dealing 0 damage

    public Enemy(String name, int level, int hp, int sp, int atk, int def, int mag, int pen) {
        super(name, level, hp, sp, atk, def, mag, pen);
        // All enemies start with Single Slash as a fallback skill
        try {
            addSkill(new src.skills.SingleSlash());
        } catch (Exception ignored) {}
    }

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
        int baseSkillChance = 15;

        if (spNow < 8) baseGuardChance += 15;
        if (hpRatio < 0.4) baseGuardChance += 15;

        int total = baseAttackChance + baseGuardChance + baseSkillChance;
        int roll = rand.nextInt(total);

        if (roll < baseAttackChance) {
            // Attack
            System.out.println(getName() + " attacks!");
            int dealt = opponent.takeDamage(atk, pen, DamageType.PHYSICAL);
            if (dealt == 0) zeroDamageStreak++; else zeroDamageStreak = 0;
        } else if (roll < baseAttackChance + baseGuardChance) {
            // Guard
            System.out.println(getName() + " guards!");
            src.status.Defbuff db = new src.status.Defbuff(1, 30);
            applyBuff(db, "guard");
            // guarding doesn't deal damage; count as 0-damage turn
            zeroDamageStreak++;
        } else {
            // Use skill if possible
            var skills = getSkills();
            // pick usable skills (cost <= sp)
            java.util.List<Integer> usable = new java.util.ArrayList<>();
            for (int i = 0; i < skills.size(); i++) {
                if (skills.get(i).getCost() <= spNow) usable.add(i);
            }
            if (!usable.isEmpty()) {
                int pick = usable.get(rand.nextInt(usable.size()));
                var skill = skills.get(pick);
                int beforeHp = opponent.getHp();
                consumeSp(skill.getCost());
                skill.use(this, opponent);
                int afterHp = opponent.getHp();
                int dealt = Math.max(0, beforeHp - afterHp);
                if (dealt == 0) zeroDamageStreak++; else zeroDamageStreak = 0;
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
}
