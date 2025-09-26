package src.skills;

import src.characters.Character;

public interface Skill {
    String getName();
    int getCost();
    void use(Character user, Character target);
}
