package src.items;

import src.characters.Player;

public class Helmet extends Armor {
    public Helmet(String name, int defBonus, int hpBonus) {
        super(name, defBonus, hpBonus);
    }

    @Override
    public void equip(Player target) {
        target.equipHelmet(this);
        System.out.println("You equipped helmet: " + getName());
    }
}
