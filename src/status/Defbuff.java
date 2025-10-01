package src.status;

import src.characters.Character;

public class Defbuff extends Buff {
    private final boolean isPercentage;
    private final int value; // percentage (if isPercentage) or flat amount
    private int appliedAmount = 0;

    // Percentage constructor (e.g., new Defbuff(3, 30) => +30% DEF for 3 turns)
    public Defbuff(int duration, int percentage) {
        this(duration, percentage, true);
    }

    // General constructor: set isPercentage = true for percent, false for flat
    public Defbuff(int duration, int value, boolean isPercentage) {
        super("DEF Buff", duration, false);
        this.isPercentage = isPercentage;
        this.value = value;
    }

    // Convenience factory for flat amount: Defbuff.flat(3, 5) => +5 DEF for 3 turns
    public static Defbuff flat(int duration, int flatAmount) {
        return new Defbuff(duration, flatAmount, false);
    }

    @Override
    public void apply(Character target) {
        if (isPercentage) {
            // Use floating calculation and ensure at least +1 DEF for low-DEF targets
            appliedAmount = (int)Math.max(1, Math.round(target.getDef() * (value / 100.0)));
            target.modifyDef(appliedAmount);
            System.out.println(target.getName() + " gains +" + appliedAmount + " DEF (" + value + "%) for " + duration + " turns.");
        } else {
            appliedAmount = value;
            target.modifyDef(appliedAmount);
            System.out.println(target.getName() + " gains +" + appliedAmount + " DEF (flat) for " + duration + " turns.");
        }
    }

    @Override
    public void remove(Character target) {
        if (appliedAmount != 0) {
            target.modifyDef(-appliedAmount);
        }
        System.out.println(target.getName() + "'s DEF buff expired.");
    }
}
