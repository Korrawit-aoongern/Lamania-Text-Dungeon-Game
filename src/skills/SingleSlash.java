package src.skills;

import src.characters.Character;
import src.status.Defbuff;
import src.status.StunDebuff;

public class SingleSlash extends PhysicalSkill {
    public SingleSlash() { super("Single Slash", 3); }

    @Override
    public void use(Character user, Character target) {
        int dmg = calcDamage(user, 35);
        int attackerPen = user.getPen(); // skill flatPen = 0 here
        target.applyBuff(new StunDebuff(2), "heavens_fall");
        System.out.println(user.getName() + " uses Single Slash dealing " + dmg + " damage!");
        target.takeDamage(dmg, attackerPen);
        incrementUse();
    }
}
