package src.skills;

import src.characters.Character;

public class BasicAttack implements Skill {
    @Override
    public String getName() { return "Basic Attack"; }
    @Override
    public int getCost() { return 0; }
    @Override
    public void use(Character user, Character target) {
        System.out.println(user.getName() + " strikes with a basic attack!");
        target.takeDamage(user.getAtk());
    }
}