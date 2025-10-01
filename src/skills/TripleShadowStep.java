package src.skills;

import src.characters.Character;
import src.status.Defbuff;

public class TripleShadowStep extends PhysicalSkill {
    public TripleShadowStep() { super("Triple Shadow Step", 45); }

    @Override
    public void use(Character user, Character target) {
        int attackerPen = user.getPen();
        int d = calcDamage(user, 33);

        System.out.println(user.getName() + " slices swiftly with Triple Shadow Step, " + "DEF increased 80% for 1 turn.");
        user.applyBuff(new Defbuff(1, 80), "triple_shadow_step");
        target.takeDamage(d, attackerPen, src.characters.DamageType.PHYSICAL);
        target.takeDamage(d, attackerPen, src.characters.DamageType.PHYSICAL);
        target.takeDamage(d, attackerPen, src.characters.DamageType.PHYSICAL);
        incrementUse();
    }
}
