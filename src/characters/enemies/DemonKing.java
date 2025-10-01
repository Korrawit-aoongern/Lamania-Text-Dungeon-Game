package src.characters.enemies;

import src.characters.Enemy;

public class DemonKing extends Enemy {
    public DemonKing() {
        // Boss: very high stats, low PEN (as specified). Level will be adjusted by spawn logic.
        super("The Demon King", 30, 2000, 200, 307, 224, 120, 10);
    }

    @Override
    public int getMaxSkillSlots() { return 6; }
}
