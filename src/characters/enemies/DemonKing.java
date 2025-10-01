package src.characters.enemies;

import src.characters.Enemy;

public class DemonKing extends Enemy {
    public DemonKing() {
        // Boss: very high stats, low PEN (as specified). Level will be adjusted by spawn logic.
    super("The Demon King", 30, 2000, 900, 307, 224, 120, 10);
        try {
            addSkill(new src.skills.DownwardSlash());
            addSkill(new src.skills.TripleSlash());
            addSkill(new src.skills.AstralBlade());
            addSkill(new src.skills.ThunderStrike());
            addSkill(new src.skills.WaterSoothing());
        } catch (Exception ignored) {}
        this.role = "boss";
        this.skillPriorityBias = 2.5;
        setExpReward(500);
    }

    @Override
    public int getMaxSkillSlots() { return 6; }
}
