package src.skills;

import src.characters.Character;
public class Heal extends AbstractSkill {
    public Heal() { super("Heal", 15); }

    @Override
    public void use(Character user, Character target) {
        // Heal amount: 10% of target's max HP, scaled by mastery multiplier
        int baseHeal = (int)Math.max(1, Math.round(target.getMaxHp() * 0.10));
        double scaled = baseHeal * getMultiplier();
        int healAmount = Math.max(1, (int)Math.round(scaled));

        System.out.println(user.getName() + " uses Heal on " + target.getName() + "!");
        target.heal(healAmount);
        System.out.println(target.getName() + " recovers " + healAmount + " HP. (" + target.getHp() + "/" + target.getMaxHp() + ")");
        incrementUse();
    }

    @Override
    protected boolean isBuffSkill() { return true; }
}
