package src.items;

import src.characters.Player;

public class Boots extends Armor {
    public Boots(String name, int defBonus, int hpBonus) {
        super(name, defBonus, hpBonus);
    }

    @Override
    public void equip(Player target) {
        target.equipBoots(this);
        System.out.println("You equipped boots: " + getName());
    }
}
