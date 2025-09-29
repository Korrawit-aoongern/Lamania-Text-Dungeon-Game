package src.skills;

import src.characters.Character;
import src.status.Defbuff;

public class TripleShadowStep extends PhysicalSkill {
    public TripleShadowStep() { super("Triple Shadow Step", 45); }

    @Override
    public void use(Character user, Character target) {
        int attackerPen = user.getPen();
        int d = calcDamage(user, 33);
        target.takeDamage(d, attackerPen);
        target.takeDamage(d, attackerPen);
        target.takeDamage(d, attackerPen);
        System.out.println(user.getName() + " slices swiftly with Triple Shadow Step, each hit " + d + " dmg ! DEF increased 10% for 1 turn.");
        user.applyBuff(new Defbuff(1, 10));
        incrementUse();
    }
}
