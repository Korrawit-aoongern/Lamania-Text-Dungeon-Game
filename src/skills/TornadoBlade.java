package src.skills;

import src.characters.Character;

class TornadoBlade extends PhysicalSkill {
    public TornadoBlade() { super("Tornado Blade", 70); }

    @Override
    public void use(Character user, Character target) {
        int total = 0;
        for (int i=0; i<10; i++) {
            int dmg = calcDamage(user, 12, 0);
            target.takeDamage(dmg);
            total += dmg;
        }
        System.out.println(user.getName() + " summons Tornado Blade hitting 10 times for total " + total + " damage! (Cannot act 2 turns)");
        incrementUse();
    }
}
