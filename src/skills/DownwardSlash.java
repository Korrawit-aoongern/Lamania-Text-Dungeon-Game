package src.skills;

import src.characters.Character;
import src.status.DefDebuff;

public class DownwardSlash extends PhysicalSkill {
    public DownwardSlash() { super("Downward Slash", 27); }

    @Override
    public void use(Character user, Character target) {
        int attackerPen = user.getPen();
        int dmg = calcDamage(user, 78);
        target.takeDamage(dmg, attackerPen);
    target.applyBuff(new DefDebuff(10, 1), "downward_slash");
        System.out.println(user.getName() + " performs Downward Slash dealing " + dmg + " damage! DEF reduced by 10% for 1 turns.");
        incrementUse();
    }
}
